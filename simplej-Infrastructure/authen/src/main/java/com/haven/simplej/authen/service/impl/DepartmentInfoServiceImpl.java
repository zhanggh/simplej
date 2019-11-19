package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.DepartmentInfoModel;
import com.haven.simplej.authen.service.DepartmentInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 部门信息 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class DepartmentInfoServiceImpl extends BaseServiceImpl2<DepartmentInfoModel> implements DepartmentInfoService {


}