package com.haven.simplej.security.service.impl;

import com.haven.simplej.security.model.AppKeyRefModel;
import com.haven.simplej.security.service.AppKeyRefService;
import org.springframework.stereotype.Service;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 操作日志表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class AppKeyRefServiceImpl extends BaseServiceImpl2<AppKeyRefModel> implements AppKeyRefService {


}