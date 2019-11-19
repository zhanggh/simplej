package com.haven.simplej.rpc.server;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.ServiceLookupKey;
import com.haven.simplej.rpc.server.loader.ServiceLoader;
import org.junit.Test;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/4/27 21:06
 * @version 1.0
 */


public class ServiceLoaderTest {

	@Test
	public void loaderTest() {

		System.setProperty(RpcConstants.RPC_SERVER_PORT_KEY,"9091");

		String path = "com.haven.epay.payment.service";
		ServiceLoader loader = ServiceLoader.getInstance();
		loader.addServiceBasePackage(path);
		List<Class> classes = loader.load(path);
		System.out.println(JSON.toJSONString(classes, true));

		System.out.println(JSON.toJSONString(loader.loadLocalService(), true));
	}


	@Test
	public void test2(){
		ServiceLookupKey lookupKey = new ServiceLookupKey();
		lookupKey.setServiceName("com.haven.simplej.rpc.service");
		lookupKey.setVersion("1.0");

		ServiceLookupKey lookupKey2 = new ServiceLookupKey();
		lookupKey2.setServiceName("com.haven.simplej.rpc.service");
		lookupKey2.setVersion("1.0");

		System.out.println(lookupKey.equals(lookupKey2));
	}
}
