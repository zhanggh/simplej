package com.haven.simplej.rpc.registry.strategy;

import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.model.*;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/5/4 17:04
 * @version 1.0
 */
public interface RegisterStrategy{
	/**
	 * 发布服务元信息以及实例信息
	 * @param serviceList
	 * @return
	 */
	@RpcMethod(timeout = 500)
	boolean register(List<ServiceInfo> serviceList);


	/**
	 * 取消服务注册
	 * @param serviceList
	 * @return
	 */
	@RpcMethod(timeout = 500)
	boolean unRegister(List<ServiceInfo> serviceList);



	/**
	 * 查询所有注册的服务信息
	 * @param domain 域 ，比如一个微服务作为一个独立的域，如果为空，则查询全部域
	 * @return
	 */
	@RpcMethod(timeout = 500)
	ServiceListInfo getService(String domain,boolean waitForChange);


	/**
	 * 实例下线
	 * @param instance
	 * @return
	 */
	boolean shutdown(ServiceInstance instance);

	/**
	 * 假如 rpc服务类实现了com.haven.simplej.rpc.heartbeat.IServiceHeartbeat时，框架向rpc服务中心发送心跳
	 * @param serviceList
	 * @return
	 */
	boolean heartbeat(List<ServiceInfo> serviceList);

	/**
	 * rpc框架实现对实例的定时心跳上报
	 * @param instance
	 * @return
	 */
	boolean heartbeat(ServiceInstance instance);

	/**
	 * 发布服务元信息以及实例信息
	 * @param uriList uri 列表
	 * @return
	 */
	@RpcMethod(timeout = 500)
	boolean registerUri(List<UrlInfo> uriList);
	/**
	 * 取消服务注册
	 * @param uriList
	 * @return
	 */
	@RpcMethod(timeout = 500)
	boolean unRegisterUri(List<UrlInfo> uriList);


	/**
	 * 查询所有注册的服务信息
	 * @param namespace 命名空间，可以是域名 ，比如一个微服务作为一个独立的域，如果为空，则查询全部域
	 * @return
	 */
	@RpcMethod(timeout = 15000)
	UrlListInfo getUrlList(String namespace);
}
