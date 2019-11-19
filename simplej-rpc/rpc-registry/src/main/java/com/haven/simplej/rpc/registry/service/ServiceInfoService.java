package com.haven.simplej.rpc.registry.service;

import com.haven.simplej.db.base.BaseService;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceListInfo;
import com.haven.simplej.rpc.registry.model.ServiceInfoModel;

/**
 * rpc服务信息 Service
 */
public interface ServiceInfoService extends BaseService<ServiceInfoModel> {

	/**
	 * 服务注册
	 * @param service 服务信息
	 * @return boolean
	 */
	boolean register(ServiceInfo service);

	/**
	 * 撤销服务注册信息
	 * @param service 服务信息
	 * @return boolean
	 */
	boolean unRegister(ServiceInfo service);

	/**
	 * 查询服务列表
	 * @param namespace
	 * @return
	 */
	ServiceListInfo getService(String namespace);

	/**
	 * 服务心跳上报
	 * @param serviceInfo
	 * @return
	 */
	boolean heartbeat(ServiceInfo serviceInfo);

}