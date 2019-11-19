package com.haven.simplej.web.config;

import com.haven.simplej.web.filter.ExceptionHandleFilter;
import com.haven.simplej.web.filter.WebAccessFilter;
import com.haven.simplej.web.filter.WebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

/**
 * @author haven.zhang
 * @date 2019/1/9.
 */
public class WebConfiguration {

	/**
	 * 默认编码
	 */
	private static final String DEFAULT_ENCODE = "UTF-8";

		@Bean
		public ServletContextInitializer initializer() {
			return sc -> {
				String ctxPath = sc.getContextPath();
				ctxPath = "/".equals(ctxPath) ? "" : ctxPath;
				sc.setAttribute("ctxPath", ctxPath);
			};
		}

	@Bean
	public CharacterEncodingFilter utf8EncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding(DEFAULT_ENCODE);
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Bean
	public FilterRegistrationBean addExecptionFilter() {
		WebFilter filter = new ExceptionHandleFilter("/*");
		FilterRegistrationBean registrationBean = createFilterRegBean(filter);
		registrationBean.setOrder(filter.getOrder());
		registrationBean.addUrlPatterns(filter.getUrlMapping());
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean addWebAccessFilter() {
		WebFilter filter = new WebAccessFilter("/*");
		FilterRegistrationBean registrationBean = createFilterRegBean(filter);
		registrationBean.setOrder(filter.getOrder());
		registrationBean.addUrlPatterns(filter.getUrlMapping());
		return registrationBean;
	}

	protected FilterRegistrationBean createFilterRegBean(Filter filter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(filter);
		registrationBean.setName(filter.getClass().getSimpleName());
		return registrationBean;
	}


	/**
	 * 文件上传配置
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 文件最大
		factory.setMaxFileSize(DataSize.ofMegabytes(100)); //KB,MB
		// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.ofMegabytes(100));
		return factory.createMultipartConfig();
	}

	//	@Bean
	//	public InternalResourceViewResolver viewResolver() {
	//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	//		resolver.setPrefix("/WEB-INF/views/");
	//		resolver.setSuffix(".jsp");
	//		resolver.setOrder(1);
	//		resolver.setViewClass(JstlView.class);
	//		return resolver;
	//	}
}
