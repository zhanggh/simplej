package com.haven.simplej.db.configuration;

import com.haven.simplej.db.annotation.HasDatasourceConditon;
import com.haven.simplej.db.annotation.ShardingConditon;
import com.haven.simplej.db.base.BaseServiceImpl;
import com.haven.simplej.db.constant.BeanName;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.datasource.DatasourceFactory;
import com.haven.simplej.db.datasource.MatrixDataSource;
import com.haven.simplej.db.datasource.interceptor.DBShardingInterceptor;
import com.haven.simplej.db.datasource.interceptor.TableShardingInterceptor;
import com.haven.simplej.db.manager.DatasourceManager;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 数据源基础配置，支持分表分库策略
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
public abstract class BaseDataSourceConfiguration {


	private SpringContext context;

	private DBShardingInterceptor dbShardingInterceptor;
	private TableShardingInterceptor tableShardingInterceptor;
	private DataSource dataSource;
	private DefaultPointcutAdvisor tableShardingAdvisor;
	private DefaultPointcutAdvisor dBShardingAdvisor;
	private DefaultPointcutAdvisor commonAdvisor;
	private DefaultAdvisorAutoProxyCreator defaultAutoProxy;
	private AspectJExpressionPointcut shardingPointcut;

	private JdbcTemplate jdbcTemplate;
	private CommonDao commonDao;
	private DataSourceTransactionManager transactionManager;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private ThreadPoolTaskExecutor taskExecutor;

	@Bean(name = "appContext")
	@Order(1)
	@ConditionalOnMissingBean(SpringContext.class)
	public SpringContext springContext1() {
		log.info("init springcontext1");
		if (this.context != null) {
			return context;
		}
		return this.context = new SpringContext();
	}

	@Bean
	@ConditionalOnMissingBean(DataSource.class)
	public DatasourceManager datasourceManager() {
		log.info("init datasourceManager");
		return new DatasourceManager();
	}

	/**
	 * Druid DataSource
	 *
	 * @return DataSource
	 */
	@Bean(name = "mxDatasource")
	@Order(2)
	@HasDatasourceConditon
	@ConditionalOnMissingBean(DataSource.class)
	public DataSource dataSource() {
		List<Properties> props = DBUtil.readDataSourceProp(Constant.DB_PROPERTIE_PATH);
		log.info("init DataSource,source size:{} {}", props.size(), dataSource != null);
		if (dataSource != null) {
			return dataSource;
		}

		if (1 < props.size()) {
			dataSource = MatrixDataSource.getDataSource(props);
		} else if (props.size() == 1) {
			dataSource = DatasourceFactory.createDataSource(props.get(0));
		} else {
			throw new UncheckedException("no datasource config error,please set it in resource/db/xxxx.properties " +
					"file");
		}

		return dataSource;
	}


	@Bean(name = "tableShardingInterceptor")
	@ConditionalOnMissingBean(TableShardingInterceptor.class)
	@ShardingConditon
	public TableShardingInterceptor tableShardingInterceptor() {
		log.info("init TableShardingInterceptor {}", tableShardingInterceptor != null);
		if (tableShardingInterceptor != null) {
			return tableShardingInterceptor;
		}
		return tableShardingInterceptor = new TableShardingInterceptor();
	}


	@Bean(name = "dBShardingInterceptor")
	@ShardingConditon
	@ConditionalOnMissingBean(DBShardingInterceptor.class)
	public DBShardingInterceptor dbShardingIntercepor() {
		log.info("init DBShardingInterceptor {}", dbShardingInterceptor != null);
		if (dbShardingInterceptor != null) {
			return dbShardingInterceptor;
		}
		return dbShardingInterceptor = new DBShardingInterceptor();
	}

	/**
	 * 代理
	 * 非spring boot项目下，需要手工开启该bean实例化
	 * @return
	 */
	//	@Bean
	//	@ShardingConditon
	//	@ConditionalOnMissingBean(DefaultAdvisorAutoProxyCreator.class)
	//	public DefaultAdvisorAutoProxyCreator defaultAutoProxy() {
	//		log.info("init DefaultAdvisorAutoProxyCreator {}", defaultAutoProxy != null);
	//		if (defaultAutoProxy != null) {
	//			return defaultAutoProxy;
	//		}
	//		DefaultAdvisorAutoProxyCreator autoProxy = new DefaultAdvisorAutoProxyCreator();
	//		autoProxy.setProxyTargetClass(true);
	//		return defaultAutoProxy = autoProxy;
	//	}
	@Bean
	@ShardingConditon
	@ConditionalOnMissingBean(AspectJExpressionPointcut.class)
	public AspectJExpressionPointcut shardingPointcut() {
		log.info("init shardingPointcut");
		if (shardingPointcut != null) {
			return shardingPointcut;
		}

		shardingPointcut = new AspectJExpressionPointcut();
		//默认进行aop切面的包
		StringBuilder pointStr = new StringBuilder(" || execution(* ");
		pointStr.append(BaseServiceImpl.class.getPackage().getName());
		pointStr.append(".*.*(..))");
		pointStr.append(" || execution(* com.haven.simplej.authen.manager.*.*(..))");
		pointStr.append(" || execution(* com.haven.simplej.authen.service.*.*(..))");
		pointStr.append(" || execution(* com.haven.simplej.security.manager.*.*(..))");
		pointStr.append(" || execution(* com.haven.simplej.security.service.*.*(..))");
		shardingPointcut.setExpression(getShardingAopExpress() + pointStr.toString());
		return shardingPointcut;
	}

