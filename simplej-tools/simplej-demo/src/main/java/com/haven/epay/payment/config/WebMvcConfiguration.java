package com.haven.epay.payment.config;

import com.haven.simplej.web.Interceptor.LogHandlerInterceptor;
import com.haven.simplej.web.config.MVCConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 默认支持jsp视图，如果需要调整，重写对应的方法即可
 * @author haven.zhang
 * @date 2019/1/14.
 */
 
@Configuration
@Order(3)
@Slf4j
public class WebMvcConfiguration extends MVCConfiguration {
	private LogHandlerInterceptor logHandlerInterceptor;

	public WebMvcConfiguration() {
		log.info("init WebMvcConfiguration");
	}


	@Bean
	public LogHandlerInterceptor createLogHandler() {
		log.debug("init LogHandlerInterceptor");
		if (this.logHandlerInterceptor != null) {
			return this.logHandlerInterceptor;
		}
		return this.logHandlerInterceptor = new LogHandlerInterceptor();
	}

}
