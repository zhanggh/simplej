package com.haven.simplej.authen.service.impl;

import com.haven.simplej.authen.model.MenuInfoModel;
import com.haven.simplej.authen.service.MenuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.haven.simplej.db.annotation.RepositorySharding;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 菜单信息表 Service implements
 */
@Slf4j
@Service
@RepositorySharding(dbName = "authen")
public class MenuInfoServiceImpl extends BaseServiceImpl2<MenuInfoModel> implements MenuInfoService {


}