	@Bean
	@ShardingConditon
	public DefaultPointcutAdvisor tableShardingAdvisor(AspectJExpressionPointcut shardingPointcut,
			TableShardingInterceptor tableShardingInterceptor) {
		log.info("init tableShardingAdvisor {}", tableShardingAdvisor != null);
		if (tableShardingAdvisor != null) {
			return tableShardingAdvisor;
		}
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(shardingPointcut);
		advisor.setAdvice(tableShardingInterceptor);
		return tableShardingAdvisor = advisor;
	}

	@Bean
	@ShardingConditon
	public DefaultPointcutAdvisor dBShardingAdvisor(AspectJExpressionPointcut shardingPointcut,
			DBShardingInterceptor dbShardingIntercepor) {
		log.info("init dBShardingAdvisor {}", dBShardingAdvisor != null);
		if (dBShardingAdvisor != null) {
			return dBShardingAdvisor;
		}
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(shardingPointcut);
		advisor.setAdvice(dbShardingIntercepor);
		return dBShardingAdvisor = advisor;
	}


	/**
	 * Druid Jdbc Template
	 *
	 * @return JdbcTemplate
	 */
	@Bean
	@Order(100)
	@HasDatasourceConditon
	public JdbcTemplate jdbcTemplate(@Qualifier("mxDatasource") DataSource dataSource) {
		log.info("init JdbcTemplate");
		if (jdbcTemplate != null) {
			return jdbcTemplate;
		}
		jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	@Bean
	@Order(101)
	@HasDatasourceConditon
	public CommonDao commonDao(JdbcTemplate jdbcTemplate) {
		log.info("init CommonDao");
		if (this.commonDao != null) {
			return this.commonDao;
		}
		return this.commonDao = new CommonDao(jdbcTemplate);
	}

	/**
	 * Druid DataSource Transaction Manager
	 *
	 * @return DataSourceTransactionManager
	 */
	@Bean
	@Order(102)
	@HasDatasourceConditon
	public DataSourceTransactionManager transactionManager(@Qualifier("mxDatasource") DataSource dataSource) {
		log.info("init DataSourceTransactionManager");
		if (this.transactionManager != null) {
			return this.transactionManager;
		}
		return this.transactionManager = new DataSourceTransactionManager(dataSource);
	}

	/**
	 * Druid Named Parameter Jdbc Template
	 *
	 * @return NamedParameterJdbcTemplate
	 */
	@Bean
	@HasDatasourceConditon
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
		log.info("init NamedParameterJdbcTemplate");
		if (this.namedParameterJdbcTemplate != null) {
			return this.namedParameterJdbcTemplate;
		}
		return this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	/**
	 * 数据库并行查询线程池
	 * @return
	 */
	@Bean(name = BeanName.SHARDING_DB_TASK_EXECUTOR)
	public ThreadPoolTaskExecutor dbTaskExecutor() {
		log.info("init ThreadPoolTaskExecutor {}", taskExecutor != null);
		if (taskExecutor != null) {
			return taskExecutor;
		}
		Properties prop = new Properties();
		try {
			InputStream in = PropertyManager.class.getResourceAsStream(Constant.DB_COMMON_PROPERTIE);
			prop.load(in);
		} catch (Exception e) {
			log.error("load db common properties file error ,use default value instead", e);
		}
		String corePoolSize = prop.getProperty(Constant.DB_THREAD_POOL_CORE_POOL_SIZE_KEY,
				String.valueOf(Constant.DB_THREAD_POOL_CORE_POOL_SIZE_DEFAULT));
		String maxPoolSize = prop.getProperty(Constant.DB_THREAD_POOL_MAX_POOL_SIZE_KEY,
				String.valueOf(Constant.DB_THREAD_POOL_MAX_POOL_SIZE_DEFAULT));
		String keepAliveTime = prop.getProperty(Constant.DB_THREAD_KEEPALIVE_TIME_KEY,
				String.valueOf(Constant.DB_THREAD_KEEPALIVE_TIME_DEFAULT));
		String queueCapacity = prop.getProperty(Constant.DB_THREAD_QUEUE_CAPACITY_KEY,
				String.valueOf(Constant.DB_THREAD_QUEUE_CAPACITY_DEFAULT));
		taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(Integer.parseInt(corePoolSize));
		taskExecutor.setMaxPoolSize(Integer.parseInt(maxPoolSize));
		taskExecutor.setKeepAliveSeconds(Integer.parseInt(keepAliveTime));
		taskExecutor.setQueueCapacity(Integer.parseInt(queueCapacity));
		return taskExecutor;
	}


	/**
	 * 分表分库业务操作类拦截表达式,如果是需要分库，则需要指定分库拦截的表达式，否则不需要
	 * //在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
	 * 列如："execution(* com.haven.etools.codegen.service.*.*(..)) || execution(* com.haven.etools.codegen.service2.*.*(
	 * ..))";
	 * @see @DBShardingStrategy
	 * @return
	 */
	public abstract String getShardingAopExpress();
}
