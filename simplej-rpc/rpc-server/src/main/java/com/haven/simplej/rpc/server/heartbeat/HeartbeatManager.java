package com.haven.simplej.rpc.server.heartbeat;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.HeathResponse;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceStatus;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.server.enums.ServerState;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.http.WebUrlManager;
import com.haven.simplej.rpc.server.loader.ServiceLoader;
import com.haven.simplej.rpc.server.netty.RpcServerContext;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.time.DateUtils;
import com.haven.simplej.time.enums.DateStyle;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.net.NetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * 上报心跳，app --> proxy -->注册中心
 * app应用服务在启动之后会执行该方法
 * @author: havenzhang
 * @date: 2018/5/5 22:30
 * @version 1.0
 */
@Slf4j
public class HeartbeatManager {

	/**
	 * 启动心跳
	 */
	public static void heartbeatStart() {
		boolean enableHeartbeat = PropertyManager.getBoolean(RpcConstants.RPC_APP_SERVER_ENABLE_HEARTBEAT, true);
		if (!enableHeartbeat) {
			log.debug("enableHeartbeat:false,do not report heartbeat");
			return;
		}
		if (RpcHelper.isRpcRegister()) {
			log.debug("registry server do not report heartbeat");
			return;
		}
		//异步上报健康度心跳,以固定的延时进行
		ThreadPoolFactory.getHeartbeatExecutor().execute(() -> {
			long start = System.currentTimeMillis();
			do {
				try {
					//long poll，长连接
					reportHeartbeat();
					if (System.currentTimeMillis() - start < 1000) {
						//防止过于频繁的访问
						long timeout = PropertyManager.getLong(RpcConstants.RPC_HEARBEAT_WAIT_TIME_GAP, 10000);
						Thread.sleep(timeout);
					}
				} catch (Exception e) {
					log.error("reportHeartbeat error", e);
				}
			} while (true);
		});
	}

	/**
	 * 上报心跳，app --> proxy -->注册中心
	 * 仅上报当前实例状态，不包含service状态
	 */
	public static void reportHeartbeat() {
		//当前app服务的监听端口
		try {

			if (RpcHelper.isRpcProxy()) {
				//向rpc 治理中心上报心跳,特殊地址
				RpcHelper.selectRpcRegisterServer();
			} else if (!ServiceInfoHelper.hasProxy()) {
				log.warn("local proxy server can not be found,do not report instance status to registry");
				return;
			}

			//非proxy服务的时候，心跳向proxy服务请求，由proxy转发到注册中心
			IServiceRegister service =
					ServiceProxy.create().setSyncRequest(true).setInterfaceClass(IServiceRegister.class).build();
			instanceHeartbeat(service);
		} catch (Exception e) {
			log.error("app server instance status heartbeat error", e);
		} finally {
			InvocationContext.removeServerInfo();
		}
	}

