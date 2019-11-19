package com.haven.simplej.rpc.model;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2018/5/19 11:12
 * @version 1.0
 */
@Data
public class UrlLookupKey {

	/**
	 * 匹配所有
	 */
	public static final String ANY = "*";

	private String uri;
	/**
	 * 命名空间，用于区分不同的web服务
	 */
	private String namespace;
	/**
	 * get/post
	 */
	private String httpMethod = ANY;
	/**
	 * header 参数，如：xxx=zzzz
	 */
	private String header = ANY;

	/**
	 * url服务版本号
	 */
	private String version;
}
