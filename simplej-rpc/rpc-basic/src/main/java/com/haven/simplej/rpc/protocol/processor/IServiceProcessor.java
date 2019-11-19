package com.haven.simplej.rpc.protocol.processor;

import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;

/**
 * 业务处理器接口类
 * @author: havenzhang
 * @date: 2018/4/26 17:44
 * @version 1.0
 */
public interface IServiceProcessor {

	/**
	 * 业务处理方法
	 * @param request 请求model
	 * @return RpcResponse
	 */
	RpcResponse process(RpcRequest request);
}
