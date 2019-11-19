package com.haven.simplej.proxytest;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.server.heathcheck.IHeathCheck;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.HeathResponse;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceListInfo;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/4/28 21:42
 * @version 1.0
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = StartUpApplication.class)
public class RpcServiceTest {

	/**
	 * 注册服务接口
	 */
	private static IServiceRegister serviceRegister;

	static {
		//静态路由
		//		System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
		//		System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "8086");
		System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY, "127.0.0.1");
		System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY, "8181");
	}

	@Before
	public void before() {
		serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
	}

	@Test
	public void test() {
		IHeathCheck service = ServiceProxy.create().setInterfaceClass(IHeathCheck.class).build();
		HeathResponse resp = service.healthCheck();
		System.out.println(JSON.toJSONString(resp, true));
	}

	@Test
	public void syncServiceTest() {
		//		System.setProperty(RpcConstants.RPC_REGISTER_SERVERS_KEY,"127.0.0.1:9092");
		//		ServiceManager.getInstance().syncServiceList();
		ServiceListInfo serviceListInfo = serviceRegister.getService(null, false);

		System.out.println(JSON.toJSONString(serviceListInfo, true));
	}

	@Test
	public void registerTest() {
		IServiceRegister register = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();

		ServiceInstance instance = new ServiceInstance();
		instance.setNamespace("test.com");
		instance.setHost("192.168.1.108");
		instance.setPort(443);
		instance.setIdc("default");
		System.out.println(register.heartbeat(instance));
	}


	@Test
	public void registerTest2(){

		String json = "[{\"createTime\":1571549442237,\"instances\":[{\"currentWeight\":0.0,\"host\":\"192.168.3"
				+ ".144\",\"httpPort\":8081,\"idc\":\"default\",\"local\":false,"
				+ "\"md5\":\"5cdbe6d988e7a0c535de118f77471534\",\"namespace\":\"demo2.web.xxx.com\",\"port\":8899,"
				+ "\"proxyHttpPort\":-1,\"proxyPort\":8181,\"regionId\":\"default\",\"weight\":0.0}],"
				+ "\"md5\":\"85fba0c93c7bc51923886935923b67ff\","
				+ "\"methods\":[{\"methodId\":\"e2cc2864b880cd544909be681c38cdf5\",\"methodName\":\"healthCheck\","
				+ "\"returnType\":\"com.haven.simplej.rpc.model.HeathResponse\",\"serviceName\":\"com.haven.simplej"
				+ ".rpc.center.service.IHeathCheck\",\"timeout\":60000,\"version\":\"1.0\"}],\"namespace\":\"demo2.web"
				+ ".xxx.com\",\"serverType\":\"BUSINESS_SERVER\",\"serviceName\":\"com.haven.simplej.rpc.center"
				+ ".service.IHeathCheck\",\"timeout\":1000,\"updateTime\":1571549442237,\"version\":\"1.0\"},"
				+ "{\"createTime\":1571549442261,\"instances\":[{\"currentWeight\":0.0,\"host\":\"192.168.3.144\","
				+ "\"httpPort\":8081,\"idc\":\"default\",\"local\":false,\"md5\":\"5cdbe6d988e7a0c535de118f77471534\","
				+ "\"namespace\":\"demo2.web.xxx.com\",\"port\":8899,\"proxyHttpPort\":-1,\"proxyPort\":8181,"
				+ "\"regionId\":\"default\",\"weight\":0.0}],\"md5\":\"3d37d40ec7a36793ba660724a8e93ca3\","
				+ "\"methods\":[{\"methodId\":\"c0fa57faa09163a906b37655e88e098d\",\"methodName\":\"getUserInfo\","
				+ "\"paramsTypes\":\"java.lang.String,com.haven.simplej.demo.rpc.model.UserinfoRpcModel\","
				+ "\"returnType\":\"java.lang.String\",\"serviceName\":\"com.haven.simplej.demo.rpc.service"
				+ ".UserinfoRpcService\",\"timeout\":2000,\"version\":\"1.0\"},"
				+ "{\"methodId\":\"d4d3036e9994a61c6ad48babf1adc1cf\",\"methodName\":\"search\",\"paramsTypes\":\"com"
				+ ".haven.simplej.demo.rpc.model.UserinfoRpcModel\",\"returnType\":\"com.haven.simplej.response.model"
				+ ".JsonResponse\",\"serviceName\":\"com.haven.simplej.demo.rpc.service.UserinfoRpcService\","
				+ "\"timeout\":200,\"version\":\"1.0\"},{\"methodId\":\"5e39d4d5f55253897647f7cb73d89963\","
				+ "\"methodName\":\"update\",\"paramsTypes\":\"com.haven.simplej.demo.rpc.model.UserinfoRpcModel\","
				+ "\"returnType\":\"int\",\"serviceName\":\"com.haven.simplej.demo.rpc.service.UserinfoRpcService\","
				+ "\"timeout\":100,\"version\":\"1.0\"},{\"methodId\":\"a00740e116da626c9f9cc87035be3b9a\","
				+ "\"methodName\":\"healthCheck\",\"returnType\":\"int\",\"serviceName\":\"com.haven.simplej.demo.rpc"
				+ ".service.UserinfoRpcService\",\"timeout\":60000,\"version\":\"1.0\"},"
				+ "{\"methodId\":\"5508e4ce9ccc70b7930d878799f0a365\",\"methodName\":\"get\",\"paramsTypes\":\"com"
				+ ".haven.simplej.demo.rpc.model.UserinfoRpcModel\",\"returnType\":\"com.haven.simplej.demo.rpc.model"
				+ ".UserinfoRpcModel\",\"serviceName\":\"com.haven.simplej.demo.rpc.service.UserinfoRpcService\","
				+ "\"timeout\":3000,\"version\":\"1.0\"},{\"methodId\":\"bbb460b1653da9a253f35b7461709a6b\","
				+ "\"methodName\":\"add\",\"paramsTypes\":\"com.haven.simplej.demo.rpc.model.UserinfoRpcModel\","
				+ "\"returnType\":\"int\",\"serviceName\":\"com.haven.simplej.demo.rpc.service.UserinfoRpcService\","
				+ "\"timeout\":100,\"version\":\"1.0\"},{\"methodId\":\"7b4a441f456f91ca4ff752075326512f\","
				+ "\"methodName\":\"delete\",\"paramsTypes\":\"com.haven.simplej.demo.rpc.model.UserinfoRpcModel\","
				+ "\"returnType\":\"int\",\"serviceName\":\"com.haven.simplej.demo.rpc.service.UserinfoRpcService\","
				+ "\"timeout\":100,\"version\":\"1.0\"},{\"methodId\":\"7d323a065fd51278a0ddf0dc3d6fbbab\","
				+ "\"methodName\":\"query\",\"paramsTypes\":\"com.haven.simplej.demo.rpc.model.UserinfoRpcModel\","
				+ "\"returnType\":\"java.util.List\",\"serviceName\":\"com.haven.simplej.demo.rpc.service"
				+ ".UserinfoRpcService\",\"timeout\":200,\"version\":\"1.0\"}],\"namespace\":\"demo2.web.xxx.com\","
				+ "\"serverType\":\"BUSINESS_SERVER\",\"serviceName\":\"com.haven.simplej.demo.rpc.service"
				+ ".UserinfoRpcService\",\"timeout\":60000,\"updateTime\":1571549442261,\"version\":\"1.0\"}]";

		List<ServiceInfo> serviceList = JSON.parseArray(json,ServiceInfo.class);
		boolean result = serviceRegister.register(serviceList);

		System.out.println(result);
	}
}
