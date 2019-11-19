package com.haven.simplej.db.datasource.interceptor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: havenzhang
 * @date: 2019/9/5 17:31
 * @version 1.0
 */
@Slf4j
public class AbstractShardingInterceptor {
	protected ParameterNameDiscoverer paraNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	/**
	 * 避免同一方法重复进入key计算逻辑
	 */
	protected ThreadLocal<AtomicInteger> counter = ThreadLocal.withInitial(() -> new AtomicInteger(0));

	protected ShardingParameter getShardingParameter(Method method,Object[] args,String strategy,String expression){
		// 参数名
		String[] paraNames = paraNameDiscoverer.getParameterNames(method);
		ShardingParameter parameter = new ShardingParameter();
		parameter.setName(strategy);
		parameter.setValue(DBUtil.getSpelValue(args, paraNames, expression, SpringContext.getContext()));
		return parameter;
	}

	/**
	 * 提交查询子任务
	 * @param invocation invocation
	 * @param futures 任务
	 * @return Object 查询结果
	 */
	protected Object getFutureResult(MethodInvocation invocation, List<Future<Object>> futures) {
		Object resp = null;
		List<Object> objs = Lists.newArrayList();
		long queryTimeout = PropertyManager.getLong(Constant.DB_THREAD_QUERY_TIMEOUT, 20000);
		for (Future<Object> future : futures) {
			try {
				Object obj = future.get(queryTimeout, TimeUnit.MILLISECONDS);
				objs.add(obj);
			} catch (Exception e) {
				log.error("parallel query error", e);
			}
		}
		Class returnType = invocation.getMethod().getReturnType();
		if (returnType.equals(List.class)) {
			List list = Lists.newArrayList();
			for (Object obj : objs) {
				List tempList = (List) obj;
				list.addAll(tempList);
			}
			resp = list;
		} else if (returnType.equals(Map.class)) {
			Map map = Maps.newHashMap();
			for (Object obj : objs) {
				Map temMap = (Map) obj;
				map.putAll(temMap);
			}
			resp = map;
		} else {
			for (Object obj : objs) {
				if (obj != null) {
					resp = obj;
					break;
				}
			}
		}
		return resp;
	}
}
