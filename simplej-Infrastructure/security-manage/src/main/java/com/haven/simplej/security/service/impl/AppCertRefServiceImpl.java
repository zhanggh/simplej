package com.haven.simplej.security.service.impl;

import com.haven.simplej.db.annotation.RepositorySharding;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.security.model.AppCertRefModel;
import com.haven.simplej.security.service.AppCertRefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 接入方对应的证书关联管理表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class AppCertRefServiceImpl extends BaseServiceImpl2<AppCertRefModel> implements AppCertRefService {


}