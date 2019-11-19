package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.SequenceInfoModel;
import com.haven.simplej.authen.service.SequenceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 序列号生成器表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class SequenceInfoServiceImpl extends BaseServiceImpl2<SequenceInfoModel> implements SequenceInfoService {


}