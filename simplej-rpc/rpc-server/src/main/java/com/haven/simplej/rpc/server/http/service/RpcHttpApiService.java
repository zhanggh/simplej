package com.haven.simplej.rpc.server.http.service;

import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.model.ParamMeta;
import com.haven.simplej.rpc.model.ServiceMeta;

import java.lang.reflect.Method;
import java.util.List;

/**
 * rpc 服务自助测试工具服务业务处理类
 * @author: havenzhang
 * @date: 2019/1/10 17:33
 * @version 1.0
 */
public interface RpcHttpApiService {

	/**
	 * 查询本地所有rpc service服务类的信息
	 * @return List
	 */
	List<ServiceMeta> query();

	/**
	 * 查询服务的方法信息
	 * @param serviceName 服务名
	 * @return List
	 */
	List<MethodMeta> queryMethods(String serviceName);

	/**
	 * 查询服务的方法信息
	 * @param methodId 服务名
	 * @return List
	 */
	Method getMethod(String methodId);


	/**
	 * 查询方法的参数信息
	 * @param methodId 方法id
	 * @return list
	 */
	List<ParamMeta> queryParams(String methodId);
}
