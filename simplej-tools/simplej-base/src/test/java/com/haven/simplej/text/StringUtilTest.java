package com.haven.simplej.text;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/10/2 19:14
 * @version 1.0
 */
public class StringUtilTest {

	@Test
	public void test1() {
		Map<String, String> params = Maps.newHashMap();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");
		System.out.println(StringUtil.convert2QueryString(params));
	}

	@Test
	public void test2() {
		String queryString = "key1=value1&key2=value2&key3=value3&ke5=";
		Map<String, String> params = StringUtil.parse2Map(queryString);
		params.forEach((k, v) -> System.out.println("key:" + k + ",value:" + v));
	}

	@Test
	public void test3(){
		String str = "hello.zhang";
		String key = StringUtil.substringAfter(str, "hello.");
		System.out.println(key);
	}

	@Test
	public void test4(){
		int a=1288845;
		byte[] bytes = StringUtil.Int2ByteArray(a);
		System.out.println(Arrays.toString(bytes));

		System.out.println(StringUtil.ByteArray2Int(bytes));
	}
}
