package com.haven.simplej.rpc.registry;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.MethodInfo;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceListInfo;
import com.vip.vjtools.vjkit.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @author: havenzhang
 * @date: 2019/5/4 20:04
 * @version 1.0
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = StartUpApplication.class)
public class ServiceRegisterTest {

	static {
		System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
		System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "9092");
		//		System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY,"127.0.0.1");
		//		System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY,"9091");
		System.setProperty(RpcConstants.RPC_SERVER_PORT_KEY, "9092");
		System.setProperty(RpcConstants.RPC_SERVICE_BASE_PACKAGE_KEY, "com.haven.simplej.rpc.server");
	}

	@Test
	public void serviceRegisterTest() {
		IServiceRegister serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();

		List<ServiceInfo> serviceList = Lists.newArrayList();
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setVersion(RpcConstants.VERSION_1);
		serviceInfo.setServiceName(IServiceRegister.class.getName());
		serviceInfo.setTimeout(500);
		serviceInfo.setNamespace("com.haven.simplej.rpc.server");
		Set<MethodInfo> methods = Sets.newHashSet();
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setReturnType(String.class.getName());
		methodInfo.setMethodName("register");
		methodInfo.setVersion(RpcConstants.VERSION_1);
		methods.add(methodInfo);

		methodInfo.setParamsTypes(RpcHelper.getParamTypesStr(new Class[]{String.class}));
		serviceInfo.setMethods(methods);

		List<ServiceInstance> instances = Lists.newArrayList();
		ServiceInstance instance = new ServiceInstance();
		instance.setIdc("default");
		instance.setHost(NetUtil.getLocalHost());
		instance.setPort(443);
		instances.add(instance);
		serviceInfo.setInstances(instances);

		serviceList.add(serviceInfo);
		boolean resp = serviceRegister.register(serviceList);

		System.out.println("register result : " + resp);
	}

	@Test
	public void hearbeat() {
		IServiceRegister serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();

		ServiceInstance instance = new ServiceInstance();
		instance.setPort(443);
		instance.setHost(NetUtil.getLocalHost());
		instance.setIdc("default");

		boolean result = serviceRegister.heartbeat(instance);

		System.out.println("heartbeat result:" + result);
	}


	@Test
	public void getServiceTest() {
		IServiceRegister serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
		ServiceListInfo serviceInfos = serviceRegister.getService(null,true);

		System.out.println(JSON.toJSONString(serviceInfos, true));
	}
}
