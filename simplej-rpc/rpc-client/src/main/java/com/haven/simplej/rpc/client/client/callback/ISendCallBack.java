package com.haven.simplej.rpc.client.client.callback;

import com.haven.simplej.rpc.model.RpcResponse;

/**
 * 消息回调接口
 * @author: havenzhang
 * @date: 2019/5/13 21:10
 * @version 1.0
 */
public interface ISendCallBack {

	/**
	 * 回调方法
	 * @param response
	 */
	void callback(RpcResponse response);
}
