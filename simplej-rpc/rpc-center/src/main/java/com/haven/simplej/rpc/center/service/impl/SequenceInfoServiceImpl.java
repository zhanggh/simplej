package com.haven.simplej.rpc.center.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.rpc.center.model.SequenceInfoModel;
import com.haven.simplej.rpc.center.service.SequenceInfoService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
/**
 * 序列号生成规则信息 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_center")
public class SequenceInfoServiceImpl extends BaseServiceImpl2<SequenceInfoModel> implements SequenceInfoService {

}