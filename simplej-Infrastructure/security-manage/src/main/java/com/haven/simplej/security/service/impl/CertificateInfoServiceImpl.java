package com.haven.simplej.security.service.impl;

import com.haven.simplej.security.model.CertificateInfoModel;
import com.haven.simplej.security.service.CertificateInfoService;
import org.springframework.stereotype.Service;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 接入方对应的key关联管理表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class CertificateInfoServiceImpl extends BaseServiceImpl2<CertificateInfoModel> implements
		CertificateInfoService {


}