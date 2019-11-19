package com.haven.simplej.rpc.test.server;

import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.heathcheck.IHeathCheck;

/**
 * @author: havenzhang
 * @date: 2019/5/13 23:40
 * @version 1.0
 */
public class HeartbeartTest {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
		System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "9092");
//		ExecutorService executorService = Executors.newFixedThreadPool(100);
		IHeathCheck service = ServiceProxy.create().setInterfaceClass(IHeathCheck.class).build();
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}


//		for (int i = 0; i < 160000; i++) {
//			final int count =i;
//			//			HeathResponse resp = service.healthCheck();
//			executorService.execute(() -> {
//				try {
//					System.out.println("-----------------------------------------i:" + count);
//					service.healthCheck();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			});
//			//			System.out.println(JSON.toJSONString(resp, true));
//
//
//		}

		Thread.sleep(13000);

		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			service.healthCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-----------------------------");
	}
}
