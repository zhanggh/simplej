package com.haven.simplej.rpc.callback;

/**
 * 快速失败响应回调
 * @author: havenzhang
 * @date: 2018/10/11 12:31
 * @version 1.0
 */
public interface Fallback {

	/**
	 * 快速失败回退方法,直接响应该调用者预先指定的结果
	 * @return Object
	 */
	Object doResponse();

	/**
	 * 快速失败后，回调钩子
	 */
	void callback(Object response);

	/**
	 * 指定该钩子是属于哪个远程方法的
	 * 必须保证唯一
	 * @return MethodName
	 */
	String getMethodName();
}
