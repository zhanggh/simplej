package com.haven.simplej.rpc.filter.impl;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志过滤器，输入请求日志，和响应日志
 * @author: havenzhang
 * @date: 2019/1/26 20:34
 * @version 1.0
 */
@Slf4j
public class RpcLogFilter implements RpcFilter {
	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {
		long start = System.currentTimeMillis();
		log.debug("request msg:{}", JSON.toJSONString(request));
		chain.doFilter(request, response);
		long cost = System.currentTimeMillis() - start;
		response.getHeader().setDelayed(cost);
		response.getHeader().setProxyResponseTime(System.currentTimeMillis());
		log.debug("response msg:{}", JSON.toJSONString(response));
	}

	@Override
	public int getOrder() {
		return FilterOrder.LOG_FILTER.order();
	}
}
