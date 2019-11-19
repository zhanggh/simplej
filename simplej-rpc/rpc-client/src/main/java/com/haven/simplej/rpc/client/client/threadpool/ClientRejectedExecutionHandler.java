package com.haven.simplej.rpc.client.client.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 请求过载策略
 * @author: havenzhang
 * @date: 2018/9/29 20:43
 * @version 1.0
 */
@Slf4j
public class ClientRejectedExecutionHandler implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		log.debug("overload,start flow limit please");
	}
}