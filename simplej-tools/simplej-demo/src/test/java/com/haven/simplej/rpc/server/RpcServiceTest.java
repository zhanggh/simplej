package com.haven.simplej.rpc.server;

import com.haven.epay.payment.StartUpApplication;
import com.haven.epay.payment.service.PaymentListService;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: havenzhang
 * @date: 2019/4/28 21:42
 * @version 1.0
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = StartUpApplication.class)
public class RpcServiceTest {

	static {
		System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY,"127.0.0.1");
		System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY,"8087");
//		System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY,"127.0.0.1");
//		System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY,"8086");
//		System.setProperty(RpcConstants.RPC_SERVER_PORT_KEY,"9091");
//		System.setProperty(RpcConstants.RPC_SERVICE_BASE_PACKAGE_KEY,"com.haven.epay.payment.service");
	}

	@Test
	public void test(){
		PaymentListService service = ServiceProxy.create().setInterfaceClass(PaymentListService.class).build();
		String resp = service.get("000",888);
		System.out.println(resp);
	}
}
