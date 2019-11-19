package com.haven.epay.payment.config;

import com.haven.simplej.authen.config.AuthenConfiguration;
import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.server.netty.RpcServerContext;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.List;


/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
@Configuration
@Import({AuthenConfiguration.class})
@Slf4j
public class BeanConfiguration extends BaseDataSourceConfiguration {


	@Autowired
	Environment environment;

	private PropertyManager propertyManager;

	private SpringContext context;

	public BeanConfiguration() {
		log.info("init BeanConfiguration");
	}


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


	/**
	 * 注册netty服务
	 * @return
	 */
	@Bean
	public RpcServerContext serverContext(@Qualifier("context") SpringContext context){
		//注册rpc服务所在的包路径
		//		System.setProperty(RpcConstants.RPC_SERVER_PORT_KEY,"9092");
		System.setProperty(RpcConstants.RPC_SERVICE_BASE_PACKAGE_KEY,"com.haven.epay.payment.service");
		if(SpringContext.getContext() == null){
			springContext();
		}
		List<RpcFilter> filters = SpringContext.getBeansOfType(RpcFilter.class);
		return new RpcServerContext(filters);
	}

	@Override
	public String getShardingAopExpress() {
		//此处表达式匹配使用了分表分库组件（@see @DBShardingStrategy）的包&类、方法
		//在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
		return "execution(* com.haven.epay.payment.service.*.*(..))"
				+ " || execution(* com.haven.epay.payment.service2.*.*(..))"
				+ " || execution(* com.haven.simplej.authen.manager.*.*(..))";
	}
}


