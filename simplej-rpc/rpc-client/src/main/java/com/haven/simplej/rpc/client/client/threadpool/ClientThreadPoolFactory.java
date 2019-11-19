package com.haven.simplej.rpc.client.client.threadpool;

import com.haven.simplej.property.PropertyManager;

import java.util.concurrent.*;

/**
 * 客户端线程池管理器
 * @author: havenzhang
 * @date: 2018/9/29 20:41
 * @version 1.0
 */
public class ClientThreadPoolFactory {

	/**
	 * 客户端线程池锁
	 */
	private static Object clientExecutorLock = new Object();


	/**
	 * 客户端级别的线程池
	 */
	private static ExecutorService clientExecutor;

	/**
	 *  过载策略
	 */
	private static ClientRejectedExecutionHandler rejectedExecutionHandler = new ClientRejectedExecutionHandler();


	/**
	 * 设置服务级别的线程池
	 * @return
	 */
	public static Executor getClientExecutor() {
		if (clientExecutor == null) {
			synchronized (clientExecutorLock) {
				if (clientExecutor == null) {
					int coreSize = Integer.parseInt(PropertyManager.get("simplej.netty.client.thread.pool.core.size",
							"10"));
					int maximumPoolSize = Integer.parseInt(PropertyManager.get("simplej.netty.client.thread.pool.max"
							+ ".size", "300"));
					long keepAliveTime = Integer.parseInt(PropertyManager.get("simplej.netty.client.thread.pool"
							+ ".keepalive.time", "30000"));
					int queueLen = Integer.parseInt(PropertyManager.get("simplej.netty.client.thread.pool.queue"
							+ ".length", "5000"));
					clientExecutor = new ThreadPoolExecutor(coreSize, maximumPoolSize, keepAliveTime,
							TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueLen), rejectedExecutionHandler);
				}
			}
		}
		return clientExecutor;
	}


	public static void shutdown() {
		if (clientExecutor != null) {
			clientExecutor.shutdown();
		}
	}
}
