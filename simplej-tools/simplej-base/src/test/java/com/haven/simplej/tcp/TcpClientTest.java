package com.haven.simplej.tcp;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: havenzhang
 * @date: 2019/9/30 23:16
 * @version 1.0
 */
public class TcpClientTest {


	@Test
	public void test() throws UnsupportedEncodingException, InterruptedException {
		String host = "127.0.0.1";
		//		int port = 8899;
		int num = 2999;
		Executor executor = Executors.newFixedThreadPool(num);
		int port = 8080;
		int timeout = 5000;
		for (int i = 0; i < num; i++) {
//			executor.execute(() -> {
				long start = System.currentTimeMillis();
				byte[] resp = TcpClient.send(host, port, timeout, "name=1234567890&test=wwer", 1);
				try {
					System.out.println(new String(resp, "utf-8") + " ,cost:" + (System.currentTimeMillis() - start));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
//			});
		}

		Thread.sleep(3000);
		long start = System.currentTimeMillis();
		byte[] resp = TcpClient.send(host, port, timeout, "name=1234567890&test=wwer", 1);
		try {
			System.out.println(new String(resp, "utf-8") + " ,cost:" + (System.currentTimeMillis() - start));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
