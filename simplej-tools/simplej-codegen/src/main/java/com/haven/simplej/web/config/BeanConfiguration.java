package com.haven.simplej.web.config;

import com.haven.simplej.codegen.ProjectInfo;
import com.haven.simplej.codegen.builder.PropertyModelBuilder;
import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
//@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@Slf4j
public class BeanConfiguration extends BaseDataSourceConfiguration {


	@Autowired
	Environment environment;
	private PropertyManager propertyManager;
	private ProjectInfo propertyModel;

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

	@Bean("taskExecutor")
	public ThreadPoolTaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setKeepAliveSeconds(10);
		executor.setMaxPoolSize(200);
		executor.setQueueCapacity(1000);
		return executor;
	}

	@Override
	public String getShardingAopExpress() {
		//此处表达式匹配使用了分表分库组件（@see @DBShardingStrategy）的包&类、方法
		//在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
		return "execution(* com.haven.simplej.codegen.service.*.*(..)) || execution(* com.haven.simplej.codegen.service2.*.*(..))";
	}
}

