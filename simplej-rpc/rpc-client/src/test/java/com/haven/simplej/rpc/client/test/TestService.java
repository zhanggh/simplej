package com.haven.simplej.rpc.client.test;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/24 22:39
 * @version 1.0
 */
public class TestService {

	public String test1(String param) {
		System.out.println("test1 request param:" + param);

		return "success";
	}

	public String test1(String param, String param2) {
		System.out.println("test1 request param1:" + param + ",param2:" + param2);

		return "success";
	}

	public String test1(String param, List<String> list) {
		System.out.println("test1 request param1:" + param + ",param2:" + JSON.toJSONString(list, true));

		return "success";
	}

	public String test1() {
		System.out.println("test1 request no param1");

		return "success";
	}


}
