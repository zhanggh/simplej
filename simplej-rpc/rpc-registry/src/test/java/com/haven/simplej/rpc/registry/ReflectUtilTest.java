package com.haven.simplej.rpc.registry;

import com.haven.simplej.rpc.registry.strategy.RegisterMysqlImpl;
import com.haven.simplej.rpc.util.ReflectUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/10/8 22:06
 * @version 1.0
 */
public class ReflectUtilTest {


	@Test
	public void test1(){

		Method method = ReflectUtil.getMethod(RegisterMysqlImpl.class,"heartbeat", List.class);

		System.out.println(method.getName());

		List<Type[]> list = ReflectUtil.getParamGenericType(method);

		System.out.println(list);
	}

}
