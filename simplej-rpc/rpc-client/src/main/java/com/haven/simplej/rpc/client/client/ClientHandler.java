package com.haven.simplej.rpc.client.client;

/**
 * @author: havenzhang
 * @date: 2018/4/26 16:16
 * @version 1.0
 */

import com.haven.simplej.rpc.client.client.callback.ISendCallBack;
import com.haven.simplej.rpc.client.client.message.MessageHelper;
import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.model.RpcResponse;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<BaseRpcMessage> {

	//处理服务端返回的数据
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseRpcMessage msg) throws Exception {
		PerformanceUtil.start(this.getClass().getSimpleName());
		RpcResponse response = new RpcResponse(msg);
		log.debug("read response from server ,response message:{}", response.toString());
		MessageHelper.addMessage(response.getHeader().getMsgId(), response);
		ChannelPromise promise = MessageHelper.getPromise(response.getHeader().getMsgId());
		if (promise != null) {
			promise.setSuccess();
		}

		//回调处理
		List<ISendCallBack> callBackList = MessageHelper.getCallBack(response.getHeader().getMsgId());
		if (CollectionUtil.isEmpty(callBackList)) {
			return;
		}
		for (ISendCallBack callBack : callBackList) {
			if (callBack != null) {
				callBack.callback(response);
			}
		}
		log.debug("callback cost:{}", PerformanceUtil.end(this.getClass().getSimpleName()));

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.debug("client 连接激活 ,remoteAddress:{}", ctx.channel().remoteAddress());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.info("client handler error", cause);
		ctx.close();
	}


}

