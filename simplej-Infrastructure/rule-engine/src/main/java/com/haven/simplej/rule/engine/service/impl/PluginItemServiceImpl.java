package com.haven.simplej.rule.engine.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rule.engine.model.PluginItemModel;
import com.haven.simplej.rule.engine.service.PluginItemService;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 插件信息，注意，一个插件仅提供一个方法 Service implements
 */
@Slf4j
@Service

public class PluginItemServiceImpl extends BaseServiceImpl2<PluginItemModel> implements PluginItemService {


}