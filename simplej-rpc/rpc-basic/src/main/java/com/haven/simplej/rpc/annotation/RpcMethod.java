package com.haven.simplej.rpc.annotation;

import com.haven.simplej.rpc.constant.RpcConstants;

import java.lang.annotation.*;

/**
 * rpc服务暴露的方法，如果要暴露远程方法，必须加上该注解
 * @author: havenzhang
 * @date: 2018/4/27 18:59
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {

	/**
	 * 服务超时时间
	 * @return
	 */
	long timeout() default RpcConstants.DEFAULT_TIMEOUT;

	/**
	 * 服务版本，暂时没有用上
	 * @return
	 */
	String version() default RpcConstants.VERSION_1;
}


