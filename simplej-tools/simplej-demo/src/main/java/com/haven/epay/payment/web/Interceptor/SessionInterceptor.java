package com.haven.epay.payment.web.Interceptor;

import com.haven.simplej.web.Interceptor.Interceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: havenzhang
 * @Date: 2019/4/10 23:54
 * @Version 1.0
 */
@Component
public class SessionInterceptor implements Interceptor {
	@Override
	public String getPathPattern() {
		return "/**";
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.getSession().setAttribute("userCode","havenzhang");
		return true;
	}
}
