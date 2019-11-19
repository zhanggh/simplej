package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.UserInfoModel;
import com.haven.simplej.authen.service.UserInfoService;
import com.haven.simplej.db.annotation.RepositorySharding;
import com.haven.simplej.db.base.BaseServiceImpl2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 用户信息表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class UserInfoServiceImpl extends BaseServiceImpl2<UserInfoModel> implements UserInfoService {


}