package com.haven.simplej.web.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author haven.zhang
 * @date 2019/1/14.
 */
public interface Interceptor extends HandlerInterceptor {

	/**
	 * 拦截器对应的uri
	 * @return
	 */
	public String getPathPattern();
}
