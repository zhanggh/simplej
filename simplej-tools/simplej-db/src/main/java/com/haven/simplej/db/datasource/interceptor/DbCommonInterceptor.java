package com.haven.simplej.db.datasource.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author haven.zhang
 * @date 2019/1/28.
 */
@Slf4j
public class DbCommonInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		log.info("into DbCommonInterceptor");

		Object result = methodInvocation.proceed();
		log.info("out DbCommonInterceptor");
		return result;
	}
}
