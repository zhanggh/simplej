package com.haven.epay.payment;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.authen.model.GroupInfoModel;
import com.haven.simplej.authen.service.GroupInfoService;
import com.haven.simplej.authen.service.OptLogService;
import com.haven.simplej.db.dao.CommonDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author haven.zhang
 * @date 2019/1/25.
 */
@Component
@Slf4j
public class ApplicationListener implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private CommonDao dao;

	@Autowired
	private GroupInfoService service;
	@Autowired
	private OptLogService optLogService;


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("--------------------onApplicationEvent--------------------");
		GroupInfoModel model = new GroupInfoModel();
		model.setId(1l);
//		log.info("group info:{}",JSON.toJSONString(service.get(model)));
// 		log.info("group2 info:{}",JSON.toJSONString(service.getModel(1l)));



		model.setId(2l);
//		model.setCreateBy("2342523");
//		service.add(model);
		log.info("-------------finish--------------");
	}
}
