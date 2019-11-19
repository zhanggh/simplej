package com.haven.simplej.rpc.config.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.rpc.config.model.ConfigItemHistoryModel;
import com.haven.simplej.rpc.config.service.ConfigItemHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 属性配置信息日志 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_config")
public class ConfigItemHistoryServiceImpl extends BaseServiceImpl2<ConfigItemHistoryModel> implements ConfigItemHistoryService {

}