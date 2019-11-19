package com.haven.simplej.test.common;

import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import org.junit.Test;

/**
 * @author: havenzhang
 * @date: 2019/4/25 15:49
 * @version 1.0
 */
public class TimerTest {

	@Test
	public void test() throws InterruptedException {
		PerformanceUtil.start();
		Thread.sleep(1000);
//		PerformanceUtil.end();
		System.out.println(PerformanceUtil.duration());
	}
}
