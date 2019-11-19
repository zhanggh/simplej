package com.haven.simplej.rpc.config.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rpc.config.model.ConfigItemModel;
import com.haven.simplej.rpc.config.service.ConfigItemService;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 属性配置信息 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_config")
public class ConfigItemServiceImpl extends BaseServiceImpl2<ConfigItemModel> implements ConfigItemService {

}