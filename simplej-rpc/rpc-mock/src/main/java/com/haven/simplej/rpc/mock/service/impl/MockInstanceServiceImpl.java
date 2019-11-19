package com.haven.simplej.rpc.mock.service.impl;

import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.rpc.mock.model.MockInstanceModel;
import com.haven.simplej.rpc.mock.service.MockInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * mock服务对应的实例 Service implements
 */
@Slf4j
@Service
public class MockInstanceServiceImpl extends BaseServiceImpl2<MockInstanceModel> implements MockInstanceService {

}