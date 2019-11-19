package com.haven.simplej.rpc.enums;

/**
 * 请求包类型，
 * @author: havenzhang
 * @date: 2018/10/7 14:07
 * @version 1.0
 */
public enum MsgFlag {

	BUSINESS((byte)1), HEARTBEAT((byte)0);

	byte value;

	MsgFlag(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}

}
