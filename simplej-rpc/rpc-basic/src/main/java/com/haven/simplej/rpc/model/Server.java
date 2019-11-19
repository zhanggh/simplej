package com.haven.simplej.rpc.model;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/5/9 22:27
 * @version 1.0
 */
@Data
public class Server {
	private String host;
	private int port;

	public Server(String host, int port) {
		this.host = host;
		this.port = port;
	}
}
