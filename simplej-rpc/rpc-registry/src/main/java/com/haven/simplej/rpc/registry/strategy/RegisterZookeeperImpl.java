package com.haven.simplej.rpc.registry.strategy;

import com.haven.simplej.rpc.model.*;

import java.util.List;

/**
 * 通过zk实现注册中心
 * @author: havenzhang
 * @date: 2018/5/4 17:04
 * @version 1.0
 */
public class RegisterZookeeperImpl implements RegisterStrategy {
	@Override
	public boolean register(List<ServiceInfo> serviceList) {
		return false;
	}

	@Override
	public boolean unRegister(List<ServiceInfo> serviceList) {
		return false;
	}

	@Override
	public ServiceListInfo getService(String domain,boolean waitForChange) {
		return null;
	}

	@Override
	public boolean shutdown(ServiceInstance instance) {
		return false;
	}

	@Override
	public boolean heartbeat(List<ServiceInfo> serviceList) {
		return false;
	}

	@Override
	public boolean heartbeat(ServiceInstance instance) {
		return false;
	}

	@Override
	public boolean registerUri(List<UrlInfo> uriList) {
		return false;
	}

	@Override
	public boolean unRegisterUri(List<UrlInfo> uriList) {
		return false;
	}

	@Override
	public UrlListInfo getUrlList(String namespace) {
		return null;
	}
}
