package com.haven.simplej.rpc.registry.service.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.registry.strategy.RegisterController;
import com.haven.simplej.rpc.registry.strategy.RegisterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务信息注册远程服务类
 * @author: havenzhang
 * @date: 2018/5/4 0:02
 * @version 1.0
 */
@Slf4j
@Service
public class ServiceRegisterImpl implements IServiceRegister, InitializingBean {

	private RegisterController controller;

	@Autowired
	private RegisterStrategy strategy;

	/**
	 * 心跳监视器
	 */
	private Object hearbeartMonitor = new Object();

	@Override
	public boolean register(List<ServiceInfo> serviceList) {
		log.debug("register service list:{}", JSON.toJSONString(serviceList, true));
		return controller.register(serviceList);
	}

	@Override
	public boolean registerUri(List<UrlInfo> uriList) {
		return controller.registerUri(uriList);
	}

	@Override
	public boolean unRegisterUri(List<UrlInfo> uriList) {
		return controller.unRegisterUri(uriList);
	}

	@Override
	public boolean unRegister(List<ServiceInfo> serviceList) {
		log.debug("unRegister service:{}", JSON.toJSONString(serviceList, true));
		return controller.unRegister(serviceList);
	}

	@Override
	public ServiceListInfo getService(String domain, boolean waitForChange) {
		log.debug("getService domain:{}", domain);
		return controller.getService(domain, waitForChange);
	}

	@Override
	public UrlListInfo getUrlList(String namespace) {

		return controller.getUrlList(namespace);
	}

	@Override
	public boolean shutdown(ServiceInstance instance) {
		return controller.shutdown(instance);
	}

	@Override
	@Deprecated
	public boolean heartbeat(List<ServiceInfo> serviceList) {
		log.debug("---------------------heartbeat---------------------");
		return controller.heartbeat(serviceList);
	}

	@Override
	public boolean urlHeartbeat(List<UrlInfo> serviceList) {
		//@TODO
		log.debug("---------------------urlHeartbeat---------------------");
		return true;
	}

	@Override
	public boolean heartbeat(ServiceInstance instance) {
		controller.heartbeat(instance);
		synchronized (hearbeartMonitor) {
			try {
				//等到自然醒
				long timeout = PropertyManager.getLong(RpcConstants.RPC_HEARBEAT_WAIT_TIME_GAP, 10000);
				hearbeartMonitor.wait(timeout);
			} catch (InterruptedException e) {
				log.error("hearbeartMonitor InterruptedException", e);
				//@可以考虑主动发起健康检查
			}
		}
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		controller = new RegisterController(strategy);
	}
}
