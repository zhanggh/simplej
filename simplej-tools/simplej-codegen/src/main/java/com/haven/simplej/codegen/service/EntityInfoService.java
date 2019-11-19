package com.haven.simplej.codegen.service;

import com.haven.simplej.codegen.model.EntityInfo;

import java.util.List;

/**
 * @author haven.zhang
 * @date 2019/1/25.
 */
public interface EntityInfoService {

	EntityInfo getEntity(String name);

	EntityInfo getEntity(String tableName,int useDbFlag);

}
