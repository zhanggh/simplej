package com.haven.simplej.rpc.config.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.rpc.config.model.ConfigItemInstanceModel;
import com.haven.simplej.rpc.config.service.ConfigItemInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * 配置项生效的服务对应的实例 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_config")
public class ConfigItemInstanceServiceImpl extends BaseServiceImpl2<ConfigItemInstanceModel> implements ConfigItemInstanceService {

}