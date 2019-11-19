package com.haven.simplej.zip;

import org.junit.Test;

import java.io.File;

/**
 * @author: havenzhang
 * @date: 2019/4/23 19:44
 * @version 1.0
 */
public class ZipTest {

	@Test
	public void test() throws Exception {

		System.out.println(System.getProperty("user.dir"));

		ZipUtil.createZipFile("src",new File("d:/test2/eclipse2.zip"));
		System.out.println("-----------");
	}
}
