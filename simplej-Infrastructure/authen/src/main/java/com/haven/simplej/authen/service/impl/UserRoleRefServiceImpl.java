package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.UserRoleRefModel;
import com.haven.simplej.authen.service.UserRoleRefService;
import com.haven.simplej.db.annotation.RepositorySharding;
import com.haven.simplej.db.base.BaseServiceImpl2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 用户角色关联表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class UserRoleRefServiceImpl extends BaseServiceImpl2<UserRoleRefModel> implements UserRoleRefService {


}