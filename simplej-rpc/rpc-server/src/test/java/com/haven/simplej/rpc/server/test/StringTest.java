package com.haven.simplej.rpc.server.test;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.text.StringUtil;

/**
 * @author: havenzhang
 * @date: 2019/9/26 22:40
 * @version 1.0
 */
public class StringTest {

	public static void main(String[] args) {
		String paramName = "map.key1";
		String[] params = StringUtil.split(paramName, ".");
		System.out.println(JSON.toJSONString(params));
		System.out.println(params[1].substring(StringUtil.length(params[1]) - 1));


		String[] a = new String[]{"1", "2"};
		if (a.getClass().isArray()) {
			System.out.println("yy");
		}

		int[] b = {1,2};
		float[] d = {1,2};
		Integer[] c = {1,2};
		System.out.println(c.getClass().getName().equals("[Ljava.lang.Integer;"));
		System.out.println(d.getClass().getName());
		if (b.getClass().isArray()) {
			System.out.println(b.getClass().getName());
		}
	}
}
