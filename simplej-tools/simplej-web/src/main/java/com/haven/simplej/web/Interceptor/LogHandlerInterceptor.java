package com.haven.simplej.web.Interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 只需要实现了Interceptor接口即可自动添加到拦截器列表中
 * @author haven.zhang
 * @date 2019/1/11.
 */
@Slf4j
public class LogHandlerInterceptor implements Interceptor {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		log.debug("LogHandlerInterceptor postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.debug("LogHandlerInterceptor afterCompletion,request:{}", JSON.toJSONString(request.getParameterMap(), true));
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("LogHandlerInterceptor preHandle");
		return true;
	}

	@Override
	public String getPathPattern() {
		return "/**";
	}
}
