package com.haven.simplej.proxytest;

import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: havenzhang
 * @date: 2019/5/9 23:38
 * @version 1.0
 */
public class ScheduleTest {

	public static void main(String[] args) {
		new ScheduleTest().test();
	}
	@Test
	public void test() {
		ThreadPoolFactory.getScheduleExecutor().scheduleAtFixedRate(() -> {
			System.out.println("test");
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}, 0,100, TimeUnit.MILLISECONDS);
	}
}
