package com.haven.simplej.authen.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 16:42
 * @Version 1.0
 */
@Slf4j
@Component
public class AccessLogManager {

	/**
	 * 登记访问日志
	 * @param request
	 * @param response
	 * @param handler
	 */
	public void registAccessLog(HttpServletRequest request, HttpServletResponse response, Object handler){
		log.info("---------------registAccessLog------------------");
	}
}
