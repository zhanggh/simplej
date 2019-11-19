package com.haven.simplej.rpc.client.client.callback;

/**
 * 业务回调
 * @author: havenzhang
 * @date: 2019/5/13 21:47
 * @version 1.0
 */
public interface IServiceCallBack<T> {

	/**
	 * 回调执行方法
	 * @param response rpc响应的结果
	 */
	void call(T response);

	/**
	 * 指定该回调作用的方法名
	 * @return
	 */
	String getMethodName();
}
