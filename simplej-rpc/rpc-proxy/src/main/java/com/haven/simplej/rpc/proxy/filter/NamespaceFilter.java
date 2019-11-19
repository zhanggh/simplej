package com.haven.simplej.rpc.proxy.filter;

import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import com.haven.simplej.rpc.proxy.helper.ProxyHelper;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 命名空间过滤器，用于收集所有经过proxy服务发起服务注册的命名空间信息
 * @author: havenzhang
 * @date: 2018/06/30 23:45
 * @version 1.0
 */
public class NamespaceFilter implements RpcFilter {

	@Autowired
	private List<RpcPlugin> plugins;

	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {
		if (ProxyHelper.isRegisterMethod(request.getHeader().getMethodId())) {
			//如果当前请求是服务列表注册，那么就收集当前客户端应用的namespace
			ProxyHelper.addAppNamespace(request.getHeader().getClientNamespace());
			plugins.parallelStream().forEach(rpcPlugin ->
					ThreadPoolFactory.getServerExecutor().execute(() ->
							rpcPlugin.listenByRegister(request.getHeader().getClientNamespace())));
		}
		chain.doFilter(request, response);
	}

	@Override
	public int getOrder() {
		return FilterOrder.NAMESPACE_FILTER.order();
	}
}
