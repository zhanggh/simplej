package com.haven.simplej.rpc.mock.plugin;

import com.google.common.collect.Maps;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.mock.model.MockInfo;
import com.haven.simplej.rpc.mock.model.MockMethod;
import com.haven.simplej.rpc.mock.plugin.constant.MockConstant;
import com.haven.simplej.rpc.mock.service.MockService;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceLookupKey;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.route.InstanceSelect;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: havenzhang
 * @date: 2018/10/31 23:42
 * @version 1.0
 */
@Slf4j
@Component
public class MockPlugin implements RpcPlugin {

	private MockService mockService;
	/**
	 * mock 信息的md5用于判断本地的信息与服务端的新是否一致，
	 * 不一致则代表信息有更新
	 */
	private volatile String mockInfoMd5;

	/**
	 * key = methodId+serviceVersion
	 * methodId与对应的methodInfo映射关系
	 */
	private static final Map<String, MockMethod> methodIdMethodInfoMap = Maps.newConcurrentMap();

	/**
	 * key = serverName+methodName+serviceVersion+returnTypes
	 * methodId与对应的methodInfo映射关系
	 */
	private static final Map<String, MockMethod> serviceNameMethodNameMethodInfoMap = Maps.newConcurrentMap();

	@Autowired
	private InstanceSelect instanceSelect;


	@Override
	public void init(List<ServiceInfo> serviceInfos, Map<String, List<ServiceInstance>> methodIdInstanceMap) {
		mockService = ServiceProxy.create().setInterfaceClass(MockService.class).build();
		boolean mockEnable = PropertyManager.getBoolean(MockConstant.MOCK_DATA_LISTEN_ENABLE, true);
		if (!mockEnable) {
			log.debug("mockEnable is false, No need to query service mock information regularly ");
			return;
		}
		long delay = PropertyManager.getLong(MockConstant.MOCK_DATA_QUERY_TIME_GAP, 10);
		//同步桩信息
		ThreadPoolFactory.getScheduleExecutor().scheduleWithFixedDelay(() -> syncMockList(), 0, delay,
				TimeUnit.SECONDS);
	}

	@Override
	public void listenByRegister(String namespace) {
		log.debug("listenByRegister ,namespace:{}", namespace);
	}


	/**
	 * 获取方法信息
	 * @param methodId 方法id
	 * @param serviceVersion 版本号
	 * @return MethodInfo
	 */
	public MockMethod getMethodInfo(String methodId, String serviceVersion) {
		return methodIdMethodInfoMap.get(RpcHelper.getLookupKey(methodId, serviceVersion));
	}


	/**
	 * 获取方法信息
	 * @param serviceVersion 版本号
	 * @return MethodInfo
	 */
	public MockMethod getMethodInfo(String serviceName, String methodName, String serviceVersion, String paramTypes) {
		return methodIdMethodInfoMap.get(RpcHelper.getLookupKey(serviceName, methodName, serviceVersion, paramTypes));
	}


	/**
	 * 从治理中心同步远程方法挡板信息
	 */
	public void syncMockList() {
		log.debug("sync mock method list");
		RpcService rpcService = MockService.class.getAnnotation(RpcService.class);
		ServiceLookupKey lookupKey = new ServiceLookupKey();
		lookupKey.setServiceName(MockService.class.getName());
		if (StringUtil.isNotEmpty(rpcService.serviceName())) {
			lookupKey.setServiceName(rpcService.serviceName());
		}
		lookupKey.setVersion(rpcService.version());
		String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		lookupKey.setNamespace(namespace);

		//获取当前service的所有实例信息
		List<ServiceInstance> instances = RpcHelper.getServiceInstance(lookupKey);
		if (CollectionUtil.isEmpty(instances)) {
			log.warn("instances is empty ,lookupKey:{}", lookupKey);
			return;
		}

		//选择合适的目标实例
		ServiceInstance instance = instanceSelect.getInstance(instances, new RpcHeader());
		InvocationContext.setServer(instance.getHost(), instance.getPort());
		MockInfo mockInfo = mockService.getMockInfo();
		if (StringUtil.equalsIgnoreCase(mockInfoMd5, mockInfo.getMd5()) || CollectionUtil.isEmpty(mockInfo.getMockMethodList())) {
			//md5相同，说明信息没发生变化
			return;
		}

		mockInfoMd5 = mockInfo.getMd5();
		//先删除本地mock信息缓存
		clearMockCache();

		//更新服务mock信息缓存
		mockInfo.getMockMethodList().forEach(method -> {
			methodIdMethodInfoMap.put(RpcHelper.getLookupKey(method.getMethodId(), method.getVersion()), method);
			String key = RpcHelper.getLookupKey(method.getServiceName(), method.getMethodName(), method.getVersion(),
					method.getParamsTypes());
			serviceNameMethodNameMethodInfoMap.put(key, method);
		});

	}


	/**
	 * 先删除本地mock信息缓存
	 */
	private void clearMockCache() {
		methodIdMethodInfoMap.clear();
		serviceNameMethodNameMethodInfoMap.clear();
	}
}
