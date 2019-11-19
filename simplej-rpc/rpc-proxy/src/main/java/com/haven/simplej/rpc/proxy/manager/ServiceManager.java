package com.haven.simplej.rpc.proxy.manager;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.proxy.constant.RpcProxyConstant;
import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.route.InstanceSelect;
import com.haven.simplej.rpc.server.http.WebUrlManager;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 服务管理器
 * @author haven.zhang
 * @date 2018/5/3.
 */
@Slf4j
@Component
public class ServiceManager {


	/**
	 *  服务列表信息,包含所有注册在rpc-server上的服务信息
	 */
	private static final Map<ServiceLookupKey, ServiceInfo> serviceMap = Maps.newConcurrentMap();

	/**
	 * url服务映射关系
	 */
	private static final Map<String, Map<UrlLookupKey, UrlInfo>> urlMap = Maps.newConcurrentMap();

	/**
	 * 注册服务接口
	 */
	private IServiceRegister serviceRegister;


	/**
	 * 服务列表信息md5
	 */
	private String serviceInfoMd5;
	/**
	 * 服务列表更新状态，0：初始状态，1：服务列表同步更新中,2-正常状态
	 */
	private volatile int syncState = 0;


	/**
	 * 缓存状态监视器
	 */
	private Object serviceCacheMonitor = new Object();

	@Autowired
	private InstanceSelect instanceSelect;

	/**
	 * 判断服务是否存在
	 * @param key
	 * @return
	 */
	public boolean serviceExisit(ServiceLookupKey key) {
		return serviceMap.containsKey(key);
	}

	/**
	 * 单例
	 * @return ServiceManager
	 */
	public ServiceManager() {
		serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
	}


	/**
	 * 向rpc-center同步获取远程服务列表信息
	 */
	public List<ServiceInfo> syncServiceList() {

		log.debug("sync service list");
		if (!RpcHelper.selectRpcRegisterServer()) {
			log.error("rpc registry can not connect");
			throw new RpcException(RpcError.SYSTEM_BUSY, "rpc registry can not connect");
		}
		//获取所有存活的服务列表信息
		ServiceListInfo serviceListInfo = serviceRegister.getService(null, syncState != 0);
		if (StringUtil.equalsIgnoreCase(serviceInfoMd5, serviceListInfo.getMd5())) {
			//md5相同，说明信息没发生变化
			return null;
		}
		serviceInfoMd5 = serviceListInfo.getMd5();
		List<ServiceInfo> serviceList = serviceListInfo.getServiceList();
		if (CollectionUtil.isEmpty(serviceList)) {
			log.debug("remote service list is empty");
			syncState = 2;
			return null;
		}
		//缓存服务列表和实例信息
		cacheServiceList(serviceList);
		return serviceList;
	}

	/**
	 * 解析并缓存服务和实例信息
	 * @param serviceList 服务列表
	 */
	public void cacheServiceList(List<ServiceInfo> serviceList) {
		try {
			boolean logServiceList = PropertyManager.getBoolean(RpcConstants.RPC_SYNC_SERVICE_LIST_LOG_ENABLE, false);
			if (logServiceList) {
				//日志太大了，排查问题的时候才打开
				log.debug("service list:{}", JSON.toJSONString(serviceList, true));
			}
			//设置当前服务缓存状态为更新中状态
			syncState = 1;
			//先清除缓存，然后再重新添加数据
			clearServiceInfoCache();
			CountDownLatch downLatch = new CountDownLatch(serviceList.size());
			serviceList.parallelStream().forEach(e -> {
				downLatch.countDown();
				try {
					ServiceLookupKey lookupKey = new ServiceLookupKey();
					lookupKey.setServiceName(e.getServiceName());
					lookupKey.setVersion(e.getVersion());
					serviceMap.put(lookupKey, e);
					//缓存实例信息
					RpcHelper.addInstance(lookupKey, e.getInstances());
					e.getMethods().parallelStream().forEach(m -> RpcHelper.addInstance(m.getMethodId(), e.getVersion()
							, e.getInstances()));

					lookupKey = new ServiceLookupKey();
					lookupKey.setServiceName(e.getServiceName());
					lookupKey.setVersion(e.getVersion());
					lookupKey.setNamespace(e.getNamespace());
					serviceMap.put(lookupKey, e);
					RpcHelper.addInstance(lookupKey, e.getInstances());
					if (e.getServiceName().equalsIgnoreCase(IServiceRegister.class.getName())) {
						//注册中心服务
						RouteManager.addRegistryInstance(e.getInstances());
					}
				} catch (Exception e1) {
					log.error("cacheServiceList error", e1);
				}
			});

			int timeout = PropertyManager.getInt(RpcProxyConstant.RPC_REGISTER_SERVICE_LIST_SYN_UPDATE_TIMEOUT, 5);
			downLatch.await(timeout, TimeUnit.SECONDS);
			//设置服务信息缓存可用状态
			syncState = 2;
			availableNotify();
			log.debug("service list sync update finish");
		} catch (InterruptedException e) {
			log.error("cacheServiceList error", e);
			throw new RpcException(RpcError.SYSTEM_BUSY,e);
		}
	}

