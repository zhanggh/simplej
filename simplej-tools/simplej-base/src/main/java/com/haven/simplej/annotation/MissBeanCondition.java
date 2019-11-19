package com.haven.simplej.annotation;

import java.lang.annotation.*;

/**
 * @author: havenzhang
 * @date: 2019/9/9 11:32
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MissBeanCondition {
}
