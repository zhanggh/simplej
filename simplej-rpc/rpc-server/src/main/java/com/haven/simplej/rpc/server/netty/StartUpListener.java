package com.haven.simplej.rpc.server.netty;

/**
 * netty服务启动成功后监听处理器
 * @author: havenzhang
 * @date: 2018/06/13 22:11
 * @version 1.0
 */
public interface StartUpListener {

	/**
	 * 监听服务启动
	 */
	void listen();
}
