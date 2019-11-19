package com.haven.simplej.db.test;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.db.test.service.ComService;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/3 23:39
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TransactionTest {

	@Autowired
	private ComService service;

	@Autowired
	private SpringContext context;

	@Autowired
	private PropertyManager propertyManager;

	@Test
	public void test() {
		service.update();
	}

	@Test
	public void test2() {
		List list = service.query();
		System.out.println(JSON.toJSONString(list, true));
	}

	@Test
	public void test3() {
		List list = service.get();
		System.out.println(JSON.toJSONString(list, true));
	}

	@Test
	public void test4() {
		int ret = service.save("zhangsanxxx1");
		System.out.println("save result : " + ret);
	}

}
