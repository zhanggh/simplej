package com.haven.simplej.authen.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 20:34
 * @Version 1.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.authen", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.haven\\.simplej\\.authen\\.config.*"))
public class AuthenConfiguration {

	public AuthenConfiguration() {
		log.info("init AuthenConfiguration");
	}
}
