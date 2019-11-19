package com.haven.simplej.rpc.test.server;

import org.junit.Test;

import java.util.Date;

/**
 * @author: havenzhang
 * @date: 2019/5/4 22:43
 * @version 1.0
 */
public class TimeTest {


	@Test
	public void test1() throws InterruptedException {
		long time = new Date().getTime();
		System.out.println(new Date(time));
		Thread.sleep(2000);
		System.out.println(new Date().getTime()-time);
	}
}
