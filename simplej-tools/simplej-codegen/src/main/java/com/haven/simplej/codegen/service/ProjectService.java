package com.haven.simplej.codegen.service;

import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.response.model.Response;

/**
 * @author haven.zhang
 * @date 2019/1/14.
 */
public interface ProjectService {

	/**
	 * 创建项目模板
	 * @return
	 */
	Response createProject(ProjectRequestModel propertyModel) throws Exception;
}
