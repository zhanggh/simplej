package com.haven.simplej.rpc.server.enums;

/**
 * @author: havenzhang
 * @date: 2018/11/10 16:41
 * @version 1.0
 */
public enum ServerState {
     /** 没启动完成、启动成功、启动失败、关闭、故障 **/
	INIT(0), STARTUP_SUCC(1), STARTUP_FAIL(2),SHUTDOWN(3),FAULT(4);

	int state;

	ServerState(int state) {
		this.state = state;
	}

	public int state() {
		return this.state;
	}}
