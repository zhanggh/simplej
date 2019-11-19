package com.haven.simplej.web.helper;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: havenzhang
 * @Date: 2019/4/7 23:00
 * @Version 1.0
 */
public class WebHelper {

	/***
	 * 判断一个请求是否为AJAX请求,是则返回true
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		//如果requestType能拿到值，并且值为 XMLHttpRequest ,表示客户端的请求为异步请求，那自然是ajax请求了，反之如果为null,则是普通的请求
		if (requestType == null) {
			return false;
		}
		return true;
	}

	/**
	 * 请求类型是否为json
	 * @param request
	 * @return
	 */
	public static boolean isJsonRequest(HttpServletRequest request){
		if(StringUtils.equalsIgnoreCase(request.getContentType(),"json")){
			return true;
		}
		return false;
	}
}
