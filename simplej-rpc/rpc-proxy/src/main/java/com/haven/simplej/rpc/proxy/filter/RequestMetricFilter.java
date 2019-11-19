package com.haven.simplej.rpc.proxy.filter;

import com.google.common.collect.Sets;
import com.haven.simplej.rpc.auth.service.AuthService;
import com.haven.simplej.rpc.server.heathcheck.IHeathCheck;
import com.haven.simplej.rpc.center.service.MetricService;
import com.haven.simplej.rpc.mock.service.MockService;
import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 流量上报过滤器,只有在proxy服务才需要该filter
 * @author: havenzhang
 * @date: 2018/9/28 16:43
 * @version 1.0
 */
@Slf4j
public class RequestMetricFilter implements RpcFilter {

	/**
	 * 排除上报的远程服务
	 */
	private static Set<String> exculdeService = Sets.newHashSet();
	static {
		exculdeService.add(IConfigService.class.getName());
		exculdeService.add(MetricService.class.getName());
		exculdeService.add(IServiceRegister.class.getName());
		exculdeService.add(IHeathCheck.class.getName());
		exculdeService.add(MockService.class.getName());
		exculdeService.add(AuthService.class.getName());
	}

	@Autowired
	private MetricService metricService;

	public RequestMetricFilter() {
	}

	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {
		long start = System.currentTimeMillis();
		log.debug("----------------------into metric filter----------------------");
		request.getHeader().setProxyDispatchTime(System.currentTimeMillis());
		chain.doFilter(request, response);
		long cost = System.currentTimeMillis() - start;
		response.getHeader().setDelayed(cost);
		response.getHeader().setProxyResponseTime(System.currentTimeMillis());

		//排除registry的服务的接口服务
		if (exculdeService.contains(request.getHeader().getServiceName())) {
			return;
		}
		//服务线程池来执行该任务
		ThreadPoolFactory.getServerExecutor().execute(() -> {
			/**
			 * 异步上报流量
			 */
			log.debug("reportRpcCall rpc request and response to rpc center");
			metricService.reportRpcCall(request, response);
		});
	}

	@Override
	public int getOrder() {
		return FilterOrder.METRIC_FILTER.order();
	}
}
