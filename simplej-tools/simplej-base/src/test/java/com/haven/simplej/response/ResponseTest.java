package com.haven.simplej.response;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.haven.simplej.response.model.*;

/**
 * Created by haven.zhang on 2019/1/8.
 */
public class ResponseTest {
	public static void main(String[] args)
			throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

		JsonResponse resp = new JsonResponse<>();
		JsonResponse resp2 = new JsonResponse<>();

		UserInfo user = new UserInfo("zhangsan",30);
		resp2.setMsg(user);
		resp2.setRespCode("success");
		resp2.setRespMsg("0000");
		System.out.println(JSON.toJSONString(resp2));


		PageInfo page = new PageInfo<>(10000,2,200);
		List<UserInfo> list = Lists.newArrayList();
		list.add(user);
		page.setData(list);
		resp.setMsg(page);

		System.out.println(JSON.toJSONString(resp));

		Constructor con = PageInfo.class.getConstructor(long.class,int.class,int.class);
		PageInfo page2 = (PageInfo) con.newInstance(100,2,20);
		page2.setCount(222l);
		System.out.println(JSON.toJSONString(page2));
	}
}
