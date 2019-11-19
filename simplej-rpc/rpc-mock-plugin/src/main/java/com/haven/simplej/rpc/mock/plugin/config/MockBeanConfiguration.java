package com.haven.simplej.rpc.mock.plugin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author: havenzhang
 * @date: 2019/10/31 23:44
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.rpc.mock.plugin", excludeFilters = @ComponentScan.Filter(type =
		FilterType.REGEX,
		pattern = "com\\.haven\\.simplej\\.rpc\\.mock\\.plugin\\.config.*"))
public class MockBeanConfiguration {
}
