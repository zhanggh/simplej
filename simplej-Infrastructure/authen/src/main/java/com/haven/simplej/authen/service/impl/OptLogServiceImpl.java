package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.OptLogModel;
import com.haven.simplej.authen.service.OptLogService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 操作日志表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class OptLogServiceImpl extends BaseServiceImpl2<OptLogModel> implements OptLogService {


}