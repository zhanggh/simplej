package com.haven.simplej.rpc.client.client;

import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.callback.ISendCallBack;
import com.haven.simplej.rpc.client.client.message.MessageHelper;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.heartbeat.HeartBeatServerHandler;
import com.haven.simplej.rpc.model.BaseRpcMessage;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.coder.RpcDecoder;
import com.haven.simplej.rpc.protocol.coder.RpcEncoder;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.text.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * netty 客户端组件，支持channel池化
 * @TODO 有待性能优化
 * @author: havenzhang
 * @date: 2018/4/26 16:15
 * @version 1.0
 */
@Slf4j
public class NettyClient {


	/**
	 * 	客户端缓存
	 */
	private static Map<String, NettyClient> clientMap = Maps.newConcurrentMap();
	/**
	 * 连接次数
	 */
	private static LongAdder counter = new LongAdder();

	private static EventLoopGroup group;

	private String host;
	private int port;

	private Channel channel;

	public static NettyClient getInstance(String host, int port) {
		String key = host + ":" + port;
		NettyClient client = clientMap.get(key);
		if (client == null) {
			synchronized (NettyClient.class) {
				client = clientMap.get(key);
				if (client == null) {
					client = new NettyClient(host, port);
					int clientThreadCount = PropertyManager.getInt(RpcConstants.RPC_CLIENT_CONNECT_POOL_SIZE_KEY, 5);
					//			, ClientThreadPoolFactory.getClientExecutor()
					group = new NioEventLoopGroup(clientThreadCount);
					clientMap.put(key, client);
				}
			}
		}
		return client;
	}


	//连接服务端的端口号地址和端口号
	private NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}


	public Channel getChannel() {
		if (channel != null && channel.isActive()) {
			log.debug("------------------channel.isActive---------------------------");
			return channel;
		}
		return connect();
	}

	/**
	 * 同步发送请求
	 * @param request 请求对象
	 * @return <R> 返回对象
	 */
	public <T> T send(BaseRpcMessage request) {
		T resp;
		try {
			long start = System.currentTimeMillis();
			//设置header信息
			setHeader(request);
			Channel channel = getChannel();
			ChannelPromise promise = channel.newPromise();
			MessageHelper.addPromise(request.getHeader().getMsgId(), promise);
			channel.writeAndFlush(request);
									promise.awaitUninterruptibly();
//			promise.await(request.getHeader().getTimeout(), TimeUnit.MILLISECONDS);
			log.debug("client request cost:{}", (System.currentTimeMillis() - start));
			if (promise.isSuccess()) {
				resp = MessageHelper.take(request.getHeader().getMsgId());
			} else {
				if (!promise.isDone()) {
					log.debug("request timeout");
					resp = (T) ResponseHelper.buildResponse(RpcError.NOT_SUPPORT_SERIAL_TYPE, "request timeout",
							request.getHeader());
				} else {
					log.debug("request fail");
					resp = (T) ResponseHelper.buildResponse(RpcError.NOT_SUPPORT_SERIAL_TYPE, "request fail",
							request.getHeader());
				}
			}
		} catch (Exception e) {
			log.error("send message error", e);
			throw new UncheckedException(e);
		} finally {
			MessageHelper.removePromise(request.getHeader().getMsgId());
		}
		return resp;
	}

	/**
	 * 设置rpc header参数
	 */
	private void setHeader(BaseRpcMessage request) {
		if (request.getHeader() == null) {
			request.setHeader(new RpcHeader());
		}
		if (StringUtil.isEmpty(request.getHeader().getMsgId())) {
			request.getHeader().setMsgId(SequenceUtil.generateId());
		}
	}

	/**
	 * 异步发送请求
	 * @param request
	 * @param callBack
	 */
	public RpcResponse sendAsyn(BaseRpcMessage request, ISendCallBack callBack) {
		long start = System.currentTimeMillis();
		//设置header信息
		setHeader(request);
		if (callBack != null) {
			MessageHelper.addCallback(request.getHeader().getMsgId(), callBack);
		}

		Channel channel = getChannel();
		channel.writeAndFlush(request);
//		channel.flush();
		RpcResponse response = ResponseHelper.buildResponse(RpcError.SUCCESS, "send success", request.getHeader());
		log.debug("client asyn request cost:{}", (System.currentTimeMillis() - start));
		return response;
	}


	/**
	 * 连接channel
	 * @return
	 */
	public Channel connect() {
		counter.add(1);
		Bootstrap b = new Bootstrap();
		try {
			int connectTimeout = PropertyManager.getInt(RpcConstants.RPC_CLIENT_CONNECT_TIMEOUT, 500);
			//			int socketTimeout = PropertyManager.getInt(RpcConstants.RPC_CLIENT_SOCKET_TIMEOUT, 5000);
			b.group(group).channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout).option(ChannelOption.SO_KEEPALIVE,
					Boolean.TRUE)
					//					.(ChannelOption.SO_TIMEOUT,socketTimeout)
					.handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							log.debug("正在连接中...channel count:{}", counter.sum());
							ChannelPipeline pipeline = ch.pipeline();
							//解决粘包和拆包的问题
							pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 2, 4, 4, 0)); //解码request
							pipeline.addLast(new RpcEncoder()); //编码request
							pipeline.addLast(new RpcDecoder()); //解码response
							pipeline.addLast(new ClientHandler()); //客户端处理类
							pipeline.addLast(new HeartBeatServerHandler()); //使用ServerHandler类来处理接收到的消息
							long writerIdleTime = PropertyManager.getLong(RpcConstants.RPC_WRITER_IDLE_TIME, 10);
							long allIdleTime = PropertyManager.getLong(RpcConstants.RPC_ALL_IDLE_TIME, 20);
							pipeline.addLast(new IdleStateHandler(0, writerIdleTime, allIdleTime, TimeUnit.SECONDS));
							//空闲检测
						}
					});
			//发起异步连接请求，绑定连接端口和host信息
			final ChannelFuture future = b.connect(host, port).sync();

			future.addListener((ChannelFutureListener) arg0 -> {
				if (future.isSuccess()) {
					log.debug("连接服务器成功,ip:{},port:{}", host, port);
				} else {
					log.debug("连接服务器失败,ip:{},port:{}", host, port, future.cause());
					close(); //关闭线程组
				}
			});

			channel = future.channel();
			return channel;
		} catch (InterruptedException e) {
			log.error("netty connect fail", e);
			throw new UncheckedException(e);
		}
	}

	public void close() {
		group.shutdownGracefully(); //关闭线程组
	}
}
