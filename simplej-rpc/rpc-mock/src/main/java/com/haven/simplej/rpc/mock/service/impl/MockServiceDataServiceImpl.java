package com.haven.simplej.rpc.mock.service.impl;

import com.haven.simplej.rpc.mock.model.MockServiceDataModel;
import com.haven.simplej.rpc.mock.service.MockServiceDataService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 远程服务方法模拟结果信息 Service implements
 */
@Slf4j
@Service
public class MockServiceDataServiceImpl extends BaseServiceImpl2<MockServiceDataModel> implements
		MockServiceDataService {

}