package ${package}.${business};

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 在Tomcat容器中以war包部署的时候，需要继承SpringBootServletInitializer类
 * 并且需要重写configure方法
 * 如果是以jar包方式运行，则不会调用configure方法
 * @author haven.zhang
 * @date 2019/1/11.
 */
@Slf4j
public class ServletInitializer extends SpringBootServletInitializer  {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.debug("-----------------ServletInitializer onStartup-----------------");
		super.onStartup(servletContext);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.debug("-----------------ServletInitializer configure-----------------");
		return application.sources(StartUpApplication.class);
	}
}
