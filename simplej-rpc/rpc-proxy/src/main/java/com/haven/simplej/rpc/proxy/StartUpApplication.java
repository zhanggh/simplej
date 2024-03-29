package com.haven.simplej.rpc.proxy;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * proxy 服务启动类
 * @author haven.zhang
 * @date 2018/11/28.
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource(value = {"classpath:/application.properties"})
@EnableScheduling
public class StartUpApplication {

	static {
		System.setProperty(RpcConstants.RPC_SERVICE_BASE_PACKAGE_KEY, StartUpApplication.class.getPackage().getName());
	}
	public static void main(String[] args) {
		SpringApplication.run(StartUpApplication.class, args);
		log.info("{} start up success", PropertyManager.get(RpcConstants.RPC_APP_NAME));
	}
}