	/**
	 * 实例心跳上报
	 * @param service 注册服务对象
	 */
	private static void instanceHeartbeat(IServiceRegister service) {
		int proxyPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_PORT_KEY, -1);
		int proxyHttpPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_HTTP_PORT_KEY, -1);
		int httpPort = PropertyManager.getInt(RpcConstants.WEB_SERVER_PORT_KEY, -1);
		ServiceInstance instance = new ServiceInstance();
		instance.setHost(NetUtil.getLocalHost());
		instance.setProxyPort(proxyPort);
		instance.setProxyHttpPort(proxyHttpPort);
		instance.setHttpPort(httpPort);
		instance.setPort(PropertyManager.getInt(RpcConstants.RPC_SERVER_PORT_KEY, -1));
		instance.setIdc(PropertyManager.get(RpcConstants.RPC_APP_IDC, RpcConstants.RPC_APP_IDC_DEFAULT));
		instance.setRegionId(PropertyManager.get(RpcConstants.RPC_APP_REGION_ID, RpcConstants.RPC_APP_REGION_DEFAULT));
		instance.setNamespace(PropertyManager.get(RpcConstants.RPC_APP_NAME));
		service.heartbeat(instance);
	}

	/**
	 * 静态服务心跳上报
	 * 实际上只能让服务中心知道该服务的存在，而不知道该服务是否正常
	 * @param service 服务列表
	 */
	@Deprecated
	private static void serviceHearbeat(IServiceRegister service) {
		List<ServiceInfo> serviceList = ServiceLoader.getInstance().getServiceList();
		log.debug("json:{}", JSON.toJSONString(serviceList));
		service.heartbeat(serviceList);
		service.urlHeartbeat(WebUrlManager.getInstance().getUrlInfoList());
	}


	/**
	 * @TODO 调用每个service的heathcheck 改成并行执行
	 * 默认的服务健康状态,调用方：rpc-center 服务治理中心
	 * rpc-center--》proxy--》 proxy--》 rpc-server（app）
	 * @return HeathResponse
	 */
	public static HeathResponse healthCheck() {
		log.debug("..............healthCheck listening........... ");
		HeathResponse response = new HeathResponse();
		ServiceInstance instance = new ServiceInstance();
		instance.setNamespace(PropertyManager.get(RpcConstants.RPC_APP_NAME));
		response.setRespCode(RpcError.SUCCESS.getErrorCode());
		response.setStartUpTime(DateUtils.dateToString(RpcServerContext.getStartUpTime(),
				DateStyle.YYYY_MM_DD_HH_MM_SS));
		instance.setHost(NetUtil.getLocalHost());
		instance.setPort(PropertyManager.getInt(RpcConstants.RPC_SERVER_PORT_KEY));

		int proxyPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_PORT_KEY, -1);
		int proxyHttpPort = PropertyManager.getInt(RpcConstants.RPC_PROXY_HTTP_PORT_KEY, -1);
		int httpPort = PropertyManager.getInt(RpcConstants.WEB_SERVER_PORT_KEY, -1);
		instance.setProxyPort(proxyPort);
		instance.setProxyHttpPort(proxyHttpPort);
		instance.setHttpPort(httpPort);

		String idc = PropertyManager.get(RpcConstants.RPC_APP_IDC, RpcConstants.RPC_APP_IDC_DEFAULT);
		String regionId = PropertyManager.get(RpcConstants.RPC_APP_REGION_ID, RpcConstants.RPC_APP_REGION_DEFAULT);
		instance.setIdc(idc);
		instance.setRegionId(regionId);
		response.setInstance(instance);

		//检查每个服务service的状态
		List<ServiceStatus> statusList = Lists.newArrayList();
		//任务列表
		List<Future<ServiceStatus>> futureList = Lists.newArrayList();
		ServiceInfoHelper.getHeathMethodMap().forEach(((method, aClass) -> {
			Future<ServiceStatus> future = ThreadPoolFactory.getHeartbeatExecutor().submit(() -> {
				ServiceStatus serviceStatus = checkService(aClass, method);
				return serviceStatus;
			});
			futureList.add(future);


		}));
		//获取任务结果
		futureList.forEach(future -> {
			try {
				ServiceStatus status = future.get(2, TimeUnit.SECONDS);
				if (status != null) {
					status.setNamespace(instance.getNamespace());
					statusList.add(status);
				}
			} catch (Exception e) {
				log.error("futureList get error", e);
			}
		});
		response.setStatusList(statusList);
		return response;
	}

	/**
	 * 检查某个service服务的状态
	 * @param aClass service服务类
	 * @param method 健康度检查方法
	 * @return ServiceStatus
	 */
	public static ServiceStatus checkService(Class aClass, Method method) {
		Object service = SpringContext.getBean(aClass);
		if (service == null) {
			return null;
		}
		RpcService rpc = (RpcService) aClass.getAnnotation(RpcService.class);
		ServiceStatus serviceStatus = new ServiceStatus();
		serviceStatus.setServiceName(aClass.getName());
		if (rpc != null) {
			serviceStatus.setVersion(rpc.version());
		}
		int result;
		try {
			Class<?>[] paramTypes = method.getParameterTypes();
			if (paramTypes == null || paramTypes.length == 0) {
				result = ReflectUtil.invokeMethod(service, method);
			} else {
				List param = Lists.newArrayList();
				for (Class<?> paramType : paramTypes) {
					param.add(null);
				}
				result = ReflectUtil.invokeMethod(service, method, param.toArray());
			}
		} catch (Exception e) {
			log.warn("heathCheck error", e);
			result = ServerState.FAULT.state();
			serviceStatus.setErrorMsg(ExceptionUtil.stackTraceText(e));
		}
		if (result == ServerState.STARTUP_SUCC.state()) {
			serviceStatus.setErrorMsg(ServerState.STARTUP_SUCC.name());
		}
		serviceStatus.setStatus(result);
		return serviceStatus;
	}
}
