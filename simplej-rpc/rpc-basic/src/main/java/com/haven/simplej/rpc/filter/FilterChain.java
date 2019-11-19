package com.haven.simplej.rpc.filter;

import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;

/**
 * 责任链
 * @author: havenzhang
 * @date: 2019/1/26 19:23
 * @version 1.0
 */
public interface FilterChain {

	void doFilter(RpcRequest request, RpcResponse response);
}
