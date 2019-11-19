package com.haven.epay.payment;

import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ImportResource;

/**
 * @author haven.zhang
 * @date 2018/11/28.
 */
@Slf4j
@SpringBootApplication
@PropertySource(value = {"classpath:/application.properties","classpath:/config.properties"})

public class StartUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartUpApplication.class, args);
		log.info("{} start up success", PropertyManager.get(RpcConstants.RPC_APP_NAME));
	}
}
