package com.haven.simplej.db.datasource.interceptor;

import com.google.common.collect.Lists;
import com.haven.simplej.db.annotation.TableShardingStrategy;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.constant.BeanName;
import com.haven.simplej.db.datasource.DataSourceHolder;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.task.AsyncTaskExecutor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 水平分表源拦截器
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
public class TableShardingInterceptor extends AbstractShardingInterceptor implements MethodInterceptor {

	public TableShardingInterceptor() {
		log.info("create TableShardingInterceptor");
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Method method = invocation.getMethod();
		if (counter.get().get() > 0) {
			log.debug("second times into TableShardingInterceptor methodName:{}", method.getName());
			counter.get().decrementAndGet();
			return invocation.proceed();
		}
		log.debug("into TableShardingInterceptor methodName:{}", method.getName());

		TableShardingStrategy tsAnno = method.getAnnotation(TableShardingStrategy.class);
		if (tsAnno != null) {
			com.haven.simplej.db.datasource.sharding.strategy.TableShardingStrategy strategy = SpringContext.getBean(tsAnno.strategyBeanName());
			Object[] args = invocation.getArguments();
			// 参数名
			ShardingParameter parameter = getShardingParameter(method, args, tsAnno.strategyBeanName(), tsAnno.expression());
			String tableName = strategy.getTableName(parameter);
			for (Object arg : args) {
				if (arg instanceof BaseDomain) {
					BaseDomain obj = (BaseDomain) arg;
					obj.setTableName(tableName);
				}
			}
			DataSourceHolder.setCurrentTableName(tableName);
		}

		Object result = invocation.proceed();
		log.debug("out TableShardingInterceptor lookupKey:{}", DataSourceHolder.getKey());
		return result;
	}


	/**
	 * 并行查询,查询范围：特定的groupName下的数据源
	 * @param invocation
	 * @return
	 */
	private Object parallelQuery(MethodInvocation invocation, String[] tableNames) {
		log.debug("parallelQuery ,methodName:{} ,tableNames:{} ", invocation.getMethod().getName(), tableNames);
		AsyncTaskExecutor taskExecutor = SpringContext.getBean(BeanName.SHARDING_DB_TASK_EXECUTOR);
		// parallel count
		List<Future<Object>> futures = Lists.newArrayList();
		for (String s : tableNames) {

			Future<Object> future = taskExecutor.submit(new QueryWorker(invocation, s));
			futures.add(future);
		}
		return getFutureResult(invocation, futures);
	}


	private class QueryWorker implements Callable<Object> {

		private MethodInvocation invocation;
		private String tableName;

		public QueryWorker(MethodInvocation invocation, String tableName) {
			this.invocation = invocation;
			this.tableName = tableName;
		}

		@Override
		public Object call() throws Exception {
			Object result = null;
			try {
				DataSourceHolder.setCurrentTableName(tableName);
				result = invocation.proceed();
			} catch (Throwable throwable) {
				throw new Exception(throwable);
			}
			return result;
		}
	}
}
