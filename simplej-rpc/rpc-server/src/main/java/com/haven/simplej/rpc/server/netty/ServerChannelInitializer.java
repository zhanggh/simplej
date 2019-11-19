package com.haven.simplej.rpc.server.netty;


import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.heartbeat.HeartBeatServerHandler;
import com.haven.simplej.rpc.protocol.coder.RpcDecoder;
import com.haven.simplej.rpc.protocol.coder.RpcEncoder;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.server.netty.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * nio channel 初始化
 * @author havenzhang
 * @date 2018/04/01.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters;

	/**
	 * 业务处理器
	 */
	private IServiceProcessor processor;

	public ServerChannelInitializer(IServiceProcessor processor, List<RpcFilter> filters) {
		super();
		this.filters = filters;
		this.processor = processor;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		//解决粘包和拆包的问题
		socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 2, 4, 4, 0)); //解码request
		socketChannel.pipeline().addLast(new RpcDecoder()); //解码request
		socketChannel.pipeline().addLast(new RpcEncoder()); //编码response
		socketChannel.pipeline().addLast(new ServerHandler(processor, filters)); //使用ServerHandler类来处理接收到的消息
		socketChannel.pipeline().addLast(new HeartBeatServerHandler()); //使用ServerHandler类来处理接收到的消息
		long readerIdleTime = PropertyManager.getLong(RpcConstants.RPC_READER_IDLE_TIME, 10);
		long allIdleTime = PropertyManager.getLong(RpcConstants.RPC_ALL_IDLE_TIME, 20);
		socketChannel.pipeline().addLast(new IdleStateHandler(readerIdleTime, 0, allIdleTime, TimeUnit.SECONDS));
		//空闲检测
	}
}