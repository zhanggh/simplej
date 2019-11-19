package com.haven.simplej.rpc.proxy.config;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.auth.plugin.config.AuthPluginConfiguration;
import com.haven.simplej.rpc.condition.IsRpcProxyServer;
import com.haven.simplej.rpc.config.plugin.config.ConfigPluginBeanConfiguration;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.mock.plugin.config.MockBeanConfiguration;
import com.haven.simplej.rpc.proxy.constant.RpcProxyConstant;
import com.haven.simplej.rpc.proxy.filter.NamespaceFilter;
import com.haven.simplej.rpc.proxy.filter.RequestMetricFilter;
import com.haven.simplej.rpc.proxy.helper.ProxyHelper;
import com.haven.simplej.rpc.proxy.processor.HttpProxyProcessorImpl;
import com.haven.simplej.rpc.proxy.processor.TcpProxyProcessorImpl;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.config.RpcConfiguration;
import com.haven.simplej.rpc.server.netty.RpcServerContext;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;


/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
@Configuration
@Slf4j
@Import({RpcConfiguration.class, ConfigPluginBeanConfiguration.class, AuthPluginConfiguration.class,
		MockBeanConfiguration.class})
public class BeanConfiguration implements InitializingBean {

	public BeanConfiguration() {
		log.info("init BeanConfiguration");
		String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		RpcHelper.addProxyCoderNamespace(namespace);
	}


	@Bean
	public HttpProxyProcessorImpl httpProxyProcessor() {
		return new HttpProxyProcessorImpl();
	}


	@Bean
	public TcpProxyProcessorImpl tcpProxyProcessor() {
		return new TcpProxyProcessorImpl();
	}

	/**
	 * 注册RPC服务容器
	 * filters: rpc远程调用的过滤器，会对每一个调用进行拦截过滤，如有需要可以自定义filter
	 */
	@Bean
	@Conditional(IsRpcProxyServer.class)
	public RpcServerContext serverContext(TcpProxyProcessorImpl proxyProcessor, List<RpcFilter> filters) {

		return new RpcServerContext(proxyProcessor, filters);
	}

	@Bean
	public RequestMetricFilter metricFilter() {
		return new RequestMetricFilter();
	}

	@Bean
	public NamespaceFilter namespaceFilter() {
		return new NamespaceFilter();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//启动之前初始化
		initCache();
	}

	/**
	 * 初始化基础数据
	 * @throws ClassNotFoundException
	 */
	private void initCache() throws ClassNotFoundException {

		//指定注册中心的服务
		ProxyHelper.addRegisterMethod(IServiceRegister.class);
		String classNames = PropertyManager.get(RpcProxyConstant.RPC_REGISTER_INTERFACE_CLASS_NAMES);

		String[] names = StringUtil.split(classNames, ";");
		if (names == null || names.length == 0) {
			return;
		}
		for (String name : names) {
			Class clz = Class.forName(name);
			ProxyHelper.addRegisterMethod(clz);
		}
	}
}


