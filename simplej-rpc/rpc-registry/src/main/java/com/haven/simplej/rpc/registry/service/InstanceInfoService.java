package com.haven.simplej.rpc.registry.service;

import com.haven.simplej.db.base.BaseService;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;

import java.util.List;

/**
 * rpc服务的实例信息 Service
 */
public interface InstanceInfoService extends BaseService<InstanceInfoModel> {

	/**
	 * 根据命名空间查询实例分组
	 * @param namespace
	 * @return
	 */
	List<InstanceInfoModel> getInstanceGroup(String namespace);



	/**
	 * 实例心跳上报
	 * @param instance
	 * @return
	 */
	boolean heartbeat(ServiceInstance instance);

	/**
	 * 根据serviceId查询实例
	 * @param serviceId
	 * @return
	 */
	List<ServiceInstance> getInstance(String serverType, long serviceId);

	/**
	 * 新增实例
	 * @param serviceInstance 实例
	 * @param namespace 命名空间
	 * @return InstanceInfoModel
	 */
	InstanceInfoModel addInstance(ServiceInstance serviceInstance, String namespace);
}