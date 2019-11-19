package com.haven.simplej.rpc.server.config;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.center.service.MetricService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.condition.*;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.filter.impl.RpcLogFilter;
import com.haven.simplej.rpc.filter.impl.RpcParamValidateFilter;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.heathcheck.HeathCheckServiceImpl;
import com.haven.simplej.rpc.server.listener.ShutdownListener;
import com.haven.simplej.rpc.server.netty.RpcServerContext;
import com.haven.simplej.rpc.server.signal.AppSignalHandler;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/05/10 11:54
 * @version 1.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.rpc.server", excludeFilters = @ComponentScan.Filter(type =
		FilterType.REGEX, pattern = "com\\.haven\\.simplej\\.rpc\\.server\\.config.*"))
@PropertySource(value = {"classpath:/rpc-server.properties"})
public class RpcConfiguration {


	@Autowired
	private List<RpcFilter> filters;


	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	public ShutdownEndpoint shutdownEndpoint() {
		return new ShutdownEndpoint();
	}

	@Bean
	public RpcLogFilter logFilter() {
		return new RpcLogFilter();
	}

	@Bean
	@Conditional(IsRpcAppServer.class)
	public RpcParamValidateFilter paramValidateFilter() {
		return new RpcParamValidateFilter();
	}

	@Bean("rpcSpringContext")
	@Order(1)
	@ConditionalOnMissingBean(SpringContext.class)
	public SpringContext springContext() {

		return new SpringContext();
	}

	/**
	 * 实例化属性管理器
	 * @return
	 */
	@Bean
	public PropertyManager propertyManager() {
		log.info("init propertyManager");

		return new PropertyManager();
	}



	@Bean
	@Lazy(false)
	@ConditionalOnMissingBean(MetricService.class)
	@Conditional(IsRpcProxyServer.class)
	public MetricService metricService() {
		log.debug("---------------------init metricService---------------------");
		MetricService metricService =
				ServiceProxy.create().setSyncRequest(false).setInterfaceClass(MetricService.class).build();
		SpringContext.registerBean("metricService", metricService);
		return metricService;
	}

	/**
	 * 暴露websocket服务
	 * @return ServerEndpointExporter
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		log.debug("---------------------init ServerEndpointExporter---------------------");
		return new ServerEndpointExporter();
	}

	@Bean
	@Conditional(IsNonRpcRegisterServer.class)
	public IServiceRegister serviceRegister(){

		return ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
	}

	@Bean
	@Conditional(IsNonRpcRegisterServer.class)
	public AppSignalHandler signalHandler(@Qualifier("serviceRegister")IServiceRegister iServiceRegister){
		return new AppSignalHandler(iServiceRegister);
	}

	@Bean
	@Conditional(IsRpcRegisterServer.class)
	public AppSignalHandler signalHandler2(){
		return new AppSignalHandler();
	}

	@Bean
	public ShutdownListener shutdownListener(AppSignalHandler signalHandler){

		return new ShutdownListener();
	}

	/**
	 * 注册RPC服务容器
	 * filters: rpc远程调用的过滤器，会对每一个调用进行拦截过滤，如有需要可以自定义filter
	 */
	@Bean
	@Conditional(IsNotRpcProxyServer.class)
	public RpcServerContext serverContext1(@Qualifier("rpcSpringContext") SpringContext context,
			List<RpcFilter> filters,AppSignalHandler signalHandler) {

		return new RpcServerContext(filters);
	}


	@Bean
	public HeathCheckServiceImpl heathCheckService(){
		return new HeathCheckServiceImpl();
	}
}
