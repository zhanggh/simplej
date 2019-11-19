package com.haven.simplej.db.sql.interceptor;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.db.sql.log.SlowQuerySqlLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * mybatis 组件的SQL 拦截器，用于收集SQL执行的效率
 * @author: havenzhang
 * @date: 2018/9/29 10:29
 * @version 1.0
 */
@Slf4j
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})})
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
		@Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
		@Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MybatisSqlInterceptor implements Interceptor {

	public MybatisSqlInterceptor() {
		log.debug("--------------MybatisSqlInterceptor---------------");
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		long startTime = System.currentTimeMillis();
		StatementHandler statementHandler = (StatementHandler) target;
		Object result;
		try {
			result = invocation.proceed();
		} finally {
			long endTime = System.currentTimeMillis();
			long timeCount = endTime - startTime;
			BoundSql boundSql = statementHandler.getBoundSql();
			String sql = boundSql.getSql();
			Object parameterObject = boundSql.getParameterObject();
			List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
			log.debug("执行 SQL：[{} ]执行耗时[ {} ms]", sql, timeCount);
			log.debug("parameterObject：{}", JSON.toJSONString(parameterObject, true));
			SlowQuerySqlLogger.log(sql, new Object[]{JSON.toJSONString(parameterObject)}, timeCount);
		}

		return result;

	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		log.debug("properties:{}", JSON.toJSONString(properties, true));
	}
}
