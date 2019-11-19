package com.haven.simplej.rpc.mock.config;

import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.rpc.server.config.RpcConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
@Configuration
@Import(RpcConfiguration.class)
@Slf4j
public class BeanConfiguration extends BaseDataSourceConfiguration {


	public BeanConfiguration() {
		log.info("init BeanConfiguration");
	}


	@Override
	public String getShardingAopExpress() {
		//此处表达式匹配使用了分表分库组件（@see @RepositorySharding）的包&类、方法
		//在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
		return "execution(* com.haven.simplej.rpc.mock.service.*.*(..))"
				+ " || execution(* com.haven.simplej.authen.manager.*.*(..))";
	}
}


