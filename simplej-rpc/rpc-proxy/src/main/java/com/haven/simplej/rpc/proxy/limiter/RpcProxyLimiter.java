package com.haven.simplej.rpc.proxy.limiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: havenzhang
 * @date: 2018/11/17 23:09
 * @version 1.0
 */
@Slf4j
@Component
public class RpcProxyLimiter implements RpcLimiter {


	@Override
	public boolean tryAquire(String key, int permits, long timeout) {
		return false;
	}

	@Override
	public void release(String key, int permits) {

	}

	@Override
	public int getFlowLimit(String key) {
		return 0;
	}
}
