package com.haven.simplej.rpc.auth.plugin.filter;

import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 权限控制过滤器
 * @author: havenzhang
 * @date: 2018/10/13 18:52
 * @version 1.0
 */
@Component
@Slf4j
public class AuthFilter implements RpcFilter {

	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {

		log.debug("--------------------AuthFilter----------------------");
		chain.doFilter(request,response);
	}

	@Override
	public int getOrder() {
		return FilterOrder.AUTH_CHECK.order();
	}
}

