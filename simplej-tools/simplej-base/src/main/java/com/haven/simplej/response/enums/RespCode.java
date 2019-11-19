package com.haven.simplej.response.enums;

/**
 * 通用响应码
 * @author haven.zhang
 * @date 2019/1/9.
 */
public enum RespCode {
	SUCCESS("成功"),
	PROCESS("处理中"),
	FAIL("处理失败"),
	UNKNOW("未知状态");

	String code;
	String msg;
	RespCode(String msg){
		this.msg = msg;
		this.code = this.name().toLowerCase();
	}
}
