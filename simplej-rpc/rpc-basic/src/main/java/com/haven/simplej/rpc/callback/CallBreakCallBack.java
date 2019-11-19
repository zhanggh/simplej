package com.haven.simplej.rpc.callback;

import com.haven.simplej.rpc.model.RpcHeader;

/**
 * 客户端连接中断异常回调器
 * 使用方式：实现该接口，并通过spring实例化（也就是由spring管理bean对象），
 * 框架就会自动获取到回调对象
 * @author: havenzhang
 * @date: 2018/10/30 23:34
 * @version 1.0
 */
public interface CallBreakCallBack {

	/**
	 * 回调方法
	 * @param header rpc 请求头部
	 */
	void execute(RpcHeader header);
}
