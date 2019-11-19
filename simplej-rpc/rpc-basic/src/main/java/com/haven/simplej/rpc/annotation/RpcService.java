package com.haven.simplej.rpc.annotation;

import com.haven.simplej.rpc.constant.RpcConstants;

import java.lang.annotation.*;

/**
 * service类如果加上该annotation，则代表是rpc接口服务
 * 注意，rpc接口服务不能有多个实现类，否则报错
 * @author: havenzhang
 * @date: 2019/4/27 18:48
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcService {

	/**
	 * 服务超时时间
	 * 默认是60秒
	 * @return
	 */
	long timeout() default RpcConstants.DEFAULT_TIMEOUT;

	/**
	 * 服务名，空时，去类所在的包+类名，如：com.haven.simplej.payment.OrderService
	 * 默认取类名
	 * @return
	 */
	String serviceName() default "";

	/**
	 * 服务版本
	 * @return
	 */
	String version() default RpcConstants.VERSION_1;

}
