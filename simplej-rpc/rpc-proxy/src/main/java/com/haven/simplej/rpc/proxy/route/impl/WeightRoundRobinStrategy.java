package com.haven.simplej.rpc.proxy.route.impl;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.proxy.route.BaseRouteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 加权轮询策略，高并发的情况下，不是绝对公平
 * @author: havenzhang
 * @date: 2018/8/28 11:30
 * @version 1.0
 */
@Component
@Lazy(false)
@Slf4j
@Scope("prototype")
public class WeightRoundRobinStrategy extends BaseRouteStrategy {


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
		double totalWeight = 0;
		double maxWeight = 0;

		for (int i = 0; i < instances.size(); i++) {
			ServiceInstance n = instances.get(i);
			totalWeight += n.getWeight();
			// 每个节点的当前权重要加上原始的权重
			n.setCurrentWeight(n.getCurrentWeight() + n.getWeight());
			// 保存当前权重最大的节点
			if (instance == null || maxWeight < n.getCurrentWeight()) {
				instance = n;
				maxWeight = n.getCurrentWeight();
			}
		}
		// 被选中的节点权重减掉总权重
		instance.setCurrentWeight(instance.getCurrentWeight() - totalWeight);
		return instance;
	}
}
