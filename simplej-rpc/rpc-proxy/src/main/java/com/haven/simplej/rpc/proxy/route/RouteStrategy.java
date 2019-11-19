package com.haven.simplej.rpc.proxy.route;

import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInstance;

import java.util.List;

/**
 * 路由策略
 * @author: havenzhang
 * @date: 2019/1/9 21:19
 * @version 1.0
 */
public interface RouteStrategy {

	/**
	 * 路由选择
	 * @param instanceList
	 * @return
	 */
	ServiceInstance select(List<ServiceInstance> instanceList, RpcHeader header);

}
