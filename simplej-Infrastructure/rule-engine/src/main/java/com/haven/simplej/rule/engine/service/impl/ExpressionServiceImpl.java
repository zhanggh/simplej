package com.haven.simplej.rule.engine.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rule.engine.model.ExpressionModel;
import com.haven.simplej.rule.engine.service.ExpressionService;
import com.haven.simplej.db.base.BaseServiceImpl2;
/**
 * 规则表达式，一个规则对应多个表达式 Service implements
 */
@Slf4j
@Service

public class ExpressionServiceImpl extends BaseServiceImpl2<ExpressionModel> implements ExpressionService {


}