package com.haven.simplej.rpc.registry.service;


import com.haven.simplej.db.base.BaseService;
import com.haven.simplej.rpc.registry.model.ServiceInstanceRefModel;

/**
 * 服务与实例的关联关系 Service
 */
public interface ServiceInstanceRefService extends BaseService<ServiceInstanceRefModel> {

	/**
	 * 增加服务与实例关系
	 * @param model 关系model
	 * @return boolean
	 */
	public boolean add(ServiceInstanceRefModel model);
}