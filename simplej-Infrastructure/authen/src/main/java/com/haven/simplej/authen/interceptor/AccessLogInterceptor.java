package com.haven.simplej.authen.interceptor;

import com.haven.simplej.authen.annotation.AccessLog;
import com.haven.simplej.authen.annotation.LoginAccess;
import com.haven.simplej.authen.constant.AuthenConstant;
import com.haven.simplej.authen.manager.AccessLogManager;
import com.haven.simplej.authen.manager.LoginManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作日志登记拦截器
 * @Author: havenzhang
 * @Date: 2019/4/6 16:15
 * @Version 1.0
 */
@Slf4j
@Component
public class AccessLogInterceptor extends BaseInterceptor {

	@Autowired
	private AccessLogManager accessLog;

	@Override
	public String getPathPattern() {
		return "/**";
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		String uri = request.getServletPath();
		log.info("**********************进入AccessLogInterceptor**********************");
		log.info("请求uri:" + uri);
		if ("/".equals(uri))
			return;

		Object handlerObj = handler;
		boolean authLessMethod = false;
		if (handlerObj instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handlerObj;
			AccessLog access = handlerMethod.getBean().getClass().getAnnotation(AccessLog.class);
			if (access == null) {
				access = handlerMethod.getMethodAnnotation(AccessLog.class);
			}
			authLessMethod = access != null;
		}

		if (authLessMethod) {
			accessLog.registAccessLog(request, response, handler);
		}
	}
}
