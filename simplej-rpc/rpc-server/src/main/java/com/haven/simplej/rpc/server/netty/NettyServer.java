package com.haven.simplej.rpc.server.netty;


import com.google.common.collect.Lists;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.server.enums.ServerState;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * netty服务端，一个java进程只能启动一个netty服务，不支持启动多个
 * @author haven.zhang
 * @date 2019/1/01.
 */
@Slf4j
public class NettyServer {


	/**
	 * netty服务状态，0-未启动状态，1-启动正常，2-启动失败
	 */
	private static volatile int state = ServerState.INIT.state();
	/**
	 * 主事件监听处理线程组,用于接受channel连接
	 */
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(PropertyManager.getInt(RpcConstants.RPC_SERVER_NETTY_BOSS_THREAD_SIZE,0));
	/**
	 * 工作事件监听处理线程组
	 */
	private static final EventLoopGroup workerGroup =  new NioEventLoopGroup(PropertyManager.getInt(RpcConstants.RPC_SERVER_NETTY_WOKER_THREAD_SIZE,0));

	private static Channel channel;

	private static NettyServer server;

	/**
	 * 责任链
	 */
	private static List<RpcFilter> filters = Lists.newArrayList();

	/**
	 * 处理器
	 */
	private static IServiceProcessor processor;

	/**
	 * 启动完成监听器
	 */
	private static List<StartUpListener> listeners = Lists.newArrayList();

	/**
	 * 监听端口
	 */
	private volatile int port;

	/**
	 * 服务状态
	 * netty服务状态，0-未启动状态，1-启动正常，2-启动失败
	 * @return int
	 */
	public static int getState() {
		return state;
	}

	/**
	 * 第一步，监听服务端口
	 * @param port 服务端口
	 * @return NettyServer
	 */
	public synchronized static NettyServer listen(int port) {
		if (server == null) {
			server = new NettyServer();
			server.port = port;
		}
		return server;
	}

	/**
	 * 第二步，指定业务处理器
	 * @param processor 业务处理器
	 * @return NettyServer
	 */
	public NettyServer setProcessor(IServiceProcessor processor) {
		server.processor = processor;
		return server;
	}

	/**
	 * 第三步，增加rpc过滤器，假如有的话！
	 * @param filters 过滤器
	 * @return NettyServer
	 */
	public NettyServer addFilters(List<RpcFilter> filters) {
		server.filters.addAll(filters);
		return server;
	}

	/**
	 * 第四步，增加启动完成监听器，假如有的话！
	 * @param listeners 监听器
	 * @return NettyServer
	 */
	public NettyServer addStartUpListener(List<StartUpListener> listeners) {
		server.listeners.addAll(listeners);
		return server;
	}


	/**
	 * 第四步，最后一步
	 * 启动服务
	 */
	public ChannelFuture start() {

		ChannelFuture future = null;
		try {
			int rcvbuf = PropertyManager.getInt(RpcConstants.RPC_SERVER_RCVBUF, 2 * 1024);
			int backlog = PropertyManager.getInt(RpcConstants.RPC_SERVER_BACKLOG, 2 * 1024);
			int sendbuf = PropertyManager.getInt(RpcConstants.RPC_SERVER_SENDBUF, 2 * 1024);
			PooledByteBufAllocator allocator = new PooledByteBufAllocator(true);
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ServerChannelInitializer(processor, filters)).option(ChannelOption.SO_REUSEADDR, Boolean.TRUE).option(ChannelOption.SO_RCVBUF, rcvbuf)//缓冲区大小
					.option(ChannelOption.SO_BACKLOG, backlog)//连接队列容量，超过容量时，服务端拒绝连接
					.option(ChannelOption.SO_SNDBUF, sendbuf).option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE).option(ChannelOption.ALLOCATOR, allocator).childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE).childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE).childOption(ChannelOption.ALLOCATOR, allocator);
			String host = RpcHelper.getRpcServerHost();
			future = bootstrap.bind(new InetSocketAddress(host, port)).sync();
			channel = future.channel();
			if (future.isSuccess()) {
				log.debug("服务端启动成功");
				state = ServerState.STARTUP_SUCC.state();
				listeners.forEach(listener -> listener.listen());
			} else {
				log.debug("服务端启动失败");
				destroy();
				System.exit(-1);
			}

			//成功绑定到端口之后,给channel增加一个 管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程。
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("Netty connect error:", e);
			throw new UncheckedException(e);
		} finally {
			if (future != null && future.isSuccess()) {
				log.info("Netty server listening on port " + port + " and ready for connections...");
			} else {
				log.error("Netty server connect up Error!");
			}
		}

		return future;
	}

	/**
	 * 关闭netty服务
	 */
	public static void destroy() {
		log.info("Shutdown Netty Server...");
		if (bossGroup != null) {
			//先关闭监听连接的线程组
			bossGroup.shutdownGracefully(); //关闭线程组
		}
		try {
			//服务关闭等待时间
			int closeWait = PropertyManager.getInt(RpcConstants.RPC_SERVER_CLOSE_TIME_WAIT,5000);
			Thread.sleep(closeWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
		state = ServerState.STARTUP_FAIL.state();
		log.info("Shutdown Netty Server Success!");
	}


}
