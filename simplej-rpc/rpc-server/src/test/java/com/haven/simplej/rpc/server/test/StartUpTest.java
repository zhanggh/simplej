package com.haven.simplej.rpc.server.test;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author: havenzhang
 * @date: 2019/9/10 11:27
 * @version 1.0
 */
public class StartUpTest {

	public static void main(String[] args) throws InterruptedException {

		System.out.println("-----------------");
		Signal.handle(new Signal("TERM"), signal -> {
			System.out.println(signal.getName());
		});

		System.out.println("0000000000000000000000000");
		Thread.sleep(3000);
	}
}
