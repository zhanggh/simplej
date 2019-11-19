package com.haven.simplej.rpc.auth.plugin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author: havenzhang
 * @date: 2018/10/31 22:49
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.rpc.auth.plugin", excludeFilters = @ComponentScan.Filter(type =
		FilterType.REGEX,
		pattern = "com\\.haven\\.simplej\\.rpc\\.auth\\.plugin\\.config.*"))
public class AuthPluginConfiguration {

}
