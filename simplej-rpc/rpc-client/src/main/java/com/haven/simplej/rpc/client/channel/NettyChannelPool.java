package com.haven.simplej.rpc.client.channel;


import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.ClientHandler;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.heartbeat.HeartBeatServerHandler;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.coder.RpcDecoder;
import com.haven.simplej.rpc.protocol.coder.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * 客户端连接池
 * @author: havenzhang
 * @date: 2018/4/28 15:31
 * @version 1.0
 */
@Slf4j
public class NettyChannelPool {

	private static EventLoopGroup group;

	private String host;
	private int port;

	private Channel channel;
	/**
	 * 连接次数
	 */
	private LongAdder counter = new LongAdder();

	private static NettyChannelPool pool;



	public static NettyChannelPool getInstance(){

		if(pool== null){
			synchronized (NettyChannelPool.class){
				if(pool== null){
					pool = new NettyChannelPool();
					int clientThreadCount = PropertyManager.getInt(RpcConstants.RPC_CLIENT_CONNECT_POOL_SIZE_KEY,5);
					//					client.group = new NioEventLoopGroup(clientThreadCount, ClientThreadPoolFactory.getClientExecutor());
					group = new NioEventLoopGroup(clientThreadCount);
				}
			}
		}
		return pool;
	}

	public Channel getChannel(){

		return null;
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
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
					.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
					//					.(ChannelOption.SO_TIMEOUT,socketTimeout)
					.handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							log.debug("正在连接中...channel count:{}", counter.sum());
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new RpcEncoder()); //编码request
							pipeline.addLast(new RpcDecoder()); //解码response
							pipeline.addLast(new ClientHandler()); //客户端处理类
							pipeline.addLast(new HeartBeatServerHandler()); //使用ServerHandler类来处理接收到的消息
							long writerIdleTime = PropertyManager.getLong(RpcConstants.RPC_WRITER_IDLE_TIME, 10);
							long allIdleTime = PropertyManager.getLong(RpcConstants.RPC_ALL_IDLE_TIME, 20);
							pipeline.addLast(new IdleStateHandler(0, writerIdleTime, allIdleTime, TimeUnit.SECONDS)); //空闲检测
						}
					});
			//发起异步连接请求，绑定连接端口和host信息
			final ChannelFuture future = b.connect(host, port).sync();

			future.addListener((ChannelFutureListener) arg0 -> {
				if (future.isSuccess()) {
					log.debug("连接服务器成功,ip:{},port:{}",host,port);
				} else {
					log.debug("连接服务器失败,ip:{},port:{}",host,port, future.cause());
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

	public void close(){
		group.shutdownGracefully(); //关闭线程组
	}
}
