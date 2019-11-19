package com.haven.simplej.rpc.proxy.route.impl;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.proxy.route.BaseRouteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机策略
 * @author: havenzhang
 * @date: 2018/5/9 21:55
 * @version 1.0
 */
@Component
@Lazy(false)
@Slf4j
@Scope("prototype")
public class RandomRouteStrategy extends BaseRouteStrategy {
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
		List<ServiceInstance> instances = filter(instanceList,header);
		//随机获取实例
		int next = ThreadLocalRandom.current().nextInt(instances.size());
		return instanceList.get(next);
	}

}
