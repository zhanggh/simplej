package com.haven.simplej.rpc.registry.enums;

/**
 * 配置项类型，
 * 1：普通key -value 配置
 * 2：配置文件
 * @author: havenzhang
 * @date: 2018/6/12 23:37
 * @version 1.0
 */
public enum ConfigItemType {

	SIMPLE("1"),FILE("2");

	private String value;
	ConfigItemType(String value){
		this.value = value;
	}

	public String value(){
		return this.value;
	}
}
