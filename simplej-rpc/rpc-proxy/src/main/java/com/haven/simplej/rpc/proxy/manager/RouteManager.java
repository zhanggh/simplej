package com.haven.simplej.rpc.proxy.manager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.proxy.constant.RpcProxyConstant;
import com.haven.simplej.rpc.proxy.limiter.RpcLimiter;
import com.haven.simplej.rpc.proxy.route.RouteStrategy;
import com.haven.simplej.rpc.proxy.route.impl.RandomRouteStrategy;
import com.haven.simplej.rpc.route.InstanceSelect;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 路由管理器
 * @author: havenzhang
 * @date: 2018/5/9 21:46
 * @version 1.0
 */
@Slf4j
@Component
@Lazy(false)
public class RouteManager implements InstanceSelect, InitializingBean {

	@Autowired
	private List<RouteStrategy> strategies;


	/**
	 * 策略关系
	 */
	private static Map<String, RouteStrategy> strategyMap = Maps.newConcurrentMap();

	/**
	 * 注册中心实例
	 */
	private static Set<ServiceInstance> registryInstanceSet = Sets.newConcurrentHashSet();


	public static RouteStrategy get(String key) {
		return strategyMap.get(StringUtil.upperCase(key));
	}

	public ServiceInstance getInstance(List<ServiceInstance> instances, RpcHeader header) {
		//路由选择
		ServiceInstance instance;

		if (StringUtil.isNotEmpty(header.getServerIp()) && StringUtil.isNotEmpty(header.getServerPort())) {
			//如果指定了目标服务ip和地址
			instance = new ServiceInstance();
			instance.setNamespace(header.getNamespace());
			instance.setHost(header.getServerIp());
			instance.setPort(Integer.parseInt(header.getServerPort()));
			instance.setIdc(header.getClientIdc());
			instance.setRegionId(header.getClientRegionId());
			instance.setWeight(100.00f);
			instance.setProxyPort(0);
		} else {
			String strategyKey = PropertyManager.get(RpcProxyConstant.RPC_PROXY_ROUTE_STRATEGY_KEY,
					RandomRouteStrategy.class.getSimpleName());
			instance = RouteManager.get(strategyKey).select(instances, header);
		}

		return instance;
	}

	/**
	 * 根据服务信息，获取实例
	 * @param serviceInfo
	 * @return
	 */
	public ServiceInstance getInstance(ServiceInfo serviceInfo, RpcHeader header) {
		//获取服务的实例
		List<ServiceInstance> instances = serviceInfo.getInstances();

		return getInstance(instances, header);
	}

	public static void addStrategy(String key, RouteStrategy strategy) {
		strategyMap.put(StringUtil.upperCase(key), strategy);
	}

	/**
	 * 增加注册中心实例
	 * @param instance
	 */
	public static void addRegistryInstance(ServiceInstance instance) {
		registryInstanceSet.add(instance);
	}

	/**
	 * 增加注册中心实例
	 * @param instances
	 */
	public static void addRegistryInstance(List<ServiceInstance> instances) {
		instances.forEach(registryInstanceSet::add);
	}


	/**
	 * 获取注册中心实例列表
	 * @return
	 */
	public static Set<ServiceInstance> getRegistryInstances() {

		return registryInstanceSet;
	}

	/**
	 * 注册中心实例
	 */
	private static List<ServiceInstance> registryInstances = new ArrayList<>();

	static {
		getRegisterInstances();
	}

	/**
	 * 返回注册中心实例
	 * @return List
	 */
	public static List<ServiceInstance> getRegisterInstances() {

		if (CollectionUtil.isNotEmpty(registryInstances)) {
			return registryInstances;
		}

		//如果同步集合没有，那么就从本机配置文件指定的注册中心地址选取
		String serversString = PropertyManager.get(RpcConstants.RPC_REGISTER_SERVERS_KEY);
		String[] servers = StringUtil.split(serversString, ";");
		for (String server : servers) {
			String[] hostPort = StringUtil.split(server, ":");
			ServiceInstance instance1 = new ServiceInstance(hostPort[0], Integer.parseInt(hostPort[1]));
			registryInstances.add(instance1);
		}
		return registryInstances;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (CollectionUtil.isNotEmpty(strategies)) {
			strategies.forEach(e -> addStrategy(e.getClass().getName(), e));
		}
	}
}
