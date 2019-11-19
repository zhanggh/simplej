package com.haven.simplej.net;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author: havenzhang
 * @date: 2019/5/9 23:24
 * @version 1.0
 */
public class NetUtilTest {

	@Test
	public void test(){
		String host = "127.0.0.2";
		int port = 443;

		System.out.println(NetUtil.connect(host,port));
	}

	@Test
	public void test2() throws IOException {
		ServerSocket serverSocket =  new ServerSocket(0); //读取空闲的可用端口
		int port = serverSocket.getLocalPort();

		System.out.println(port);
	}
}
