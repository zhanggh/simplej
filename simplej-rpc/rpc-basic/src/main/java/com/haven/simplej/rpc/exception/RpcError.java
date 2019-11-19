package com.haven.simplej.rpc.exception;

import lombok.Getter;

/**
 * 错误码定义
 * @author: havenzhang
 * @date: 2019/4/26 18:03
 * @version 1.0
 */
@Getter
public enum  RpcError {

	SUCCESS("200","success"),

	START_UP_FAIL("100","start up fail"),

	SYSTEM_BUSY("500","system busy"),
	NOT_SUPPORT_PROTOCOL("501","not support protocol"),
	NOT_SUPPORT_DATATYPE("502","not support datatype"),
	NOT_SUPPORT_SERIAL_TYPE("503","not support serial type"),
	RELAY_REQUEST_PARAM_ERROR("504","relay param parse error"),
	DECODE_ERROR("505","decode error"),
	ENCODE_ERROR("508","encode error"),
	SERVER_OVERLOAD("506","server overload"),
	SERVER_METHOD_OVERLOAD("507","server method request overload"),

	REQUEST_TIMEOUT("301","request timeout"),
	RPC_FIELD_ERROR("302","request params error"),
	SERVICE_NOT_FOUND("303","error service request"),
	SERVICE_CLASS_NOT_FOUND("304","error service request"),
	METHOD_NOT_FOUND("305","error method request,can not find method"),
	SERVICE_INSTANCE_NOT_FOUND("306","error method request,instance can not be found"),
	INVAILD_REQUEST("307","invalid request"),

	ERROR_METHOD_ID("308","invalid request,can not find local method"),
	REQUEST_PARAMS_NOT_MATCH("308","invalid request,request params are not match params of local method"),
	RELAY_BODY_TYPE_ERROR("309","relay body must be string"),
	INVAILD_RESPONSE("310","invalid response");


	private String errorCode;
	private String errorMsg;

	RpcError(String errorCode,String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
}
