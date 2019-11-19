package com.haven.simplej.rpc.registry;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceListInfo;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: havenzhang
 * @date: 2019/5/14 22:31
 * @version 1.0
 */
public class RegisterTest {

	static IServiceRegister service;

	ExecutorService executer = Executors.newFixedThreadPool(50);


	@Before
	public void before() {
		System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
		System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "8383");
		service = ServiceProxy.create().setSyncRequest(false).setInterfaceClass(IServiceRegister.class).build();
	}

	@Test
	public void test0() throws InterruptedException {

		int count = 10099;
		CountDownLatch downLatch = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			executer.execute(()->{
				downLatch.countDown();
				ServiceListInfo serviceListInfo = service.getService(null, false);
				System.out.println(JSON.toJSONString(serviceListInfo));
				InvocationContext.removeTraceId();
			});

		}
		downLatch.await();
		executer.shutdown();
		System.out.println("-----------------------finish--------------------------------");
	}

	@Test
	public void test1() throws InterruptedException {

		//		System.out.println(JSON.toJSONString(service.getService(null, true), true));
		System.out.println("---------------------------------------------------");
		String json = "{\"currentWeight\":0.0,\"host\":\"192.168.3.144\",\"httpPort\":9191,\"idc\":\"default\"," +
				"\"local\":false,\"md5\":\"180457527529de2d17cc4d457644f5f0\",\"namespace\":\"www.proxy.rpc.simplej" + ".com\",\"port\":8181,\"proxyHttpPort\":-1,\"proxyPort\":-1,\"regionId\":\"default\",\"weight\":0.0}";

		ServiceInstance instance = JSON.parseObject(json, ServiceInstance.class);
		//		service.heartbeat(instance);
		for (int i = 0; i < 199; i++) {
			boolean result = service.heartbeat(instance);
			System.out.println(result);
			System.out.println("------------------------------------------------------------------------------i:" + i);
		}

		System.out.println("------------------------------------------------------------------------------");
		Thread.sleep(2000000);
	}


	@Test
	public void test2() throws InterruptedException {
		String json = "[{\"createTime\":1570415449212,\"instances\":[{\"currentWeight\":0.0,\"host\":\"192.168.3" +
				".144\",\"httpPort\":9191,\"idc\":\"default\",\"local\":false," + "\"md5" +
				"\":\"321ca3df602d92d880f05f1b407ade63\",\"port\":8181,\"proxyHttpPort\":-1,\"proxyPort\":-1," +
				"\"weight\":0.0}],\"md5\":\"92f29204537093e436c7625b4e37702e\"," + "\"methods\":[{\"methodId" +
				"\":\"e2cc2864b880cd544909be681c38cdf5\",\"methodName\":\"healthCheck\"," + "\"returnType\":\"com" +
				".haven.simplej.rpc.model.HeathResponse\",\"timeout\":60000,\"version\":\"1" + ".0\"}]," +
				"\"namespace\":\"www.proxy.rpc.simplej.com\",\"serverType\":\"proxy\",\"serviceName\":\"com" + ".haven"
				+ ".simplej.rpc.center.service.IHeathCheck\",\"timeout\":1000,\"version\":\"1.0\"}," + "{\"createTime" + "\":1570415449520,\"instances\":[{\"currentWeight\":0.0,\"host\":\"192.168.3.144\"," + "\"httpPort" + "\":9191,\"idc\":\"default\",\"local\":false,\"md5\":\"321ca3df602d92d880f05f1b407ade63\"," + "\"port\":8181,\"proxyHttpPort\":-1,\"proxyPort\":-1,\"weight\":0.0}]," + "\"md5" + "\":\"af3e1eb65d16fefbfa85b1db81343cbd\"," + "\"methods\":[{\"methodId" + "\":\"b7c70414fc916f5a121ad4ed5723ee26\",\"methodName\":\"register\"," + "\"paramsTypes\":\"java" + ".lang.String,java.lang.String,int\",\"returnType\":\"int\",\"timeout\":60000," + "\"version\":\"1" + ".0\"},{\"methodId\":\"6895ced349dbb3249c90ce25f6ceeda4\"," + "\"methodName\":\"getNextLongSeqNo\"," + "\"paramsTypes\":\"java.lang.String,java.lang.String\"," + "\"returnType\":\"java.lang.String\"," + "\"timeout\":60000,\"version\":\"1.0\"}," + "{\"methodId\":\"8a5a3b4400f04c6e62b47bc52f1ebaee\"," + "\"methodName\":\"getNextShortSeqNo\"," + "\"paramsTypes\":\"java.lang.String,java.lang.String," + "int\",\"returnType\":\"long\"," + "\"timeout\":60000,\"version\":\"1.0\"}],\"namespace\":\"www" + ".proxy.rpc.simplej.com\"," + "\"serverType\":\"proxy\",\"serviceName\":\"com.haven.simplej.rpc" + ".center.service.ISequenceService\"," + "\"timeout\":100,\"version\":\"1.0\"}]";


		List<ServiceInfo> serviceList = JSON.parseArray(json, ServiceInfo.class);

		boolean result = service.heartbeat(serviceList);
		System.out.println(result);
		Thread.sleep(2000);
	}
}
