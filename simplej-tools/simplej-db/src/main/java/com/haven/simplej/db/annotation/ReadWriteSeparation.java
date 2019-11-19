package com.haven.simplej.db.annotation;

import com.haven.simplej.db.enums.ReadWriteType;

import java.lang.annotation.*;

/**
 * 读写分离操作注解，当业务操作方法上加上该注解，则代表数据库的操作需要读写分离
 * @author haven.zhang
 * @date 2019/1/26.
 */
@Target({ ElementType.METHOD , ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ReadWriteSeparation {

	/**
	 * 指定被注解的方法属于读操作还是写操作
	 * @return
	 */
	ReadWriteType type() default ReadWriteType.WRITE;

	/**
	 * 操作的数据库服务是否为master节点
	 * 写操作都是在master节点，而读操作一般在slaver节点，但有时候我们也希望它在master节点进行读，
	 * 所以可以自定义配置
	 * @return
	 */
	boolean isMaster() default true;
}
