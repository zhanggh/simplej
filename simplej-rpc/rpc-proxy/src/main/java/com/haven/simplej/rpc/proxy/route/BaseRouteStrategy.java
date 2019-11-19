package com.haven.simplej.rpc.proxy.route;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.net.NetUtil;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.proxy.manager.RouteManager;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * 优先获取本地服务
 * @author: havenzhang
 * @date: 2018/6/10 22:34
 * @version 1.0
 */
@Slf4j
public abstract class BaseRouteStrategy implements InitializingBean, RouteStrategy {

	/**
	 * 如果本地存在某个service的实例，则路由到本地
	 * @param instanceList 实例列表
	 * @return ServiceInstance
	 */
	protected ServiceInstance getLocalInstance(List<ServiceInstance> instanceList) {
		if (CollectionUtil.isEmpty(instanceList)) {
			throw new RpcException(RpcError.SERVICE_INSTANCE_NOT_FOUND, "route select error,instance list is empty");
		}
		List<ServiceInstance> tempList = Lists.newArrayList();
		boolean result = instanceList.parallelStream().anyMatch(e -> {
			if (NetUtil.getLocalHost().equals(e.getHost()) || StringUtil.equals(e.getHost(), RpcConstants.LOCAHOST)) {
				tempList.add(e);
				return true;
			}
			return false;
		});
		if (result && CollectionUtil.isNotEmpty(tempList)) {
			ServiceInstance instance = new ServiceInstance();
			instance.setIdc(tempList.get(0).getIdc());
			instance.setPort(tempList.get(0).getPort());
			instance.setHttpPort(tempList.get(0).getHttpPort());
			instance.setProxyHttpPort(tempList.get(0).getProxyHttpPort());
			instance.setNamespace(tempList.get(0).getNamespace());
			instance.setHost(RpcConstants.LOCAHOST);
			instance.setRegionId(tempList.get(0).getRegionId());
			return instance;
		}

		return null;
	}

	/**
	 * 按机房和集合进行分组
	 * @return Map
	 */
	protected Map<String, List<ServiceInstance>> groupByIdcAndRegion(List<ServiceInstance> instances) {
		Map<String, List<ServiceInstance>> instanceMap = Maps.newHashMap();
		instances.forEach(instance -> {
			List<ServiceInstance> list = instanceMap.get(instance.getIdc());
			if (CollectionUtil.isEmpty(list)) {
				list = Lists.newArrayList();
				list.add(instance);
				instanceMap.put(instance.getIdc(), list);
			}
		});

		return instanceMap;
	}

	/**
	 * 过滤同机房或者同集合的实例
	 * @param instanceList 所有实例集合
	 * @param header 请求头
	 * @return List
	 */
	protected List<ServiceInstance> filter(List<ServiceInstance> instanceList, RpcHeader header) {
		/**
		 * 分组,假如客户端有传idc 或者regionid ，那么优先从这些指定的idc或者regionid里面选取实例
		 */
		Map<String, List<ServiceInstance>> instanceMap = groupByIdcAndRegion(instanceList);
		List<ServiceInstance> instances = null;
		if (StringUtil.isNotEmpty(header.getClientRegionId())) {
			instances = instanceMap.get(header.getClientRegionId());
		}
		if (CollectionUtil.isEmpty(instances) && StringUtil.isNotEmpty(header.getClientIdc())) {
			instances = instanceMap.get(header.getClientIdc());
		}
		if (CollectionUtil.isEmpty(instances)) {
			//兜底的服务实例列表
			instances = instanceList;
		}
		return instances;
	}

	@Override
	public void afterPropertiesSet() {
		RouteManager.addStrategy(this.getClass().getSimpleName(), this);
	}
}
