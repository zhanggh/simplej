package com.haven.simplej.codegen.builder;

import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.codegen.kit.TmplKit;
import com.haven.simplej.codegen.model.EntityInfo;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.codegen.service.impl.EntityInfoServiceImpl;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试代码生成器
 * @author haven.zhang
 * @date 2019/1/15.
 */
@Component
@Slf4j
public class JunitCodeBuilder {
	@Autowired
	private EntityInfoServiceImpl service;


	public void buildJunitTest(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			//构建单元测试service类
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			String servicePath = outputPath + "/service";
			String rpcServicePath = outputPath + "/rpc/service";

			Map<String, Object> data = new HashMap<>(2);
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());
			data.put("useRpcFlag", projectModel.getUseRpcFlag());

			for (String name : projectModel.getTabNames()) {
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				String str = TmplKit.tmplToString("test/junit-test.ftl", data);
				File file = new File(servicePath, entity.getName() + "ServiceTest.java");
				TmplKit.strToFile(str, file);

				if(projectModel.getUseRpcFlag() == 1){
					str = TmplKit.tmplToString("test/junit-rpc-test.ftl", data);
					file = new File(rpcServicePath, entity.getName() + "RpcServiceTest.java");
					TmplKit.strToFile(str, file);
				}
			}
		} catch (Exception e) {
			log.error("buildJunitTest error", e);
			throw new UncheckedException(e);
		}
	}
}
