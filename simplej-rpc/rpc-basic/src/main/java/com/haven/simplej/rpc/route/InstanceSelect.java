package com.haven.simplej.rpc.route;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/11/3 10:24
 * @version 1.0
 * 实例负载均衡器
 */
public interface InstanceSelect {

	/**
	 * 根据当前负载均衡策略器，选择合适的实例
	 * @param instances 实例集合
	 * @param header 请求头
	 * @return  ServiceInstance
	 */
	ServiceInstance getInstance(List<ServiceInstance> instances, RpcHeader header);
}
