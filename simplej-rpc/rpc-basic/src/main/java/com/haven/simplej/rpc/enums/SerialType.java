package com.haven.simplej.rpc.enums;

import lombok.Getter;

/**
 * 报文序列化方式
 * @author: havenzhang
 * @date: 2018/8/26 21:23
 * @version 1.0
 */
@Getter
public enum SerialType {

	JSON(2),PROTOSTUFF(1),RELAY(3),FILE_TRANSFER(4),CUSTOM(5);//CUSTOM-定制的协议

	private int value;

	SerialType(int value){
		this.value = value;
	}
}
