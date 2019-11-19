package com.haven.simplej.authen.interceptor;

import com.haven.simplej.authen.annotation.AuthAccess;
import com.haven.simplej.authen.annotation.LoginAccess;
import com.haven.simplej.authen.constant.AuthenConstant;
import com.haven.simplej.authen.manager.AuthorityManager;
import com.haven.simplej.authen.manager.LoginManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * @Author: havenzhang
 * @Date: 2019/4/6 16:15
 * @Version 1.0
 */
@Slf4j
@Component
public class LoginInterceptor extends BaseInterceptor {

	@Autowired
	private LoginManager loginManager;

	@Override
	public String getPathPattern() {
		return "/**";
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getServletPath();
		log.info("**********************进入LoginInterceptor**********************");
		log.info("请求uri:" + uri);
		if ("/".equals(uri) || "/login.html".equals(uri) || "/login".equals(uri) || "/logout.html".equals(uri) || "/logout".equals(uri))
			return true;

		Object handlerObj = handler;
		boolean authLessMethod = false;
		if (handlerObj instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handlerObj;
			LoginAccess access = handlerMethod.getBean().getClass().getAnnotation(LoginAccess.class);
			if (access == null) {
				access = handlerMethod.getMethodAnnotation(LoginAccess.class);
				authLessMethod = access != null;
			} else {
				authLessMethod = true;
			}
			if (access == null) {
				AuthAccess authAccess = handlerMethod.getBean().getClass().getAnnotation(AuthAccess.class);
				if(authAccess == null){
					authAccess = handlerMethod.getMethodAnnotation(AuthAccess.class);
					authLessMethod = authAccess != null;
				}else {
					authLessMethod = true;
				}
			}
		}

		if (authLessMethod) {
			loginManager.checkAccess((String) request.getSession().getAttribute(AuthenConstant.USER_CODE_KEY), uri);
		}
		return true;
	}
}
