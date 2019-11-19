package com.haven.simplej.rpc.config.plugin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author: havenzhang
 * @date: 2019/10/31 23:09
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.haven.simplej.rpc.config.plugin", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
		pattern = "com\\.haven\\.simplej\\.rpc\\.config\\.plugin\\.config.*"))
public class ConfigPluginBeanConfiguration {
}
