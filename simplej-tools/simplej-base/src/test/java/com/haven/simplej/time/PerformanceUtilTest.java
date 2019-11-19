package com.haven.simplej.time;

import com.vip.vjtools.vjkit.logging.PerformanceUtil;

/**
 * @author: havenzhang
 * @date: 2019/9/9 19:49
 * @version 1.0
 */
public class PerformanceUtilTest {

	public static void main(String[] args) throws InterruptedException {
		PerformanceUtil.start("009988");

		Thread.sleep(2000);
		System.out.println(PerformanceUtil.duration("009988"));

		Thread.sleep(1000);
		System.out.println(PerformanceUtil.end("009988"));
	}
}
