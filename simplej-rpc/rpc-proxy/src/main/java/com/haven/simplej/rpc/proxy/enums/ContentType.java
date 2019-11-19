package com.haven.simplej.rpc.proxy.enums;

/**
 * @author: havenzhang
 * @date: 2019/5/18 16:11
 * @version 1.0
 */
public enum ContentType {

	APPLICATION_JSON("application/json"),
	APPLICATION_XML("application/xml"),
	TEXT_PLAIN("text/plain"),
	TEXT_XML("text/xml"),
	TEXT_HTML("text/html"),
	MULTIPART_FORM_DATA("multipart/form-data"),
	APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");

	String value;

	public String getValue(){
		return value;
	}

	ContentType(String contentType){
		this.value = contentType;
	}
}
