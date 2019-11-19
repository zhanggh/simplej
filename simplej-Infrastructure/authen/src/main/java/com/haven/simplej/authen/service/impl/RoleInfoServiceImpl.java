package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.RoleInfoModel;
import com.haven.simplej.authen.service.RoleInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 角色信息表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class RoleInfoServiceImpl extends BaseServiceImpl2<RoleInfoModel> implements RoleInfoService {


}