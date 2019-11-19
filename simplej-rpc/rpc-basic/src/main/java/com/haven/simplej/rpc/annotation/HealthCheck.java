package com.haven.simplej.rpc.annotation;

import java.lang.annotation.*;

/**
 * rpc健康监测方法
 * 加到方法上，方法需要返回int,0代表服务正常，其他代表异常
 * @author: havenzhang
 * @date: 2019/4/27 18:59
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HealthCheck {
	/**
	 * 服务超时时间,默认是20毫秒
	 * @return
	 */
	long timeout() default 20;
}
