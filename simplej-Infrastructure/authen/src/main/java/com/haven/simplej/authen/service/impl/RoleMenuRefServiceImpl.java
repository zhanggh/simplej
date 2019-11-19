package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.RoleMenuRefModel;
import com.haven.simplej.authen.service.RoleMenuRefService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 角色与菜单关联表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class RoleMenuRefServiceImpl extends BaseServiceImpl2<RoleMenuRefModel> implements RoleMenuRefService {


}