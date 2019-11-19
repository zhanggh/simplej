package com.haven.simplej.security.service.impl;

import com.haven.simplej.security.model.KeyInfoModel;
import com.haven.simplej.security.service.KeyInfoService;
import org.springframework.stereotype.Service;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 秘钥信息表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class KeyInfoServiceImpl extends BaseServiceImpl2<KeyInfoModel> implements KeyInfoService {


}