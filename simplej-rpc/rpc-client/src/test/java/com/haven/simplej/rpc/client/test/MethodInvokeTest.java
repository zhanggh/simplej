package com.haven.simplej.rpc.client.test;

import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/24 22:42
 * @version 1.0
 */
public class MethodInvokeTest {


	public static void main(String[] args) {
		TestService service = new TestService();
		List params = Lists.newArrayList();
		params.add("1111");
		List params2 = Lists.newArrayList();
		params2.add("2525252");
		params.add(params2);
		Object resp = ReflectionUtil.invokeMethod(service, "test1", params.toArray());

		System.out.println("invoke resp:" + resp);
	}
}
