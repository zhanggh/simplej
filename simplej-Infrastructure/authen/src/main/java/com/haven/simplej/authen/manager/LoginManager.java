package com.haven.simplej.authen.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 16:22
 * @Version 1.0
 */
@Slf4j
@Component
public class LoginManager {


	/**
	 * 判断用户是否有权限访问
	 * @return
	 */
	public boolean checkAccess(String userCode,String uri){

		log.info("--------------------login access verify--------------------");
		return true;
	}
}
