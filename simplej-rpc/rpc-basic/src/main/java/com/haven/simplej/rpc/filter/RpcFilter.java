package com.haven.simplej.rpc.filter;

import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;

/**
 * 过滤器接口
 * 业务自定义的filter，order请设置在300以上，[0-300]为框架内用
 * @author: havenzhang
 * @date: 2018/5/26 17:38
 * @version 1.0
 */
public interface RpcFilter{

	/**
	 * 过滤方法
	 * @param request 请求
	 * @param response 响应
	 */
	void doFilter(RpcRequest request, RpcResponse response,FilterChain chain);

	/**
	 * filter 执行顺序，数字越小，优先越高
	 * 业务自定义的filter，order请设置在300以上，[0-300]为框架内用
	 * @return
	 */
	int getOrder();
}
