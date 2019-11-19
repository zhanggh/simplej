package com.haven.simplej.rpc.proxy.limiter;

/**
 * @author: havenzhang
 * @date: 2018/11/17 22:45
 * @version 1.0
 */
public interface RpcLimiter {

	/**
	 * 尝试获取许可
	 * @param key  资源key
	 * @param permits  许可数量
	 * @param timeout 等待超时时间,毫秒
	 * @return boolean
	 */
	boolean tryAquire(String key, int permits, long timeout);

	/**
	 * 释放许可
	 * @param key  资源key
	 * @param permits 许可数量
	 */
	void release(String key, int permits);

	/**
	 * 获取流控最大qps限制阈值
	 * @param key  资源key
	 * @return int
	 */
	int getFlowLimit(String key);
}
