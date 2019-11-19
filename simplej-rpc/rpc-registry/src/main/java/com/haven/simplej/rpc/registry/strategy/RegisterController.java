package com.haven.simplej.rpc.registry.strategy;

import com.haven.simplej.rpc.model.*;

import java.util.List;

/**
 * 策略管理器
 * @author: havenzhang
 * @date: 2018/5/4 17:10
 * @version 1.0
 */
public class RegisterController implements RegisterStrategy {


	/**
	 * 服务信息注册策略（使用mysql还是zookeeper，还是别的)
	 */
	private RegisterStrategy strategy;

	public RegisterController(RegisterStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public boolean register(List<ServiceInfo> serviceList) {
		return strategy.register(serviceList);
	}

	@Override
	public boolean unRegister(List<ServiceInfo> serviceList) {
		return strategy.unRegister(serviceList);
	}

	@Override
	public ServiceListInfo getService(String domain,boolean waitForChange) {
		return strategy.getService(domain,waitForChange);
	}

	@Override
	public boolean shutdown(ServiceInstance instance) {
		return strategy.shutdown(instance);
	}

	@Override
	public boolean heartbeat(List<ServiceInfo> serviceList) {
		return strategy.heartbeat(serviceList);
	}

	@Override
	public boolean heartbeat(ServiceInstance instance) {
		return strategy.heartbeat(instance);
	}

	@Override
	public boolean registerUri(List<UrlInfo> uriList) {
		return strategy.registerUri(uriList);
	}

	@Override
	public boolean unRegisterUri(List<UrlInfo> uriList) {
		return strategy.unRegisterUri(uriList);
	}

	@Override
	public UrlListInfo getUrlList(String namespace) {
		return strategy.getUrlList(namespace);
	}
}
