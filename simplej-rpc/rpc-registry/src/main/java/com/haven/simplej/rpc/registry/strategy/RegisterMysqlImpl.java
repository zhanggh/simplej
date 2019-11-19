package com.haven.simplej.rpc.registry.strategy;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.LoadingCache;
import com.haven.simplej.cache.CacheManager;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.rpc.registry.domain.definition.InstanceInfoDefinition;
import com.haven.simplej.rpc.registry.enums.InstanceStatus;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;
import com.haven.simplej.rpc.registry.service.InstanceInfoService;
import com.haven.simplej.rpc.registry.service.ServiceInfoService;
import com.haven.simplej.rpc.registry.service.UrlInfoService;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 通过mysql实现注册中心
 * @author: havenzhang
 * @date: 2018/5/4 17:04
 * @version 1.0
 */
@Slf4j
@Component
@Data
public class RegisterMysqlImpl implements RegisterStrategy {

	@Autowired
	private ServiceInfoService service;

	@Autowired
	private InstanceInfoService instanceInfoService;

	@Autowired
	private UrlInfoService urlInfoService;

	private Object serviceMonitor = new Object();
	private Object urlMonitor = new Object();

	private LoadingCache<String, Object> cache;

	public RegisterMysqlImpl() {
		cache = CacheManager.createCache(100, 5000);
	}

	@Override
	public boolean register(List<ServiceInfo> serviceList) {
		for (ServiceInfo serviceInfo : serviceList) {
			service.register(serviceInfo);
		}
		return true;
	}

	@Override
	public boolean unRegister(List<ServiceInfo> serviceList) {
		for (ServiceInfo serviceInfo : serviceList) {
			service.unRegister(serviceInfo);
		}
		return true;
	}

	@Override
	public ServiceListInfo getService(String domain, boolean waitForChange) {
		long serviceMonitorWaitTime =
				PropertyManager.getLong(RpcConstants.RPC_REGISTER_SERVER_SERVICE_MONITOR_WAIT_TIME, 5000);
		if (StringUtil.isEmpty(domain) && waitForChange) {
			//没有指定域名的查询，是来自proxy的服务列表监听请求
			synchronized (serviceMonitor) {
				try {
					log.info("getService waiting.......");
					serviceMonitor.wait(serviceMonitorWaitTime);
				} catch (InterruptedException e) {
					log.warn("serviceMonitor interrupted", e);
				}
			}
		}

		String key = "getService:" + domain;
		ServiceListInfo serviceListInfo;
		try {
			serviceListInfo = (ServiceListInfo) cache.get(key);
			if (serviceListInfo != null) {
				return serviceListInfo;
			}
		} catch (Exception e) {
			log.debug("no cache key:{}", key);
		}
		serviceListInfo = service.getService(domain);
		cache.put(key, serviceListInfo);
		return serviceListInfo;
	}

	@Override
	public boolean shutdown(ServiceInstance instance) {
		InstanceInfoModel updateModel = new InstanceInfoModel();
		updateModel.setHost(instance.getHost());
		updateModel.setPort(instance.getPort());
		updateModel.setIdc(instance.getIdc());
		updateModel.setRegionId(instance.getRegionId());
		updateModel.setProxyPort(instance.getProxyPort());
		updateModel.setHttpPort(instance.getHttpPort());
		updateModel.setStatus((byte) InstanceStatus.invalid.getStatus());
		updateModel.setUpdateTime(DateUtils.getTimestamp(new Date()));
		instanceInfoService.update(updateModel, InstanceInfoDefinition.host.name(), InstanceInfoDefinition.port.name()
				, InstanceInfoDefinition.httpPort.name(), InstanceInfoDefinition.idc.name());
		return true;
	}

	@Override
	public boolean heartbeat(List<ServiceInfo> serviceList) {
		log.debug("heartbeat serviceInfo:{}", JSON.toJSONString(serviceList, true));
		ThreadPoolFactory.getServerExecutor().execute(() -> serviceList.forEach(e -> service.heartbeat(e)));
		return true;
	}

	@Override
	public boolean heartbeat(ServiceInstance instance) {
		log.debug("heartbeat instance:{}", instance);
		instanceInfoService.heartbeat(instance);
		return true;
	}

	@Override
	public boolean registerUri(List<UrlInfo> uriList) {
		if (CollectionUtil.isNotEmpty(uriList)) {
			for (UrlInfo urlInfo : uriList) {
				urlInfoService.regitster(urlInfo);
			}
		}

		return true;
	}

	@Override
	public boolean unRegisterUri(List<UrlInfo> uriList) {
		if (CollectionUtil.isNotEmpty(uriList)) {
			for (UrlInfo urlInfo : uriList) {
				urlInfoService.unRegitster(urlInfo);
			}
		}

		return false;
	}

	@Override
	public UrlListInfo getUrlList(String namespace) {
		long serviceMonitorWaitTime =
				PropertyManager.getLong(RpcConstants.RPC_REGISTER_SERVER_SERVICE_MONITOR_WAIT_TIME, 5000);
		if (StringUtil.isEmpty(namespace)) {
			synchronized (urlMonitor) {
				try {
					log.info("getUrlList waiting.......");
					urlMonitor.wait(serviceMonitorWaitTime);
				} catch (InterruptedException e) {
					log.warn("urlMonitor interrupted", e);
				}
			}
		}

		String key = "getUrlList:" + namespace;
		UrlListInfo urlListInfo = null;
		try {
			urlListInfo = (UrlListInfo) cache.get(key);
			if (urlListInfo != null) {
				return urlListInfo;
			}
		} catch (Exception e) {
			log.debug("no cache key:{}", key);
		}
		urlListInfo = urlInfoService.getUrlList(namespace);
		cache.put(key, urlListInfo);
		return urlListInfo;
	}
}
