package com.haven.simplej.rpc.proxy.http.model;

import lombok.Data;

import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/5/16 20:44
 * @version 1.0
 */
@Data
public class HttpResponse {

	private Map<String,String> header;

	private byte[] body;

}
