package com.haven.simplej.codegen.helper;

import com.haven.simplej.codegen.model.ProjectRequestModel;

/**
 * @author: havenzhang
 * @date: 2019/4/25 22:09
 * @version 1.0
 */
public class ThreadHelper {

	private static ThreadLocal<ProjectRequestModel> modelThreadLocal = ThreadLocal.withInitial(() -> new ProjectRequestModel());

	public static ProjectRequestModel get(){
		return modelThreadLocal.get();
	}

	public static void set(ProjectRequestModel model){
		modelThreadLocal.set(model);
	}
}
