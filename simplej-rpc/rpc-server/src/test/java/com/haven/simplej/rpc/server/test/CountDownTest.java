package com.haven.simplej.rpc.server.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: havenzhang
 * @date: 2019/9/27 20:35
 * @version 1.0
 */
public class CountDownTest {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch downLatch = new CountDownLatch(5);

		for (int i = 0; i < 2; i++) {
			downLatch.countDown();
		}
		downLatch.await(5, TimeUnit.SECONDS);
		System.out.println("------------------------");
	}
}
