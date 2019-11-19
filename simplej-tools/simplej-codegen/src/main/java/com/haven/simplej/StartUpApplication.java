package com.haven.simplej;

import com.haven.simplej.windows.WindowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author haven.zhang
 * @date 2018/11/28.
 */
@Slf4j
//@SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@PropertySource(value = {"classpath:/application.properties"})
//@ImportResource(locations={"classpath:/mybatis.xml"})
public class StartUpApplication {

	/**
	 * 默认启动的url
	 */
	private static final String DEFAULT_URL = "http://localhost/";
	/**
	 * 是否自动打开浏览器
	 */
	private static boolean autoBrowse = true;



	public static void openBrowse() throws URISyntaxException {
		//启动浏览器并打开url
		if(autoBrowse){
			WindowUtil.browse(new URI(DEFAULT_URL));
		}
	}

	public static void setAutoBrowse(boolean autoBrowse){
		StartUpApplication.autoBrowse = autoBrowse;
	}

	public static void main(String[] args) throws URISyntaxException {
		//启动服务
		String logHome = "logs";
		System.setProperty("spring.pid.file", logHome + File.separator + "application.pid");
		SpringApplication application = new SpringApplication(StartUpApplication.class);
		ApplicationPidFileWriter pidFileWriter = new ApplicationPidFileWriter();
		pidFileWriter.setTriggerEventType(ApplicationReadyEvent.class);
		application.addListeners(pidFileWriter);
		application.addListeners((ApplicationListener<ApplicationFailedEvent>) event -> {
			log.error("spring init error exit process", event.getException());
			System.exit(-2);
		});
		application.run(args);
		//打开浏览器访问url
		openBrowse();
		log.info("codegen start up success");
	}
}
