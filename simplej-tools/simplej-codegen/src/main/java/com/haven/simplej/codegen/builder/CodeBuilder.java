package com.haven.simplej.codegen.builder;

import com.google.common.collect.Sets;
import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.codegen.kit.TmplKit;
import com.haven.simplej.codegen.model.EntityInfo;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.codegen.service.EntityInfoService;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.text.StringUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用于生成各类代码，如领域模型domain、dao 、mapper、service、controller 、vo、以及完整的工程代码等
 * @author haven.zhang
 * @date 2018/12/26.
 */
@Component
@Slf4j
public class CodeBuilder {

	@Autowired
	private EntityInfoService service;

	/**
	 * 构建rpc服务的请求响应model
	 */
	public void buildRpcModel(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/rpc.model";

			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				String str = TmplKit.tmplToString("rpc-model.ftl", data);
				File file = new File(outputPath, entity.getName() + "RpcModel.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildRpcModel error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建rpc api接口类
	 * @param outputPath
	 * @param projectModel
	 */
	public void buildRpcServiceApi(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/rpc.service";

			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				String str = TmplKit.tmplToString("rpcService.ftl", data);
				File file = new File(outputPath, entity.getName() + "RpcService.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildRpcServiceApi error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建rpc api 接口实现类
	 * @param outputPath
	 * @param projectModel
	 */
	public void buildRpcServiceImpl(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/rpc.service";

			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				data.put("useDbFlag", projectModel.getUseDbFlag());
				String str = TmplKit.tmplToString("rpcServiceImpl.ftl", data);
				File file = new File(outputPath, entity.getName() + "RpcServiceImpl.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildRpcServiceImpl error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 生成领域模型代码
	 * @param outputPath
	 */
	public void buildDomain(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/domain";
			Set set = Sets.newHashSet();
			set.add("id");
			set.add("isDeleted");
			set.add("createTime");
			set.add("createdBy");
			set.add("updateTime");
			set.add("updatedBy");
			set.add("reserved1");
			set.add("reserved2");
			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("baseFields", set);
				data.put("business", projectModel.getBusinessName());
				String str = TmplKit.tmplToString("domain.ftl", data);
				File file = new File(outputPath, entity.getName() + ".java");
				TmplKit.strToFile(str, file);
				//生成definition信息类
				str = TmplKit.tmplToString("domainDef.ftl", data);
				file = new File(outputPath + "/definition", entity.getName() + "Definition.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildDomain error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建Model类
	 * @throws Exception
	 * @param outputPath
	 */
	public void buildModel(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}

			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath = outputPath + "/model";
			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				String str = TmplKit.tmplToString("model.ftl", data);
				File file = new File(outputPath, entity.getName() + "Model.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildModel error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 生成mybatis mapper
	 * @throws IOException
	 * @throws TemplateException
	 * @param outputPath
	 */
	public void buildMapper(String outputPath, ProjectRequestModel projectModel) {

		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.MAPPERS_PATH);
				outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			}

			outputPath += "/mappers";
			Map<String, Object> data = new HashMap<>(2);
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());

			for (String name : projectModel.getTabNames()) {
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				String str = TmplKit.tmplToString("mapper.ftl", data);
				File file = new File(outputPath, entity.getName() + "Mapper.xml");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildMapper error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 生成dao类
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void buildDao(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/mapper";
			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				String str = TmplKit.tmplToString("dao.ftl", data);
				File file = new File(outputPath, entity.getName() + "Mapper.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildDao error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建service类
	 * @throws IOException
	 * @throws TemplateException
	 * @param outputPath
	 */
	public void buildService(String outputPath, ProjectRequestModel projectModel) {

		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/service";


			for (String name : projectModel.getTabNames()) {
				Map<String, Object> data = new HashMap<>(2);
				data.put("package", projectModel.getBasePackage());
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				data.put("business", projectModel.getBusinessName());
				data.put("daoType", projectModel.getDaoType());
				data.put("datasourceType", projectModel.getDatasourceType());
				data.put("useDbFlag", projectModel.getUseDbFlag());
				data.put("useRpcFlag", projectModel.getUseRpcFlag());
				{

					String str = TmplKit.tmplToString("service.ftl", data);
					File file = new File(outputPath, entity.getName() + "Service.java");
					TmplKit.strToFile(str, file);
				}
				{
					String str = TmplKit.tmplToString("service-impl.ftl", data);
					String path = outputPath + "/impl";

					File file = new File(path, entity.getName() + "ServiceImpl.java");
					TmplKit.strToFile(str, file);
				}
			}
		} catch (Exception e) {
			log.error("buildService error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建web项目的controller类
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void buildController(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (StringUtil.isEmpty(outputPath)) {
				outputPath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
			}
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + projectModel.getBusinessName().replace('.', '/');
			outputPath += "/web/controller";
			Map<String, Object> data = new HashMap<>(2);
			data.put("package", projectModel.getBasePackage());
			data.put("useDbFlag", projectModel.getUseDbFlag());
			data.put("useRpcFlag", projectModel.getUseRpcFlag());

			data.put("business", projectModel.getBusinessName());
			for (String name : projectModel.getTabNames()) {
				EntityInfo entity = service.getEntity(name, projectModel.getUseDbFlag());
				data.put("entity", entity);
				String mapping = projectModel.getBusinessName().replace('.', '/');
				data.put("mapping", mapping);

				String templateName = "controller.ftl";
				if (projectModel.getUseRpcFlag() == 1) {
					templateName = "rpc-controller.ftl";
				}
				String str = TmplKit.tmplToString(templateName, data);
				File file = new File(outputPath, entity.getName() + "Controller.java");
				TmplKit.strToFile(str, file);
			}
		} catch (Exception e) {
			log.error("buildController error", e);
			throw new UncheckedException(e);
		}
	}



	/**
	 * 通过domain反向生成表创建语句
	 */
	public void buildTableSql() {

	}
}
