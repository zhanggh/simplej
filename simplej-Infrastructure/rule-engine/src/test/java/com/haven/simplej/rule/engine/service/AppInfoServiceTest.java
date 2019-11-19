package com.haven.simplej.rule.engine.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.rule.engine.StartUpApplication;
import com.haven.simplej.rule.engine.model.AppInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * AppInfoService单元测试类
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class AppInfoServiceTest {

	@Autowired
	private AppInfoService service;


	@Test
	public void getTest(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.get(model),true));
	}

	@Test
	public void count(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.count(model), true));
	}

	@Test
	public void query(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.query(model), true));
	}

	@Test
	public void  search(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.search(model), true));
	}

	@Test
	public void create(){
		AppInfoModel model = new AppInfoModel();
//		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		model.setAccessKey("2423525245");
		model.setName("test_app");
		model.setAppCode("10000000001");
		model.setDescription("描述");
		System.out.println(JSON.toJSONString(service.create(model), true));
	}

	@Test
	public void batchInsert(){
		List<AppInfoModel> list = Lists.newArrayList();
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchInsert(list), true));
	}

	@Test
	public void  update(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		System.out.println(JSON.toJSONString(service.update(model), true));
	}

	@Test
	public void  batchUpdate(){
		List<AppInfoModel> list = Lists.newArrayList();
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchUpdate(list, new String[]{"id"}), true));
	}

	@Test
	public void  remove(){
		AppInfoModel model = new AppInfoModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.remove(model), true));
	}
}
