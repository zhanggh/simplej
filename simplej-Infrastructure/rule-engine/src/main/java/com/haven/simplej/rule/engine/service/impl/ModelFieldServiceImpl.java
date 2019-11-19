package com.haven.simplej.rule.engine.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rule.engine.model.ModelFieldModel;
import com.haven.simplej.rule.engine.service.ModelFieldService;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 模型字段信息，与model_info表是多对一关系 Service implements
 */
@Slf4j
@Service

public class ModelFieldServiceImpl extends BaseServiceImpl2<ModelFieldModel> implements ModelFieldService {


}