	/**
	 * 缓存可用通知
	 */
	private void availableNotify() {
		synchronized (serviceCacheMonitor) {
			serviceCacheMonitor.notifyAll();
		}
	}

	/**
	 * 清除本地服务信息缓存
	 */
	private void clearServiceInfoCache() {
		serviceMap.clear();
		RpcHelper.clearInstanceCache();
	}

	/**
	 * 判断当前缓存是否可用
	 * @return boolean
	 */
	private boolean isNormal() {
		return syncState == 2;
	}

	/**
	 * 等待缓存可用通知
	 */
	private void waitNotify() {
		synchronized (serviceCacheMonitor) {
			try {
				serviceCacheMonitor.wait(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取服务信息，
	 * @param lookupKey
	 */
	public ServiceInfo getService(ServiceLookupKey lookupKey) {

		if (!isNormal()) {
			waitNotify();
		}
		return serviceMap.get(lookupKey);
	}

	private UrlInfo getUrlInfo(HttpRequest httpReq) {
		Map<String, String> header = httpReq.getHttpHeader();
		String namespace = header.get(HttpField.namespace);

		UrlLookupKey lookupKey = new UrlLookupKey();
		lookupKey.setHttpMethod(httpReq.getHttpMethod()); //可选条件
		lookupKey.setNamespace(namespace);//必要条件
		lookupKey.setVersion(header.get(HttpField.serviceVersion));//必要条件
		lookupKey.setUri(httpReq.getHttpUrl());//必要条件
		UrlInfo urlInfo = WebUrlManager.getInstance().getUrlInfo(lookupKey);

		if (urlInfo == null) {
			lookupKey.setHttpMethod(UrlLookupKey.ANY);
			urlInfo = WebUrlManager.getInstance().getUrlInfo(lookupKey);
		}
		if (urlInfo == null) {
			lookupKey.setHttpMethod(UrlLookupKey.ANY);
			Iterator<Map.Entry<String, String>> iterator = header.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				lookupKey.setHeader(entry.getKey() + "=" + entry.getValue());
				urlInfo = WebUrlManager.getInstance().getUrlInfo(lookupKey);
				if (urlInfo != null) {
					break;
				}
			}
		}
		return urlInfo;
	}

	/**
	 * 获取目标业务app的url地址
	 * @return url
	 */
	public String getTargetUrl(HttpRequest httpReq) {
		UrlInfo urlInfo = getUrlInfo(httpReq);
		if (urlInfo == null) {
			throw new RpcException(RpcError.INVAILD_REQUEST, "uri can not be found");
		}
		List<ServiceInstance> instances = urlInfo.getInstances();
		ServiceInstance instance = instanceSelect.getInstance(instances, InvocationContext.getHeader());
		int port = instance.getProxyHttpPort();
		boolean proxyToPorxy = PropertyManager.getBoolean(RpcConstants.RPC_PROXY_TO_PROXY_KEY, false);
		if (instance.isLocal() || !proxyToPorxy) {
			port = instance.getHttpPort();
		}
		StringBuilder url = new StringBuilder();
		url.append(urlInfo.getSchema()).append("://");
		url.append(instance.getHost()).append(":").append(port);
		url.append(urlInfo.getUri());
		log.debug("getTargetUrl:{}", url.toString());
		return url.toString();
	}
}
