package com.haven.simplej.rpc.admin.test;

import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.config.model.ConfigItemRpcModel;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.server.heathcheck.IHeathCheck;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author: havenzhang
 * @date: 2019/11/6 23:08
 * @version 1.0
 */
public class ConfigTest {

	private IConfigService service;


	@BeforeClass
	public static void beforeClass() {

		System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY, "127.0.0.1");
		System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY, "8181");
	}

	@Before
	public void before() {
		service = ServiceProxy.create().setInterfaceClass(IConfigService.class).build();
	}

	@Test
	public void test() {

		ConfigItemRpcModel item = new ConfigItemRpcModel();
		item.setDescription("test");
		item.setId(99);
		System.out.println(service.addConfigItem(item));
	}


	@Test
	public void shutdown() throws InterruptedException {
		InvocationContext.setServer("127.0.0.1", 8383);
		IHeathCheck check = ServiceProxy.create().setSyncRequest(true).setServerNamespace("www.register.rpc.simplej"
				+ ".com").setInterfaceClass(IHeathCheck.class).build();

		boolean result = check.shutdown("", "");

		System.out.println("response:" + result);
//		Thread.sleep(10000);
	}
}
