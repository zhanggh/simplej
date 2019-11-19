package com.haven.simplej.rpc.model;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/5/15 22:22
 * @version 1.0
 */
@Data
public class HttpHeader extends RpcHeader {


	/**
	 * 对http请求有效
	 */
	private String httpUrl;

	/**
	 * 对http请求有效
	 */
	private String httpMethod;

	/**
	 * 对http有效
	 */
	private String contentType;
}
