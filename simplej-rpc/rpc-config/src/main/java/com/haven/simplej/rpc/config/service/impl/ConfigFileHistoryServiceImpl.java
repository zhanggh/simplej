package com.haven.simplej.rpc.config.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rpc.config.model.ConfigFileHistoryModel;
import com.haven.simplej.rpc.config.service.ConfigFileHistoryService;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 属性文件信息修改记录日志 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_config")
public class ConfigFileHistoryServiceImpl extends BaseServiceImpl2<ConfigFileHistoryModel> implements ConfigFileHistoryService {

}