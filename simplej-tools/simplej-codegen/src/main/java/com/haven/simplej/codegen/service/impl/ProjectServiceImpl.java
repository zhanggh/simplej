package com.haven.simplej.codegen.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.haven.simplej.codegen.builder.ProjectBuilder;
import com.haven.simplej.codegen.dao.EntityInfoDao;
import com.haven.simplej.codegen.enums.ProjectType;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.codegen.service.ProjectService;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.manager.DatasourceManager;
import com.haven.simplej.io.FileUtil;
import com.haven.simplej.response.enums.RespCode;
import com.haven.simplej.response.model.Response;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.zip.ZipUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Set;

/**
 * @author haven.zhang
 * @date 2019/1/14.
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectBuilder builder;

	@Autowired
	private EntityInfoDao entityInfoDao;

	@Autowired
	private DatasourceManager datasourceManager;

	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor executor;


	@Override
	public Response createProject(ProjectRequestModel projectModel) throws Exception {

		log.info("create new project ,name:{},propertyModel:{}", projectModel.getArtifactId(), JSON.toJSONString(projectModel, true));
		update(projectModel);
		if (projectModel.getUseDbFlag() == 1) {
			CommonDao dao = initDao(projectModel);
			if (dao == null) {
				return new Response(RespCode.FAIL, "init datasource fail");
			}
			entityInfoDao.setDao(dao);
		}

		//删除多余目录和文件
		String projectPath = projectModel.getRootPath() + projectModel.getArtifactId();
		File projectDir = new File(projectPath);
		if (projectDir.exists()) {
			FileUtil.deleteDir(projectDir);
		}
		File outputFile = new File(projectPath + ".zip");
		if (outputFile.exists()) {
			FileUtil.deleteFile(outputFile);
		}
		//重新构建工程文件
		if (ProjectType.simpleApp.name().equalsIgnoreCase(projectModel.getProjectType())) {
			builder.buildServiceProject(projectModel);
			ZipUtil.createZipFile(projectPath, outputFile);
		} else {
			builder.buildWebProject(projectModel);
			PerformanceUtil.start();
			ZipUtil.createZipFile(projectPath, outputFile);
			log.info("zip cost:{}", PerformanceUtil.duration());
		}

		log.info("创建新项目：{} 完成，打开目录:{}", projectModel.getArtifactId(), projectModel.getRootPath());
		//		WindowUtil.openDir(openPath.replaceAll("\\/", "\\\\"));
		return new Response(RespCode.SUCCESS, "create new project success");
	}


	/**
	 * 复制属性
	 * @param projectModel
	 */
	private void update(ProjectRequestModel projectModel) {
		projectModel.setWebArtifactId(projectModel.getArtifactId() + "Web");
		projectModel.setWebGroupId(projectModel.getGroupId());
		projectModel.setWebVersion(projectModel.getVersion());
		projectModel.setParentArtifactId(projectModel.getArtifactId());
		projectModel.setParentGroupId(projectModel.getGroupId());
		projectModel.setParentVersion(projectModel.getVersion());
		projectModel.setServiceArtifactId(projectModel.getArtifactId() + "Service");
		projectModel.setServiceGroupId(projectModel.getGroupId());
		projectModel.setServiceVersion(projectModel.getVersion());

		projectModel.setDatasourceType(getDatasourceType(projectModel));
		//打开目录
		String openPath = System.getProperty("user.dir") + "/output/project/";
		projectModel.setRootPath(openPath);

		if (projectModel.getUseDbFlag() == 0) {
			projectModel.setDbHost("");
			projectModel.setPort("");
			projectModel.setDaoType("0");
			projectModel.setUserName("");
			projectModel.setPassword("");
			projectModel.setTabNames(new String[]{"test.userinfo"});
		} else {
			String tables = StringUtils.replaceAll(projectModel.getTables(), "\n", "").replaceAll(" ", "");
			projectModel.setTables(tables);
			projectModel.setTabNames(tables.split(","));
		}
	}

	/**
	 * 判断是否分库
	 * @param projectModel
	 * @return
	 */
	private String getDatasourceType(ProjectRequestModel projectModel) {
		String[] tables = StringUtils.split(projectModel.getTables(), ",");
		Set<String> set = Sets.newHashSet();
		for (String table : tables) {
			String[] params = StringUtil.split(table, "\\.");
			set.add(params[0]);
		}
		if (set.size() > 1) {
			return "sharding";
		}
		return "single";
	}


	/**
	 * 初始化dao
	 * @param model ProjectRequestModel
	 * @return CommonDao
	 */
	private CommonDao initDao(ProjectRequestModel model) {
		String[] tables = model.getTables().split(",");
		String[] params = tables[0].split("\\.");
		return datasourceManager.initDao(model.getDbHost(), model.getPort(), model.getUserName(), model.getPassword(), params[0]);
	}
}
