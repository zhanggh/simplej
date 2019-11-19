package com.haven.simplej.db.test;

import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.db.test.service.ComService;
import com.haven.simplej.db.test.service.impl.ComServiceImpl;
import com.haven.simplej.db.test.sharding.ShardingWithNameStrategy;
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
 * @author: havenzhang
 * @date: 2019/9/3 23:50
 * @version 1.0
 */
@Configuration
@Slf4j
public class BeanConfiguration extends BaseDataSourceConfiguration {

	@Autowired
	Environment environment;

	private PropertyManager propertyManager;

	private SpringContext context;

	@Bean("context")
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

	@Bean
	public ComService comService(){
		return new ComServiceImpl();
	}

	@Bean("shardingWithNameStrategy")
	public ShardingWithNameStrategy shardingWithNameStrategy(){
		return new ShardingWithNameStrategy();
	}

	@Override
	public String getShardingAopExpress() {
		return "execution(* com.haven.simplej.db.test.service.*.*(..))";
	}
}
