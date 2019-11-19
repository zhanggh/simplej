package com.haven.simplej.rule.engine.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.rule.engine.StartUpApplication;
import com.haven.simplej.rule.engine.model.ModelInfoModel;
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
 * ModelInfoService单元测试类
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class ModelInfoServiceTest {

	@Autowired
	private ModelInfoService service;


	@Test
	public void getTest(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.get(model),true));
	}

	@Test
	public void count(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.count(model), true));
	}

	@Test
	public void query(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.query(model), true));
	}

	@Test
	public void  search(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);

		System.out.println(JSON.toJSONString(service.search(model), true));
	}

	@Test
	public void create(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		System.out.println(JSON.toJSONString(service.create(model), true));
	}

	@Test
	public void batchInsert(){
		List<ModelInfoModel> list = Lists.newArrayList();
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchInsert(list), true));
	}

	@Test
	public void  update(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		System.out.println(JSON.toJSONString(service.update(model), true));
	}

	@Test
	public void  batchUpdate(){
		List<ModelInfoModel> list = Lists.newArrayList();
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		model.setCreateTime(new Timestamp(new Date().getTime()));
		model.setCreatedBy("test");
		list.add(model);
		System.out.println(JSON.toJSONString(service.batchUpdate(list, new String[]{"id"}), true));
	}

	@Test
	public void  remove(){
		ModelInfoModel model = new ModelInfoModel();
		model.setId(1L);
		System.out.println(JSON.toJSONString(service.remove(model), true));
	}
}
