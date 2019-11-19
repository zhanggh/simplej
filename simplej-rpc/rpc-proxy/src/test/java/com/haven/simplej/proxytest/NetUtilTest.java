package com.haven.simplej.proxytest;

import com.haven.simplej.net.NetUtil;

/**
 * @author: havenzhang
 * @date: 2019/9/13 18:23
 * @version 1.0
 */
public class NetUtilTest {

	public static void main(String[] args) {
		System.out.println(NetUtil.getHostName());
		System.out.println(NetUtil.getLocalHost());
		System.out.println(NetUtil.findRandomAvailablePort(5000,9999));
	}
}
