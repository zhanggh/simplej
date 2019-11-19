package com.haven.simplej.rpc.server.http.model;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/9/12 20:44
 * @version 1.0
 */
@Data
public class HttpModel {
	private String httpMethod;
	private String url;
	private String httpBody;
	private String headers;
	private String contentType;
}
