package com.haven.simplej.web.config;


import com.haven.simplej.web.Interceptor.Interceptor;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


//@Configuration
public class MVCConfiguration extends WebMvcConfigurationSupport {

	@Autowired
	private List<Interceptor> interceptors;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
//		registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
	}

//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		registry.jsp("/WEB-INF/views/", ".jsp");
//	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}



	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		if(CollectionUtil.isNotEmpty(interceptors)){
			interceptors.forEach(e-> registry.addInterceptor(e).addPathPatterns(e.getPathPattern()));
		}
	}
}