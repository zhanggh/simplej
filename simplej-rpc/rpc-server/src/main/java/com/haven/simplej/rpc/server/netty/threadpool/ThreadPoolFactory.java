package com.haven.simplej.rpc.server.netty.threadpool;

import com.google.common.collect.Maps;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.server.netty.threadpool.reject.MethodRejectedExecutionHandler;
import com.haven.simplej.rpc.server.netty.threadpool.reject.ServerRejectedExecutionHandler;
import com.vip.vjtools.vjkit.collection.MapUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 服务线程池管理器
 * @author: havenzhang
 * @date: 2018/5/4 15:42
 * @version 1.0
 */
@Slf4j
public class ThreadPoolFactory {


	private static Object serverExecutorLock = new Object();
	private static Object scheduledExecutorLock = new Object();
	private static Object methodExecutorLock = new Object();
	private static Object heartbeatExecutorLock = new Object();
	private static Object configListenExecutorLock = new Object();

	/**
	 * 线程状态：0-正常，1-关闭
	 */
	private static volatile int state = 0;

	/**
	 * 服务级别的线程池
	 */
	private static ExecutorService serverExecutor;


	/**
	 * methodId与methodExecutor的缓存关系
	 */
	private static Map<String, ExecutorService> methodExecutorMap = Maps.newHashMap();
	/**
	 * 服务心跳检测专用的线程池
	 */
	private static ExecutorService heartbeatExecutor;
	/**
	 * 配置监听专用的线程池
	 */
	private static ExecutorService configListenExecutor;

	/**
	 * 计划调度线程池
	 */
	private static ScheduledExecutorService executorService;

	/**
	 *  服务过载策略
	 */
	private static ServerRejectedExecutionHandler rejectedExecutionHandler = new ServerRejectedExecutionHandler();

	/**
	 * 方法请求过载拒绝策略
	 */
	private static MethodRejectedExecutionHandler methodRejectedExecutionHandler = new MethodRejectedExecutionHandler();

	/**
	 * 设置服务级别的线程池
	 * @return
	 */
	public static ExecutorService getServerExecutor() {
		if (serverExecutor == null) {
			synchronized (serverExecutorLock) {
				if (serverExecutor == null) {

					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.thread.pool.core.size", "100"));
					int maximumPoolSize = Integer.parseInt(PropertyManager.get("simplej.netty.thread.pool.max.size",
							"300"));
					long keepAliveTime = Integer.parseInt(PropertyManager.get("simplej.netty.thread.pool.keepalive" +
							".time", "30000"));
					int queueLen = Integer.parseInt(PropertyManager.get("simplej.netty.thread.pool.queue.length",
							"5000"));
					serverExecutor = new ThreadPoolExecutor(coreSize, maximumPoolSize, keepAliveTime,
							TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueLen), rejectedExecutionHandler);
				}
			}
		}
		return serverExecutor;
	}

	/**
	 * 获取该方法的独立线程池
	 * @param methodId 本地方法的唯一id
	 * @return ExecutorService
	 */
	public static ExecutorService getMethodExecutor(String methodId) {

		return methodExecutorMap.get(methodId);
	}

	/**
	 * 创建方法级别的线程池
	 * @return ExecutorService
	 */
	public static ExecutorService createMethodExecutor(String methodId) {
		ExecutorService methodExecutor = methodExecutorMap.get(methodId);
		if (methodExecutor == null) {
			synchronized (methodExecutorLock) {
				methodExecutor = methodExecutorMap.get(methodId);
				if (methodExecutor == null) {
					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.method.thread.pool.core.size",
							"5"));
					int maximumPoolSize =
							Integer.parseInt(PropertyManager.get("simplej.netty.method.thread.pool.max" + ".size",
									"300"));
					long keepAliveTime = Integer.parseInt(PropertyManager.get("simplej.netty.method.thread.pool" +
							".keepalive.time", "30000"));
					int queueLen = Integer.parseInt(PropertyManager.get("simplej.netty.method.thread.pool.queue" +
							".length", "5000"));
					methodExecutor = new ThreadPoolExecutor(coreSize, maximumPoolSize, keepAliveTime,
							TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueLen), methodRejectedExecutionHandler);

					methodExecutorMap.put(methodId, methodExecutor);
				}
			}
		}
		return methodExecutor;
	}


	/**
	 * 获取心跳检测专用线程池
	 * @return
	 */
	public static ExecutorService getHeartbeatExecutor() {
		if (heartbeatExecutor == null) {
			synchronized (heartbeatExecutorLock) {
				if (heartbeatExecutor == null) {
					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.heart.thread.pool.core.size",
							"100"));
					int maximumPoolSize =
							Integer.parseInt(PropertyManager.get("simplej.netty.heart.thread.pool.max" + ".size", "300"
							));
					long keepAliveTime = Integer.parseInt(PropertyManager.get("simplej.netty.heart.thread.pool" +
							".keepalive.time", "30000"));
					int queueLen = Integer.parseInt(PropertyManager.get("simplej.netty.heart.thread.pool.queue.length"
							, "5000"));
					heartbeatExecutor = new ThreadPoolExecutor(coreSize, maximumPoolSize, keepAliveTime,
							TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueLen));
				}
			}
		}
		return heartbeatExecutor;
	}


	/**
	 * 获取配置监听专用线程池
	 * @return
	 */
	public static Executor getConfigListenExecutor() {
		if (configListenExecutor == null) {
			synchronized (configListenExecutorLock) {
				if (configListenExecutor == null) {
					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.config.thread.pool.core.size",
							"2"));
					int maximumPoolSize =
							Integer.parseInt(PropertyManager.get("simplej.netty.config.thread.pool.max" + ".size", "3"
							));
					long keepAliveTime = Integer.parseInt(PropertyManager.get("simplej.netty.config.thread.pool" +
							".keepalive.time", "30000"));
					int queueLen = Integer.parseInt(PropertyManager.get("simplej.netty.config.thread.pool.queue" +
							".length", "5000"));
					configListenExecutor = new ThreadPoolExecutor(coreSize, maximumPoolSize, keepAliveTime,
							TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueLen));
				}
			}
		}
		return configListenExecutor;
	}

	/**
	 * 获取计划调度线程执行器
	 * @return
	 */
	public static ScheduledExecutorService getScheduleExecutor() {
		if (executorService == null) {
			synchronized (scheduledExecutorLock) {
				if (executorService == null) {
					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.scheduled.thread.pool.core" +
							".size", "10"));
					executorService = new ScheduledThreadPoolExecutor(coreSize);
				}
			}
		}
		return executorService;
	}


	/**
	 * 关闭所有线程池
	 */
	public static void shutdown() {
		log.debug("shutdownAll thread pool............");
		state = 1;
		if (executorService != null) {
			executorService.shutdown();
		}
		if (MapUtil.isNotEmpty(methodExecutorMap)) {
			methodExecutorMap.forEach((k, executor) -> executor.shutdown());
		}
		if (serverExecutor != null) {
			serverExecutor.shutdown();
		}
		if (heartbeatExecutor != null) {
			heartbeatExecutor.shutdown();
		}
		if (configListenExecutor != null) {
			configListenExecutor.shutdown();
		}

	}

	/**
	 * 查询线程池管理器的状态
	 * @return int
	 */
	public static int getState() {
		return state;
	}
}
