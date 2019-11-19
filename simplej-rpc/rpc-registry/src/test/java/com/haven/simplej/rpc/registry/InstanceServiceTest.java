package com.haven.simplej.rpc.registry;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;
import com.haven.simplej.rpc.registry.service.InstanceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/5/4 23:51
 * @version 1.0
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = StartUpApplication.class)
public class InstanceServiceTest {

	@Autowired
	private InstanceInfoService instanceInfoService;

	@Test
	public void queryInstanceTest() {
		List<InstanceInfoModel> instanceInfoModels = instanceInfoService.getInstanceGroup("com.haven.simplej.rpc");
		System.out.println(JSON.toJSONString(instanceInfoModels, true));
	}


}
