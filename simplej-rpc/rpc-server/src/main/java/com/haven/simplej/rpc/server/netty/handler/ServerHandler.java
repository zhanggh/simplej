package com.haven.simplej.rpc.server.netty.handler;


import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.callback.CallBreakCallBack;
import com.haven.simplej.rpc.enums.MsgFlag;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.filter.impl.RpcFilterChain;
import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * 每个请求进来都会产生一个新的handler对象
 * @author haven.zhang
 * @date 2019/1/29.
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<BaseRpcMessage> {
	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters;

	/**
	 * 业务处理器
	 */
	private IServiceProcessor processor;

	/**
	 * 请求头部
	 */
	private RpcHeader header;

	public ServerHandler(IServiceProcessor processor, List<RpcFilter> filters) {
		super();
		this.filters = filters;
		this.processor = processor;
	}


	@Override
	public void channelRead0(ChannelHandlerContext ctx, BaseRpcMessage msg) {

		RpcRequest request = new RpcRequest(msg);
		RpcResponse response = new RpcResponse();
		response.setMsgFlag(request.getMsgFlag());
		response.setHeader(request.getHeader());
		header = request.getHeader();
		if (request == null || request.getMsgFlag() == 0) {
			log.debug("channelRead0 , request is null");
			response.getHeader().setRespCode(RpcError.SUCCESS.getErrorCode());
			response.getHeader().setRespMsg("heartbeat success");
			ctx.writeAndFlush(response);
			return;
		}
		String methodId = request.getHeader().getMethodId();
		//首先交给服务总线程池处理任务，如果总线程池满了，则交给方法线程池，还是拒绝，就返回失败
		ThreadPoolFactory.getServerExecutor().execute(() -> {
			try {
				//服务线程池执行任务
				executeTask(ctx, request);
			} catch (RpcException e) {
				log.error("channelRead0 error,msg header:{}", JSON.toJSONString(request.getHeader()), e);
				if (StringUtil.equalsIgnoreCase(e.getRespCode(), RpcError.SERVER_OVERLOAD.getErrorCode())) {
					ThreadPoolFactory.createMethodExecutor(methodId).execute(() -> {
						try {
							//方法线程池执行任务
							executeTask(ctx, request);
						} catch (RpcException e1) {
							log.error("channelRead0 error", e);
							response.getHeader().setRespCode(e.getRespCode());
							response.getHeader().setRespMsg(e.getRespMsg());
							ctx.writeAndFlush(response);
						} catch (Exception e2) {
							log.error("channelRead0 error", e);
							response.getHeader().setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
							response.getHeader().setRespMsg(ExceptionUtil.stackTraceText(e));
							ctx.writeAndFlush(response);
						}
					});

				} else {
					response.getHeader().setRespCode(e.getRespCode());
					response.getHeader().setRespMsg(e.getRespMsg());
					ctx.writeAndFlush(response);
				}
			} catch (Exception e) {
				log.error("channelRead0 error", e);
				response.getHeader().setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
				response.getHeader().setRespMsg(ExceptionUtil.stackTraceText(e));
				ctx.writeAndFlush(response);
			}
		});

	}

	/**
	 * 异步执行任务
	 */
	private void executeTask(ChannelHandlerContext ctx, RpcRequest request) {
		long start = System.currentTimeMillis();
		RpcResponse response = new RpcResponse();
		response.setMsgFlag(request.getMsgFlag());
		//链路id
		SequenceUtil.putTraceId(request.getHeader().getMsgId());
		//返回的数据结构
		FilterChain chain = new RpcFilterChain();
		((RpcFilterChain) chain).init(processor, filters);
		chain.doFilter(request, response);
		log.debug("process response cost:{}", System.currentTimeMillis() - start);
		if (response.getHeader() == null) {
			response.setHeader(request.getHeader());
		}
		ctx.writeAndFlush(response);
		if (!request.getHeader().isKeepalive()) {
			ctx.close();
		}
	}

	//通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.debug("服务端接收数据完毕..");
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.debug("server channelActive>>>>>>>>,remoteAddress:{}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		log.debug("channelRegistered>>>>>>>>");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		log.debug("channelUnregistered>>>>>>>>");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		if (cause instanceof IOException) {
			log.debug("remote client has closed");
			//客户端网络异常/中断时，回调处理
			List<CallBreakCallBack> callBackList = SpringContext.getBeansOfType(CallBreakCallBack.class);
			if (CollectionUtil.isNotEmpty(callBackList)) {
				ThreadPoolFactory.getServerExecutor().execute(() -> callBackList.parallelStream().forEach(e -> e.execute(header)));
			}
			return;
		}

		RpcResponse response = new RpcResponse();
		response.setMsgFlag(MsgFlag.BUSINESS.getValue());
		RpcHeader header = new RpcHeader();
		if (cause instanceof RpcException) {
			header = ((RpcException) cause).getHeader();
			RpcException rpcException = (RpcException) cause;
			header.setRespCode(rpcException.getRespCode());
			header.setRespMsg(rpcException.getRespMsg());
		} else if (cause.getCause() instanceof RpcException) {
			header = ((RpcException) cause.getCause()).getHeader();
			RpcException rpcException = (RpcException) cause.getCause();
			header.setRespCode(rpcException.getRespCode());
			header.setRespMsg(rpcException.getRespMsg());
		} else {
			header.setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
			header.setRespMsg(ExceptionUtil.stackTraceText(cause));
		}
		if (header.getSerialType() == 0) {
			header.setSerialType(SerialType.JSON.getValue());
		}
		response.setHeader(header);
		ctx.writeAndFlush(response);
		super.exceptionCaught(ctx, cause);
		log.error("exceptionCaught>>>>>>>>", cause);
		ctx.close();
	}
}