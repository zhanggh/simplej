package com.haven.simplej.rpc.server.http.model;

import lombok.Data;

/**
 * http请求调用rpc服务的model
 * @author: havenzhang
 * @date: 2019/1/10 17:38
 * @version 1.0
 */
@Data
public class Http2RpcModel {

	private String serviceVersion;
	private String serviceName;
	private String methodName;

	private String methodId;
	/**
	 * 方法参数类型，如java.lang.String,java.lang.Integer
	 */
	private String methodParamTypes;




}
