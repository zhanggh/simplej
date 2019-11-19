package com.haven.simplej.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类字段与xml节点别名映射
 * @author: havenzhang
 * @date: 2019/4/12 17:35
 * @version 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface XmlAlias {
	String value() default "";
}
