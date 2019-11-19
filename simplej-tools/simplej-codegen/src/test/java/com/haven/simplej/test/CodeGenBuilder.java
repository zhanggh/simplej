package com.haven.simplej.test;

import com.haven.simplej.StartUpApplication;
import com.haven.simplej.codegen.builder.CodeBuilder;
import com.haven.simplej.codegen.builder.ProjectBuilder;
import com.haven.simplej.codegen.enums.ProjectType;
import com.haven.simplej.codegen.ProjectInfo;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.windows.WindowUtil;
import com.haven.simplej.zip.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * 单元测试类
 * 可根据实际情况生成相关代码，如domain/dao/service/controller/mapper
 * 甚至java app 、web项目等
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class CodeGenBuilder {


	@Autowired
	Environment environment ;

	@Autowired
	private CodeBuilder codeBuilder;

	@Autowired
	private ProjectBuilder projectBuilder;

	private ProjectRequestModel projectModel;


	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		System.out.printf("prop:"+environment.getProperty("table.names"));

		projectModel.setParentGroupId("com.haven.simplej");
		projectModel.setParentArtifactId("demo");
		projectModel.setVersion("1.0");
		projectModel.setBasePackage("com.haven.simplej");
		projectModel.setBusinessName("demo");
	}


	/**
	 * 创建基础工程
	 * @throws Exception
	 */
	@Test
	public void createProject() throws Exception {
		if(ProjectType.simpleApp.name().equalsIgnoreCase(projectModel.getProjectType())){
			projectBuilder.buildServiceProject(projectModel);
		}else {
			projectBuilder.buildWebProject(projectModel);
		}
		//打开目录
		String rootPath = System.getProperty("user.dir");
		String openPath = rootPath+StringUtils.substring(projectModel.getRootPath(),1);
		log.info("创建新项目：{} 完成，打开目录:{}",projectModel.getParentArtifactId(),openPath);
		WindowUtil.openDir(openPath.replaceAll("\\/","\\\\"));
		ZipUtil.createZipFile(openPath, new File(openPath + "/" + projectModel.getParentArtifactId() + ".zip"));
	}

	/**
	 * 生成领域模型
	 * @throws Exception
	 */
	@Test
	public void aCreateDomain() throws Exception {
		codeBuilder.buildDomain(null,projectModel);
	}

	/**
	 * 可以理解为对外的模型，比如model
	 * @throws Exception
	 */
	@Test
	public void bCreateModel() throws Exception {
		 codeBuilder.buildModel(null,projectModel);
	}

	@Test
	public void cCreateMapper() throws Exception {
		codeBuilder.buildMapper(null,projectModel);
	}

	@Test
	public void dCreateDao() throws Exception {
		codeBuilder.buildDao(null,projectModel);
	}

	@Test
	public void eCreateService() throws Exception {
		codeBuilder.buildService(null,projectModel);
	}

	@Test
	public void fCreateController() throws Exception {
		codeBuilder.buildController(null,projectModel);
	}

	@Test
	public void gCreateWebProject() throws Exception {
		projectBuilder.buildWebProject(projectModel);
	}

	@Test
	public void hCreateAppProject() throws Exception {
		projectBuilder.buildServiceProject(projectModel);
	}

	@Test
	public void iCreateTableSql() throws Exception {
		codeBuilder.buildTableSql();
	}

}
