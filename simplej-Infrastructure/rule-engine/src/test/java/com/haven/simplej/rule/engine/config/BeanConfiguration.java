package com.haven.simplej.rule.engine.config;

import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;


/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
@Configuration
@Slf4j
public class BeanConfiguration extends BaseDataSourceConfiguration {


	@Autowired
	Environment environment;

	private PropertyManager propertyManager;

	private SpringContext context;

	public BeanConfiguration() {
		log.info("init BeanConfiguration");
	}


	@Bean
	@Order(1)
	@ConditionalOnMissingBean(SpringContext.class)
	public SpringContext springContext() {
		log.info("init springcontext {}", this.context != null);
		if (this.context != null) {
			return this.context;
		}
		return this.context = new SpringContext();
	}

	/**
	 * 实例化属性管理器
	 * @return
	 */
	@Bean
	public PropertyManager propertyManager() {
		log.info("init propertyManager");
		if (this.propertyManager != null) {
			return this.propertyManager;
		}
		return this.propertyManager = new PropertyManager();
	}

	@Override
	public String getShardingAopExpress() {
		//此处表达式匹配使用了分表分库组件（@see @RepositorySharding）的包&类、方法
		//在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
		return "execution(* com.haven.simplej.rule.engine.service.*.*(..)) || execution(* com.haven.simplej.rule.engine.service2.*.*(..))";
	}
}


