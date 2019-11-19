package com.haven.simplej.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @Author: havenzhang
 * @Date: 2019/4/7 22:18
 * @Version 1.0
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.security", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.haven\\.simplej\\.security\\.config.*"))
public class SecurityConfiguration {
}
