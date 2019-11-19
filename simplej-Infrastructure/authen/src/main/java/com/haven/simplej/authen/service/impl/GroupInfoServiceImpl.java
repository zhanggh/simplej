package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.GroupInfoModel;
import com.haven.simplej.authen.service.GroupInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 小组信息，一个部门下面对应多个小组 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class GroupInfoServiceImpl extends BaseServiceImpl2<GroupInfoModel> implements GroupInfoService {


}