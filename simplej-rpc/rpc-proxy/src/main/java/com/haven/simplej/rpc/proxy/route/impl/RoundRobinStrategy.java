package com.haven.simplej.rpc.proxy.route.impl;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.proxy.route.BaseRouteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 并发的情况下，不一定那么准确
 * 一般轮询策略,每个请求都需要产生一个对象
 * @author: havenzhang
 * @date: 2018/8/28 11:30
 * @version 1.0
 */
@Component
@Lazy(false)
@Slf4j
@Scope("prototype")
public class RoundRobinStrategy extends BaseRouteStrategy {

	/**
	 * 轮询的当前位置
	 */
	private int currentIndex;
	/**
	 * 总实例数
	 */
	private int totalServer;


	@Override
	public ServiceInstance select(List<ServiceInstance> instanceList, RpcHeader header) {
		//不管是什么路由策略，如果存在本机服务，则优先路由到本机
		ServiceInstance instance = getLocalInstance(instanceList);
		if (instance != null) {
			log.debug("select local host instance");
			instance.setLocal(true);
			return instance;
		}

		/**
		 * 分组,假如客户端有传idc 或者regionid ，那么优先从这些指定的idc或者regionid里面选取实例
		 */
		List<ServiceInstance> instances = filter(instanceList, header);
		totalServer = instances.size();
		currentIndex = (currentIndex + 1) % totalServer;
		return instances.get(currentIndex);
	}
}
