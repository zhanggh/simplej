package com.haven.simplej.rpc.config.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.rpc.config.model.ConfigFileModel;
import com.haven.simplej.rpc.config.service.ConfigFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 属性文件信息 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_config")
public class ConfigFileServiceImpl extends BaseServiceImpl2<ConfigFileModel> implements ConfigFileService {

}