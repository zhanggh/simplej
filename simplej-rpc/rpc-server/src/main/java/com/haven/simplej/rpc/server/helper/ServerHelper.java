package com.haven.simplej.rpc.server.helper;

import com.haven.simplej.rpc.client.client.threadpool.ClientThreadPoolFactory;
import com.haven.simplej.rpc.server.netty.NettyServer;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.context.ShutdownEndpoint;

/**
 * 服务管理器
 * @author: havenzhang
 * @date: 2018/9/10 13:20
 * @version 1.0
 */
@Slf4j
public class ServerHelper {

	/**
	 * 关闭Tomcat服务
	 */
	public static void shutdownTomcat() {
		log.debug("shutdownTomcat............");
		ShutdownEndpoint shutdownEndpoint = SpringContext.getBean(ShutdownEndpoint.class);
		if(shutdownEndpoint != null){
			shutdownEndpoint.shutdown();
		}
	}

	/**
	 *  关闭netty服务
	 */
	public static void shutdownNetty() {
		log.debug("shutdownNetty............");
		NettyServer.destroy();
	}

	/**
	 * 关闭所有服务
	 */
	public static void shutdownAll() {
		log.debug("shutdownAll............");
		ThreadPoolFactory.shutdown();
		ClientThreadPoolFactory.shutdown();
		shutdownTomcat();
		shutdownNetty();
	}


}
