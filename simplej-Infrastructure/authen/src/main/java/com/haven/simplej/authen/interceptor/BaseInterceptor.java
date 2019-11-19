package com.haven.simplej.authen.interceptor;

import com.haven.simplej.web.Interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 16:16
 * @Version 1.0
 */
@Slf4j
public abstract class BaseInterceptor extends HandlerInterceptorAdapter implements Interceptor {
}
