package com.haven.simplej.window;

import com.haven.simplej.windows.WindowUtil;

import java.net.URISyntaxException;

/**
 * @author haven.zhang
 * @date 2019/1/16.
 */
public class WindowUtilTest {
	public static void main(String[] args) throws URISyntaxException {
		String url = "http://localhost/project/exception";

		System.out.println(System.getProperty("user.dir"));
//		WindowUtil.browse(new URI(url));

		WindowUtil.openDir("E:\\vipay\\etools\\codegen/output/projects/".replaceAll("\\/","\\\\"));

		System.out.println("--------------------------");
	}
}
