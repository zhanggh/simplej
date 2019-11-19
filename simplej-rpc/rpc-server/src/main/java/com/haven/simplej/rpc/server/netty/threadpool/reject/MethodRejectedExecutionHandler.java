package com.haven.simplej.rpc.server.netty.threadpool.reject;

import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 方法线程池拒绝策略
 * @author: havenzhang
 * @date: 2019/1/29 20:05
 * @version 1.0
 */
@Slf4j
public class MethodRejectedExecutionHandler implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		log.debug("overload,start flow limit please");
		throw new RpcException(RpcError.SERVER_METHOD_OVERLOAD);
	}
}
