package com.haven.simplej.rpc.enums;

/**
 * rpc 报文类型
 * @author: havenzhang
 * @date: 2018/05/04 20:54
 * @version 1.0
 */
public enum MsgType {

	REQUEST((byte)0),RESPONSE((byte)1);

	public byte value;

	MsgType(byte type){
		this.value = type;
	}


}
