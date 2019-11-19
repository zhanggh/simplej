package com.haven.simplej.rule.engine.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.rule.engine.StartUpApplication;
import com.haven.simplej.rule.engine.model.PluginItemModel;
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
 * PluginItemService单元测试类
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class PluginItemServiceTest {

	@Autowired
	private PluginItemService service;


	@Test
	public void getTest(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.get(model),true));
	}

	@Test
	public void count(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.count(model), true));
	}

	@Test
	public void query(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.query(model), true));
	}

	@Test
	public void  search(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.search(model), true));
	}

	@Test
	public void create(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		System.out.println(JSON.toJSONString(service.create(model), true));
	}

	@Test
	public void batchInsert(){
		List<PluginItemModel> list = Lists.newArrayList();
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchInsert(list), true));
	}

	@Test
	public void  update(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		System.out.println(JSON.toJSONString(service.update(model), true));
	}

	@Test
	public void  batchUpdate(){
		List<PluginItemModel> list = Lists.newArrayList();
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchUpdate(list, new String[]{"id"}), true));
	}

	@Test
	public void  remove(){
		PluginItemModel model = new PluginItemModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.remove(model), true));
	}
}
