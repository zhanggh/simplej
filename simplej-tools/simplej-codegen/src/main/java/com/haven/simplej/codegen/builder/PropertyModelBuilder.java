package com.haven.simplej.codegen.builder;

import com.haven.simplej.codegen.ProjectInfo;
import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.property.PropertyManager;

/**
 * Created by haven.zhang on 2019/1/10.
 */
public class PropertyModelBuilder {

	public static ProjectInfo build(){
		ProjectInfo model = new ProjectInfo();


		//项目父级ArtifactId
		String parentArtifactId = PropertyManager.get(PropertyKey.PROJECT_PARENT_ARTIFACTID);
		model.setParentArtifactId(parentArtifactId);
		//项目父级groupId
		String parentGroupId = PropertyManager.get(PropertyKey.PROJECT_PARENT_GROUPID);
		model.setParentGroupId(parentGroupId);
		//项目父级version
		String parentVersion = PropertyManager.get(PropertyKey.PROJECT_PARENT_VERSION);
		model.setParentVersion(parentVersion);

		//web项目名称
		model.setWebArtifactId(parentArtifactId + "Web");
		//项目groupId
		model.setWebGroupId(model.getParentGroupId());
		//项目version
		model.setWebVersion(model.getParentVersion());

		//web项目名称
		model.setServiceArtifactId(parentArtifactId + "Service");
		//项目groupId
		model.setServiceGroupId(model.getParentGroupId());
		//项目version
		model.setServiceVersion(model.getParentVersion());

		//项目类型
		String projectType = PropertyManager.get(PropertyKey.PROJECT_TYPE);
		model.setProjectType(projectType);
		//项目描述
		String projectDesc = PropertyManager.get(PropertyKey.PROJECT_DESCRIPT);
		model.setProjectDesc(projectDesc);
		//项目目录
		String rootPath = PropertyManager.get(PropertyKey.PROJECT_PATH);
		model.setRootPath(rootPath);

		String basePackage = PropertyManager.get(PropertyKey.BASE_PACKAGE);
		model.setBasePackage(basePackage);
		String business = PropertyManager.get(PropertyKey.BUSINESS_NAME);
		model.setBusinessName(business);
		String basePath = PropertyManager.get(PropertyKey.JAVA_OUTPUT_FILE);
		model.setJavaFilePath(basePath);

		String mapperPath = PropertyManager.get(PropertyKey.MAPPERS_PATH);
		model.setMappersPath(mapperPath);

		String daoType = PropertyManager.get(PropertyKey.PROJECT_DAO_TYPE);
		model.setProjectDaoType(daoType);

		String dataSourceType = PropertyManager.get(PropertyKey.PROJECT_DATASOURCE_TYPE);
		model.setDatasourceType(dataSourceType);
		return model;
	}
}
