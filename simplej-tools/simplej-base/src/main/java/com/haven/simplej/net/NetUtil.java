package com.haven.simplej.net;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 网络工具
 * @author: havenzhang
 * @date: 2019/5/9 23:17
 * @version 1.0
 */
@Slf4j
public class NetUtil extends com.vip.vjtools.vjkit.net.NetUtil {

	/**
	 * 网络连接测试，用于判断网络是否正常
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean connect(String host, int port) {

		return connect(host, port,100);
	}

	public static boolean connect(String host, int port, int timeout) {

		try {
			Socket socket = new Socket();
			SocketAddress address = new InetSocketAddress(host, port);
			socket.connect(address, timeout);
			socket.close();
			return true;
		} catch (IOException e) {
			log.debug("connect fail");
		}
		return false;
	}
}
