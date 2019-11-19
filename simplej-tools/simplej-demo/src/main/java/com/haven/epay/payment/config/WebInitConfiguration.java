package com.haven.epay.payment.config;

import com.haven.epay.payment.web.filter.AuthorityFilter;
//import com.haven.epay.payment.web.servlet.DemoServlet;
import com.haven.epay.payment.web.listener.DemoListener;
import com.haven.epay.payment.web.servlet.DemoServlet;
import com.haven.simplej.web.config.WebConfiguration;
import com.haven.simplej.web.filter.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haven.zhang
 * @date 2019/1/28.
 */
@Slf4j
@Configuration
public class WebInitConfiguration extends WebConfiguration {

	public WebInitConfiguration() {
		log.info("init WebInitConfiguration");
	}

//	/**
//	 * 注册servlet
//	 * @return
//	 */
//	@Bean
//	public ServletRegistrationBean demoServlet() {
//		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new DemoServlet(), "/*");
//		registrationBean.setLoadOnStartup(1);
//		return registrationBean;
//	}


	@Bean
	public ServletListenerRegistrationBean demoListener() {
		ServletListenerRegistrationBean<DemoListener> registrationBean = new ServletListenerRegistrationBean<>(new DemoListener());
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean authorityFilter() {
		WebFilter filter = new AuthorityFilter();
		FilterRegistrationBean registrationBean = createFilterRegBean(filter);
		registrationBean.setOrder(filter.getOrder());
		registrationBean.addUrlPatterns(filter.getUrlMapping());
		return registrationBean;
	}

}
