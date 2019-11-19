package com.haven.simplej.proxytest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Date;

/**
 * @author: havenzhang
 * @date: 2019/5/18 22:56
 * @version 1.0
 */
public class JsonTest {

	@Test
	public void test() {
		String json = "{'person':{'name':'zhangsan','age':29,'height':173.5,'time':" + new Date().getTime() + ",'birthday':" + new Date().getTime() + "}}";
		JSONObject jsonObject = JSON.parseObject(json);
		System.out.println(jsonObject.get("hello"));
		System.out.println(jsonObject.getJSONObject("time"));
		System.out.println(jsonObject.get("time"));
		//		System.out.println(jsonObject.getObject("age",Integer.class)==29);
		Person person = jsonObject.getObject("person", Person.class);
		System.out.println(person.getAge());
	}
}
