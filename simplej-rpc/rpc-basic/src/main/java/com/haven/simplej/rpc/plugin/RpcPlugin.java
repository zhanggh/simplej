package com.haven.simplej.rpc.plugin;

import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * 插件接口
 * 所有实现了该接口的类，都会被proxy服务在启动时进行初始化
 * 注意：实现类必须交给spring进行实例化以及bean管理
 * @author: havenzhang
 * @date: 2018/10/31 22:23
 * @version 1.0
 */
public interface RpcPlugin {

	/**
	 * proxy服务启动的时候进行初始化
	 * @param serviceInfos 服务列表
	 * @param methodIdInstanceMap 服务实例
	 */
	void init(List<ServiceInfo> serviceInfos, Map<String, List<ServiceInstance>> methodIdInstanceMap);

	/**
	 * 监听服务注册请求，在客户端应用首次发起服务注册的时候，触发
	 * @param namespace 客户端应用命名空间
	 */
	void listenByRegister(String namespace);
}
