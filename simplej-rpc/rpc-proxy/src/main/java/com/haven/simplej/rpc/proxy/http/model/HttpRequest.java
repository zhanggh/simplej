package com.haven.simplej.rpc.proxy.http.model;

import com.haven.simplej.rpc.model.RpcRequest;
import lombok.Data;

import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/5/15 22:37
 * @version 1.0
 */
@Data
public class HttpRequest extends RpcRequest {
	/**
	 * http请求头
	 */
	Map<String, String> httpHeader;
	/**
	 * http请求参数，表单请求的时候
	 */
	Map<String, String> reqParams;

	/**
	 * 请求流
	 */
	byte[] stream;
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
