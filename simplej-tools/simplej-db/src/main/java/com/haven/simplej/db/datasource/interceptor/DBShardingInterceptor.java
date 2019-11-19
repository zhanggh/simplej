package com.haven.simplej.db.datasource.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.db.annotation.DBShardingStrategy;
import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.annotation.Query;
import com.haven.simplej.db.annotation.ReadWriteSeparation;
import com.haven.simplej.db.constant.BeanName;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.datasource.DataSourceHolder;
import com.haven.simplej.db.datasource.ReadWriteKeyManager;
import com.haven.simplej.db.enums.ReadWriteType;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.db.model.ReadWriteDataSourceKey;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;
import com.haven.simplej.db.model.builder.DataSourceLookupKeyBuilder;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.AsyncTaskExecutor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.*;

/**
 * 水平分库数据源拦截器，支持读写分离
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
public class DBShardingInterceptor extends AbstractShardingInterceptor implements MethodInterceptor {


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		if (counter.get().get() > 0) {
			log.debug("second times into DBShardingInterceptor methodName:{}", method.getName());
			counter.get().decrementAndGet();
			return invocation.proceed();
		}
		log.debug("into DBShardingInterceptor methodName:{},into times:{}", method.getName(), counter.get().incrementAndGet());
		//数据源id
		DataSource dataSource = method.getAnnotation(DataSource.class);
		//分库注解
		DBShardingStrategy rsAnno = method.getAnnotation(DBShardingStrategy.class);
		//读写分离注解
		ReadWriteSeparation rwAnno = method.getAnnotation(ReadWriteSeparation.class);

		//查询分组注解
		Query query = method.getAnnotation(Query.class);

		Class serverClz = invocation.getThis().getClass();
		if (dataSource == null) {
			dataSource = (DataSource) serverClz.getAnnotation(DataSource.class);
			log.debug("get DataSource from class annotation,class:{}", DataSource.class.getName());
		}

		if (rsAnno == null) {
			//方法级别没有具体定义@RepositorySharding的时候，则判断类级别是否有配置默认的分库策略
			rsAnno = (DBShardingStrategy) serverClz.getAnnotation(DBShardingStrategy.class);
			log.debug("get DBShardingStrategy from class annotation,class:{}", DBShardingStrategy.class.getName());

		}

		if (rwAnno == null) {
			//方法级别没有具体定义@RepositorySharding的时候，则判断类级别是否有配置默认的分库策略
			rwAnno = (ReadWriteSeparation) serverClz.getAnnotation(ReadWriteSeparation.class);
			log.debug("get DBShardingStrategy from class annotation,class:{}", DBShardingStrategy.class.getName());

		}


		//如果是特定的查询
		if (query != null) {
			if (StringUtil.isEmpty(query.dbNameExpression())) {
				return parallelQuery(invocation, query.groupName());
			} else {
				String[] dbNames = DBUtil.parserSchemas(query.dbNameExpression());
				String[] tables = null;
				if (StringUtil.isNotEmpty(query.tableExpression())) {
					tables = new String[]{query.tableExpression()};
					if (StringUtil.contains(query.tableExpression(), Constant.SCHEMA_PARAM_SYMBOL)) {
						tables = DBUtil.parserSchemas(query.tableExpression());
					}
				}
				return parallelQuery(invocation, query.groupName(), dbNames, tables, query.isMaster());
			}

		}

		if (rsAnno != null) {
			if (StringUtils.isBlank(rsAnno.strategyBeanName())) {
				throw new UncheckedException("strategyBeanName must not be empty");
			}
			if (StringUtils.isNoneBlank(rsAnno.strategyBeanName())) {
				com.haven.simplej.db.datasource.sharding.strategy.DBShardingStrategy strategy = SpringContext.getBean(rsAnno.strategyBeanName());
				if (strategy == null) {
					throw new UncheckedException("if use custom strategyBeanName,you must set the expression");
				}

				if (StringUtils.isBlank(rsAnno.expression())) {
					throw new UncheckedException("if use custom strategyBeanName,you must set the expression");
				}
				Object[] args = invocation.getArguments();
				// 参数名
				ShardingParameter parameter = getShardingParameter(method, args, rsAnno.strategyBeanName(), rsAnno.expression());
				StrategyKey key = strategy.getDataSourceLookupKey(parameter);
				log.debug("db sharding strategyBeanName key:{}", key);
				if (key != null && StringUtils.isEmpty(key.getDbName())) {
					//cannot find dbName
					if (rwAnno == null || rwAnno.type().equals(ReadWriteType.WRITE)) {
						throw new UncheckedException("cannot find dbName,please check if the partition field value is empty");
					}
					//parallel query all datasource of the group
					return parallelQuery(invocation, key.getGroupName());
				} else {
					DataSourceHolder.setCurrentDbName(key.getDbName());
					setKey(key, rwAnno);
				}

			} else {
				log.info("there is no DBShardingStrategy for method:{} ,use default key:{} instead", method.getName(), DataSourceHolder.getDefaultKey());
				DataSourceHolder.putKey(DataSourceHolder.getDefaultKey());
			}
		} else if (dataSource != null && StringUtils.isNoneBlank(dataSource.id())) {
			//默认是主库
			boolean useMaster = rwAnno == null || rwAnno.isMaster();
			setKey(DBUtil.getDatasourceId(dataSource.id()), dataSource.groupName(), useMaster);
		} else if (StringUtils.isNoneBlank(dataSource.dbName())) {
			//ReadWriteKeyManager
			boolean useMaster = rwAnno == null || rwAnno.isMaster();
			setKey(dataSource.dbName(), dataSource.groupName(), useMaster);
		} else {
			log.info("there is no DBShardingStrategy for method:{} ,use default key:{} instead", method.getName(), DataSourceHolder.getDefaultKey());
			DataSourceHolder.putKey(DataSourceHolder.getDefaultKey());
		}

		Object result;
		try {
			result = invocation.proceed();
		} finally {
			log.debug("out DBShardingInterceptor and remove lookupKey:{}", DataSourceHolder.getKey());
			DataSourceHolder.clear();
		}
		return result;
	}

	private void setKey(String dbName, String groupName, boolean isMaster) {
		if (Constant.DEFAULT_DATA_SOURCE_GROUP_NAME.equalsIgnoreCase(groupName)) {
			String key = dbName + "_";
			if (isMaster) {
				key += Constant.MASTER_FLAG;
			} else {
				key += Constant.SLAVER_FLAG;
			}
			groupName = ReadWriteKeyManager.getDbNameGroupMap().get(key);
		}
		StrategyKey key = new StrategyKey(dbName, groupName);
		//设置当前线程获取dataSource的key
		if (key == null) {
			DataSourceHolder.putKey(DataSourceHolder.getDefaultKey());
		}
		//如果当前注解的方法是读操作，非master时，只有读,否则可读写
		DataSourceHolder.putKey(DataSourceLookupKeyBuilder.build(key, isMaster));
		DataSourceHolder.setCurrentDbName(dbName);
	}

	private void setKey(StrategyKey key, ReadWriteSeparation rwAnno) {
		//设置当前线程获取dataSource的key
		if (key == null) {
			DataSourceHolder.putKey(DataSourceHolder.getDefaultKey());
		} else if (rwAnno != null && !rwAnno.isMaster()) {
			//如果当前注解的方法是读操作，非master时，只有读
			DataSourceHolder.putKey(DataSourceLookupKeyBuilder.build(key, false));
		} else {
			//如果当前注解的方法是写操作
			DataSourceHolder.putKey(DataSourceLookupKeyBuilder.build(key, true));
		}
	}

	/**
	 * 并行查询,查询范围：特定的groupName下的数据源
	 * @param invocation
	 * @return
	 */
	private Object parallelQuery(MethodInvocation invocation, String groupName) {
		log.debug("parallelQuery ,methodName:{} ,groupName:{}", invocation.getMethod().getName(), groupName);
		AsyncTaskExecutor taskExecutor = SpringContext.getBean(BeanName.SHARDING_DB_TASK_EXECUTOR);
		List<ReadWriteDataSourceKey> keys = ReadWriteKeyManager.getKeyManager().get(groupName);
		// parallel count
		List<Future<Object>> futures = Lists.newArrayList();
		for (ReadWriteDataSourceKey key : keys) {
			DataSourceLookupKey lookupKey = key.getReadKey();
			if (lookupKey == null) {
				lookupKey = key.getWriteKey();
			}
			if (lookupKey == null) {
				log.warn("readkey and writeKey are all null");
				continue;
			}
			if (StringUtils.equalsIgnoreCase(lookupKey.getDbName(), Constant.DATA_SOURCE_DEFAULT_DB_NAME)) {
				continue;
			}
			if (StringUtils.startsWith(lookupKey.getDbName(), Constant.DATA_SOURCE_ID)) {
				continue;
			}

			Future<Object> future = taskExecutor.submit(new QueryWorker(invocation, lookupKey, null));
			futures.add(future);
			log.debug("--------------dbTaskExecutor.submit---------------");
		}

		return getFutureResult(invocation, futures);
	}

	/**
	 * 并行查询,查询范围：特定的groupName下的数据源
	 * @param invocation
	 * @return
	 */
	private Object parallelQuery(MethodInvocation invocation, String groupName, String[] dbName, String[] tables,
			boolean isMaster) {
		log.debug("parallelQuery ,methodName:{} ,groupName:{},dbName:{},tables:{}", invocation.getMethod().getName(), groupName, JSON.toJSONString(dbName), JSON.toJSONString(tables));
		AsyncTaskExecutor taskExecutor = SpringContext.getBean(BeanName.SHARDING_DB_TASK_EXECUTOR);
		// parallel count
		List<Future<Object>> futures = Lists.newArrayList();
		for (String s : dbName) {
			StrategyKey key = new StrategyKey(s, groupName);
			DataSourceLookupKey lookupKey = DataSourceLookupKeyBuilder.build(key, isMaster);
			if (tables != null) {
				for (String table : tables) {
					Future<Object> future = taskExecutor.submit(new QueryWorker(invocation, lookupKey, table));
					futures.add(future);
				}
			} else {
				Future<Object> future = taskExecutor.submit(new QueryWorker(invocation, lookupKey, null));
				futures.add(future);
			}

		}
		return getFutureResult(invocation, futures);
	}


	private class QueryWorker implements Callable<Object> {

		private MethodInvocation invocation;
		private DataSourceLookupKey key;
		private String table;

		public QueryWorker(MethodInvocation invocation, DataSourceLookupKey key, String table) {
			this.invocation = invocation;
			this.key = key;
			this.table = table;
		}

		@Override
		public Object call() throws Exception {
			Object result = null;
			try {
				DataSourceHolder.putKey(key);
				DataSourceHolder.setCurrentDbName(key.getDbName());
				DataSourceHolder.setCurrentTableName(table);
				result = invocation.proceed();
			} catch (Throwable throwable) {
				throw new Exception(throwable);
			} finally {
				DataSourceHolder.clear();
			}
			return result;
		}
	}
}
