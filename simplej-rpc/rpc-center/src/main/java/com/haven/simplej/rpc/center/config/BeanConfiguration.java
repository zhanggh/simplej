package com.haven.simplej.rpc.center.config;

import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.config.RpcConfiguration;
import com.haven.simplej.rpc.server.netty.RpcServerContext;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;


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
		return "execution(* com.haven.simplej.rpc.center.service.*.*(..))"
				+ " || execution(* com.haven.simplej.authen.manager.*.*(..))";
	}
}


