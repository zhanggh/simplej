package com.haven.simplej.bean;

import com.alibaba.fastjson.JSON;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import org.junit.Test;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author: havenzhang
 * @date: 2019/5/15 21:50
 * @version 1.0
 */
public class BeanUtilTest {

	@Test
	public void test() {
		BeanUtil.getFields(Person.class);
	}


	@Test
	public void getParamNameTest() {
		DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
		Method method = ReflectionUtil.getMethod(Person.class, "sayHello", String.class, Person.class);
		Method method2 = ReflectionUtil.getMethod(Person.class, "sayHello", String.class, Person.class);
		String[] names = discover.getParameterNames(method);
		System.out.println(JSON.toJSONString(names));
		System.out.println(method.equals(method2));
	}
}
