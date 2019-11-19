package com.haven.simplej.rpc.registry.service;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.model.*;

import java.util.List;

/**
 * 服务发布 接口
 * @author haven.zhang
 * @date 2018/3/3.
 */
@RpcService(timeout = 1000)
@Doc(value = "服务注册&监听，下线等功能",author = "havenzhang",date = "20180303")
public interface IServiceRegister  extends BaseRpcService {

	/**
	 * 发布服务元信息以及实例信息
	 * @param serviceList
	 * @return
	 */
	@RpcMethod(timeout = 1500)
	boolean register(List<ServiceInfo> serviceList);

	/**
	 * 发布服务元信息以及实例信息
	 * @param uriList uri 列表
	 * @return
	 */
	@RpcMethod(timeout = 1500)
	boolean registerUri(List<UrlInfo> uriList);
	/**
	 * 取消服务注册
	 * @param uriList
	 * @return
	 */
	@RpcMethod(timeout = 1500)
	boolean unRegisterUri(List<UrlInfo> uriList);


	/**
	 * 取消服务注册
	 * @param serviceList
	 * @return
	 */
	@RpcMethod(timeout = 1500)
	boolean unRegister(List<ServiceInfo> serviceList);



	/**
	 * 查询所有注册的服务信息
	 * @param namespace 命名空间，可以是域名 ，比如一个微服务作为一个独立的域，如果为空，则查询全部域
	 * @return
	 */
	@RpcMethod(timeout = 15000)
	ServiceListInfo getService(String namespace,boolean waitForChange);

	/**
	 * 查询所有注册的服务信息
	 * @param namespace 命名空间，可以是域名 ，比如一个微服务作为一个独立的域，如果为空，则查询全部域
	 * @return
	 */
	@RpcMethod(timeout = 15000)
	UrlListInfo getUrlList(String namespace);

	/**
	 * 实例下线
	 * @param instance
	 * @return
	 */
	@RpcMethod(timeout = 500)
	boolean shutdown(ServiceInstance instance);

	/**
	 * 假如 rpc服务类实现了com.haven.simplej.rpc.heartbeat.IServiceHeartbeat时，框架向rpc服务中心发送心跳
	 * @param serviceList
	 * @return
	 */
	@RpcMethod(timeout = 5000)
	boolean heartbeat(List<ServiceInfo> serviceList);

	@RpcMethod(timeout = 1500)
	boolean urlHeartbeat(List<UrlInfo> serviceList);

	/**
	 * rpc框架实现对实例的定时心跳上报
	 * @param instance
	 * @return
	 */
	@RpcMethod(timeout = 5500)
	boolean heartbeat(ServiceInstance instance);

}
