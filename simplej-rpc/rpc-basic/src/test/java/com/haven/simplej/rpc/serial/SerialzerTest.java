package com.haven.simplej.rpc.serial;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/27 23:06
 * @version 1.0
 */
public class SerialzerTest {


	@Test
	public void test(){
		List<String> list = Lists.newArrayList();
		list.add("111");
		list.add("222");
		list.add("33");

		String[] params = new String[]{"zhangsan","wangwu","lisi"};

		System.out.println(JSON.toJSONString(list));
		System.out.println(JSON.toJSONString(params));
	}
}
