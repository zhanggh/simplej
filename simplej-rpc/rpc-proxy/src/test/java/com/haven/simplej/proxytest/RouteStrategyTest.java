package com.haven.simplej.proxytest;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: havenzhang
 * @date: 2019/9/13 18:35
 * @version 1.0
 */
public class RouteStrategyTest {


	@Test
	public void test(){
		for (int i = 0;i<900000;i++){
			int next = ThreadLocalRandom.current().nextInt(6);
			System.out.println("num:"+next);
			if(next == 5){
				System.out.println("dsfdsfdsf");
				break;
			}
		}
	}
}
