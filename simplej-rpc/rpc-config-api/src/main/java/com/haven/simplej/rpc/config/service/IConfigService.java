package com.haven.simplej.rpc.config.service;

import com.haven.simplej.rpc.annotation.*;
import com.haven.simplej.rpc.config.model.ConfigFileRpcModel;
import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigItemRpcModel;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;

/**
 * 配置管理接口
 * @author: havenzhang
 * @date: 2018/06/11 23:19
 * @version 1.0
 */
@RpcService(timeout = 30000)
@Doc(value = "配置管理接口", author = "havenzhang", date = "2018-02-05")
public interface IConfigService extends BaseRpcService {

	/**
	 * 查询最新配置文件，如果有的话返回
	 * @param namespace 指定命名空间
	 * @return ConfigFileResponse
	 */
	@RpcMethod(timeout = 15000)
	@Doc(value = "查询最新的配置文件", author = "havenzhang")
	ConfigFileResponse queryNewConfigFile(@RpcParam(required = true) @Doc("命名空间（域名）") String namespace,
			@RpcParam(required = true) @Doc("是否等待属性发生变更才返回") boolean waitForChange);


	/**
	 * 查询最新的配置属性项
	 * @param namespace 指定命名空间
	 * @return ConfigItemResponse
	 */
	@RpcMethod(timeout = 15000)
	@Doc(value = "查询最新的配置项", author = "havenzhang")
	ConfigItemResponse queryNewConfigItem(@RpcParam(required = true) @Doc("命名空间（域名）") String namespace,
			@RpcParam(required = true) @Doc("是否等待属性发生变更才返回") boolean waitForChange);


	@RpcMethod(timeout = 500)
	@Doc(value = "新增配置文件", author = "havenzhang")
	int addConfigFile(@RpcStruct ConfigFileRpcModel configFile);

	@RpcMethod(timeout = 500)
	@Doc(value = "修改配置文件", author = "havenzhang")
	int updateConfigFile(@RpcStruct ConfigFileRpcModel configFile);


	@RpcMethod(timeout = 500)
	@Doc(value = "新增属性项", author = "havenzhang")
	int addConfigItem(@RpcStruct ConfigItemRpcModel configItem);

	@RpcMethod(timeout = 500)
	@Doc(value = "修改属性项", author = "havenzhang")
	int updateConfigItem(@RpcStruct ConfigItemRpcModel configItem);


	@RpcMethod(timeout = 500)
	@Doc(value = "单独获取某个属性信息", author = "havenzhang")
	ConfigItemRpcModel getConfigItem(@RpcParam(required = true, maxLength = 100) String namespace,
			@RpcParam(required = true, maxLength = 100) String key);


	@RpcMethod(timeout = 500)
	@Doc(value = "单独获取某个属性文件", author = "havenzhang")
	ConfigItemRpcModel getConfigFile(@RpcParam(required = true, maxLength = 100) String namespace,
			@RpcParam(required = true, maxLength = 100) String fileName);

}
