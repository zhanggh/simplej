package com.haven.simplej.rpc.annotation;

import java.lang.annotation.*;

/**
 * 指定某个入参为实体类的时候
 * @author: havenzhang
 * @date: 2019/5/2 23:33
 * @version 1.0
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcStruct {

	/**
	 * 是否可空
	 * @return
	 */
	boolean required() default false;

}
