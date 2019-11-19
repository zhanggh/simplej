package com.haven.simplej.codegen.builder;

import com.google.common.collect.Lists;
import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.codegen.enums.DaoType;
import com.haven.simplej.codegen.enums.ModuleType;
import com.haven.simplej.codegen.enums.ProjectType;
import com.haven.simplej.codegen.kit.TmplKit;
import com.google.common.collect.Maps;
import com.haven.simplej.codegen.ProjectInfo;
import com.haven.simplej.codegen.model.EntityInfo;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.sequence.SequenceUtil;
import com.vip.vjtools.vjkit.io.FileUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * java工程构建类
 * 生成的web项目跟普通的app项目差别在于web项目包含了controller类，以及web相关的配置类
 * 然而其他service/dao/mapper等公共类均在一个xxxService的工程中
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@Component
public class ProjectBuilder {

	private static final String SRC = "\\src\\main\\java";
	private static final String RESOURCE = "\\src\\main\\resources";
	//web目录
	private static final String WEBAPP_VIEWS = "\\src\\main\\resources\\templates";
	private static final String WEBAPP_STATIC = "\\src\\main\\resources\\static";

	private static final String TEST_SRC = "\\src\\test\\java";
	private static final String TEST_RESOURCE = "\\src\\test\\resources";

	@Autowired
	private CodeBuilder codeBuilder;
	@Autowired
	private JunitCodeBuilder junitcCdeBuilder;

	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor executor;

	/**
	 * 构建一个基础版本的web项目（maven项目） 包含两个模块，一个是web模块，一个是service模块
	 * 例如：
	 * epay
	 *   -- epayWeb
	 *   -- epayService
	 * @param projectModel
	 */
	public void buildWebProject(ProjectRequestModel projectModel) throws Exception {
		List<Future> tasks = Lists.newArrayList();
		//web工程名
		String webName = projectModel.getWebArtifactId();
		//根项目目录
		String projectPath = projectModel.getRootPath() + projectModel.getParentArtifactId() + "/";
		//生成根目录
		FileUtil.makesureDirExists(projectPath);
		//pom.xml参数
		Map<String, Object> params = Maps.newHashMap();
		params.put("artifactId", projectModel.getParentArtifactId());
		params.put("groupId", projectModel.getParentGroupId());
		params.put("version", projectModel.getParentVersion());
		params.put("projectName", projectModel.getParentArtifactId());
		params.put("description", projectModel.getProjectDesc());
		params.put("webName", webName);
		params.put("serviceName", projectModel.getServiceArtifactId());
		params.put("projectType", projectModel.getProjectType());
		params.put("useDbFlag", projectModel.getUseDbFlag());
		params.put("useRpcFlag", projectModel.getUseRpcFlag());
		params.put("parentGroupId", projectModel.getParentGroupId());
		params.put("parentVersion", projectModel.getParentVersion());
		params.put("parentArtifactId", projectModel.getParentArtifactId());
		//生成pom.xml
		tasks.add(executor.submit(() -> buildPomFile(projectPath, "rootpom.ftl", params)));
		tasks.add(executor.submit(() -> buildGitignore(projectPath, "gitignore.ftl")));
		tasks.add(executor.submit(() -> buildReadme(projectPath, "readme.ftl")));


		//生成web项目
		//		FileUtil.makesureDirExists(projectPath + webName);
		FileUtil.makesureDirExists(projectPath + webName + SRC);
		FileUtil.makesureDirExists(projectPath + webName + RESOURCE);
		FileUtil.makesureDirExists(projectPath + webName + TEST_SRC);
		FileUtil.makesureDirExists(projectPath + webName + TEST_RESOURCE);
		FileUtil.makesureDirExists(projectPath + webName + WEBAPP_VIEWS);
		FileUtil.makesureDirExists(projectPath + webName + WEBAPP_STATIC);
		//web项目对应的pom.xml
		params.put("artifactId", webName);
		params.put("groupId", projectModel.getWebGroupId());
		params.put("version", projectModel.getWebVersion());
		params.put("parentGroupId", projectModel.getParentGroupId());
		params.put("parentVersion", projectModel.getParentVersion());
		params.put("parentArtifactId", projectModel.getParentArtifactId());
		params.put("projectName", webName);
		params.put("description", projectModel.getProjectDesc());
		params.put("serviceName", projectModel.getServiceArtifactId());
		params.put("useDbFlag", projectModel.getUseDbFlag());
		params.put("useRpcFlag", projectModel.getUseRpcFlag());

		//生成web工程下的pom文件
		tasks.add(executor.submit(() -> buildPomFile(projectPath + webName, "webpom.ftl", params)));
		//生成web 工程controller类
		tasks.add(executor.submit(() -> buildWebCodes(projectPath + webName + SRC, projectModel)));

		if (projectModel.getUseRpcFlag() == 0 && projectModel.getUseDbFlag() == 1) {
			//非rpc项目的情况下，才在web工程下直接访问数据库
			tasks.add(executor.submit(() -> buildDbPropertyFile(projectPath + webName + RESOURCE + "/db", projectModel)));
		}
		//		buildHeathCheckFile(projectPath + webName + WEBAPP, "webapp/health_check.ftl");
		//生成logback.xml文件
		tasks.add(executor.submit(() -> buildLogbackXml(projectPath + webName + RESOURCE, "logback.ftl", projectModel)));
		//生成properties文件
		tasks.add(executor.submit(() -> buildPropertiesFile(projectPath + webName + RESOURCE, "properties.ftl", "application.properties", projectModel)));
		if (projectModel.getUseRpcFlag() == 1) {
			tasks.add(executor.submit(() -> buildPropertiesFile(projectPath + webName + RESOURCE, "rpc-client-properties.ftl", "rpc-client.properties", projectModel)));
			tasks.add(executor.submit(() -> buildPropertiesFile(projectPath + webName + RESOURCE, "rpc-server" + "-properties.ftl", "rpc-server.properties", projectModel)));
		}

		//生成跟目录的gitingore文件
		tasks.add(executor.submit(() -> buildGitignore(projectPath, "gitignore.ftl")));
		//生成web项目的gitingore文件
		tasks.add(executor.submit(() -> buildGitignore(projectPath + webName, "gitignore.ftl")));

		if (projectModel.getNeedAdminTemplate() == 1) {
			//生成静态资源文件
			String staticPath = PropertyManager.get("project.static.base.dir", "static");
			File staticDir = new File(staticPath);
			if (!staticDir.exists() || staticDir.listFiles().length < 1) {
				staticPath = "simplej-codegen/" + staticPath;
			}
			String finalStaticPath = staticPath;
			tasks.add(executor.submit(() -> copyStaticFile(finalStaticPath, projectPath + webName + RESOURCE + "\\static")));
			//生成sql文件
			String sqlPath = PropertyManager.get("project.sql.base.dir", "sql");
			File sqlDir = new File(sqlPath);
			if (!sqlDir.exists() || sqlDir.listFiles().length < 1) {
				sqlPath = "simplej-codegen/" + sqlPath;
			}
			String finalSqlPath = sqlPath;
			tasks.add(executor.submit(() -> copyStaticFile(finalSqlPath, projectPath + webName + RESOURCE + "\\sql")));
		}

		//service模块名
		tasks.add(executor.submit(() -> buildServiceProject(projectModel)));
		tasks.forEach(e -> {
			try {
				e.get();
			} catch (Exception e1) {
				log.error("task run error", e1);
			}
		});
	}

	/**
	 * 构建rpc service api 工程
	 * @param projectModel
	 */
	public void buildServiceApiProject(ProjectRequestModel projectModel) {
		try {
			//工程名为
			String moduleName = projectModel.getServiceArtifactId() + "-api";
			String appPath = projectModel.getRootPath() + projectModel.getParentArtifactId() + "/";

			//生成项目目录
			//			FileUtil.makesureDirExists(appPath + moduleName);
			FileUtil.makesureDirExists(appPath + moduleName + SRC);
			FileUtil.makesureDirExists(appPath + moduleName + RESOURCE);
			FileUtil.makesureDirExists(appPath + moduleName + TEST_SRC);
			FileUtil.makesureDirExists(appPath + moduleName + TEST_RESOURCE);

			Map<String, Object> params = Maps.newHashMap();
			params.put("groupId", projectModel.getServiceGroupId());
			params.put("version", projectModel.getServiceVersion());
			params.put("artifactId", moduleName);
			params.put("parentGroupId", projectModel.getParentGroupId());
			params.put("parentVersion", projectModel.getParentVersion());
			params.put("parentArtifactId", projectModel.getParentArtifactId());
			params.put("projectName", projectModel.getServiceArtifactId());
			params.put("serviceName", projectModel.getServiceArtifactId());
			params.put("description", projectModel.getProjectDesc());
			params.put("projectType", projectModel.getProjectType());
			params.put("useDbFlag", projectModel.getUseDbFlag());//是否需要数据库
			params.put("useRpcFlag", projectModel.getUseRpcFlag());

			List<Future> tasks = Lists.newArrayList();
			//生成pom.xml
			tasks.add(executor.submit(() -> buildPomFile(appPath + moduleName, "service-api-pom.ftl", params)));
			tasks.add(executor.submit(() -> buildGitignore(appPath + moduleName, "gitignore.ftl")));
			tasks.add(executor.submit(() -> buildReadme(appPath + moduleName, "readme.ftl")));

			//生成rpc model类
			tasks.add(executor.submit(() -> codeBuilder.buildRpcModel(appPath + moduleName + SRC, projectModel)));

			//生成rpc service类
			tasks.add(executor.submit(() -> codeBuilder.buildRpcServiceApi(appPath + moduleName + SRC, projectModel)));

			tasks.forEach(e -> {
				try {
					e.get();
				} catch (Exception e1) {
					log.error("task run error", e1);
				}
			});
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建一个基础版本的java app项目（maven项目）
	 * @param projectModel
	 */
	public void buildServiceProject(ProjectRequestModel projectModel) {
		try {
			//工程名为
			String moduleName = projectModel.getServiceArtifactId();
			String appPath = projectModel.getRootPath() + projectModel.getParentArtifactId() + "/";

			//生成项目目录
			//			FileUtil.makesureDirExists(appPath + moduleName);
			FileUtil.makesureDirExists(appPath + moduleName + SRC);
			FileUtil.makesureDirExists(appPath + moduleName + RESOURCE);
			FileUtil.makesureDirExists(appPath + moduleName + TEST_SRC);
			FileUtil.makesureDirExists(appPath + moduleName + TEST_RESOURCE);

			Map<String, Object> params = Maps.newHashMap();
			params.put("groupId", projectModel.getServiceGroupId());
			params.put("version", projectModel.getServiceVersion());
			params.put("artifactId", moduleName);
			params.put("parentGroupId", projectModel.getParentGroupId());
			params.put("parentVersion", projectModel.getParentVersion());
			params.put("parentArtifactId", projectModel.getParentArtifactId());
			params.put("projectName", projectModel.getServiceArtifactId());
			params.put("serviceName", projectModel.getServiceArtifactId());
			params.put("description", projectModel.getProjectDesc());
			params.put("projectType", projectModel.getProjectType());
			params.put("useDbFlag", projectModel.getUseDbFlag());//是否需要数据库
			params.put("useRpcFlag", projectModel.getUseRpcFlag());

			List<Future> tasks = Lists.newArrayList();
			if (ProjectType.simpleApp.name().equalsIgnoreCase(projectModel.getProjectType())) {
				//生成pom.xml
				tasks.add(executor.submit(() -> buildPomFile(appPath, "rootpom.ftl", params)));
				tasks.add(executor.submit(() -> buildGitignore(appPath, "gitignore.ftl")));
				tasks.add(executor.submit(() -> buildReadme(appPath, "readme.ftl")));
			}
			//生成pom.xml
			tasks.add(executor.submit(() -> buildPomFile(appPath + moduleName, "servicepom.ftl", params)));
			//生成gitingore文件
			tasks.add(executor.submit(() -> buildGitignore(appPath + moduleName, "gitignore.ftl")));
			//生成readme.md文件
			tasks.add(executor.submit(() -> buildReadme(appPath + moduleName, "readme.ftl")));

			if (projectModel.getUseRpcFlag() == 1) {
				if (projectModel.getUseDbFlag() == 1) {//如果需要访问数据库
					//生成数据库属性配置文件
					tasks.add(executor.submit(() -> buildDbPropertyFile(appPath + moduleName + RESOURCE + "/db", projectModel)));
					tasks.add(executor.submit(() -> buildDbPropertyFile(appPath + moduleName + TEST_RESOURCE + "/db", projectModel)));
				}
				//生成spring boot配置类
				tasks.add(executor.submit(() -> buildSpringBootConfig(appPath + moduleName + SRC, false, false,
						projectModel, ModuleType.SERVICE.name())));
			} else {
				if (projectModel.getUseDbFlag() == 1) {//如果需要访问数据库
					//生成数据库属性配置文件
					tasks.add(executor.submit(() -> buildDbPropertyFile(appPath + moduleName + TEST_RESOURCE + "/db", projectModel)));
				}
			}
			//生成spring boot配置类
			tasks.add(executor.submit(() -> buildSpringBootConfig(appPath + moduleName + TEST_SRC, false, true, projectModel, ModuleType.SERVICE.name())));
			//生成mybatis配置
			if (projectModel.getUseDbFlag() == 1 && StringUtils.equalsIgnoreCase(projectModel.getDaoType(), DaoType.MYBATIS.name())) {
				tasks.add(executor.submit(() -> buildMybatisXml(appPath + moduleName + RESOURCE, projectModel)));
			}
			//生成logback.xml文件
			tasks.add(executor.submit(() -> buildLogbackXml(appPath + moduleName + RESOURCE, "logback.ftl", projectModel)));
			//生成普通properties文件
			tasks.add(executor.submit(() -> buildPropertiesFile(appPath + moduleName + RESOURCE, "properties.ftl", "application.properties", projectModel)));
			if (projectModel.getUseRpcFlag() == 1) {
				tasks.add(executor.submit(() -> buildPropertiesFile(appPath + moduleName + RESOURCE, "rpc-client-properties.ftl", "rpc-client.properties", projectModel)));
				tasks.add(executor.submit(() -> buildPropertiesFile(appPath + moduleName + RESOURCE, "rpc-server" + "-properties.ftl", "rpc-server.properties", projectModel)));
			}
			//生成公共代码，如domain，dao ，service ，mapper等
			tasks.add(executor.submit(() -> buildCommonCodes(appPath + moduleName, projectModel)));
			//生成单元测试代码
			tasks.add(executor.submit(() -> junitcCdeBuilder.buildJunitTest(appPath + moduleName + TEST_SRC, projectModel)));

			if (projectModel.getUseRpcFlag() == 1) {
				//生成rpc service类
				tasks.add(executor.submit(() -> codeBuilder.buildRpcServiceImpl(appPath + moduleName + SRC, projectModel)));
				//生成service - api工程
				buildServiceApiProject(projectModel);
			}
			tasks.forEach(e -> {
				try {
					e.get();
				} catch (Exception e1) {
					log.error("task run error", e1);
				}
			});
		} catch (Exception e) {
			throw new UncheckedException(e);
		}

	}


	/**
	 * 生成项目中的pom.xml文件
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildPomFile(String outputPath, String pomTemplateName) {
		buildPomFile(outputPath, pomTemplateName, Maps.newHashMap());
	}

	/**
	 * 生成项目中的pom.xml文件
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildPomFile(String outputPath, String pomTemplateName, Map<String, Object> params) {
		try {
			Map<String, Object> data = new HashMap<>(2);
			String basePackage = PropertyManager.get(PropertyKey.BASE_PACKAGE);
			data.put("package", basePackage);
			data.putAll(params);

			String str = TmplKit.tmplToString(pomTemplateName, data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath, "pom.xml");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildPomFile error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建项目公共的类
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildCommonCodes(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (codeBuilder == null) {
				log.warn("codelBuilder is null,cannot execute buildCommonCodes method");
				return;
			}
			List<Future> tasks = Lists.newArrayList();
			if (projectModel.getUseDbFlag() == 1 && projectModel.getDaoType().equalsIgnoreCase(DaoType.MYBATIS.name())) {
				tasks.add(executor.submit(() -> codeBuilder.buildDao(outputPath + SRC, projectModel)));
				tasks.add(executor.submit(() -> codeBuilder.buildMapper(outputPath + RESOURCE, projectModel)));
			}

			tasks.add(executor.submit(() -> codeBuilder.buildDomain(outputPath + SRC, projectModel)));
			tasks.add(executor.submit(() -> codeBuilder.buildService(outputPath + SRC, projectModel)));
			tasks.add(executor.submit(() -> codeBuilder.buildModel(outputPath + SRC, projectModel)));
			tasks.forEach(e -> {
				try {
					e.get();
				} catch (Exception e1) {
					log.error("task run error", e1);
				}
			});
		} catch (Exception e) {
			log.error("buildCommonCodes error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 生成web项目相应代码，如controller等
	 */
	private void buildWebCodes(String outputPath, ProjectRequestModel projectModel) {
		try {
			if (codeBuilder == null) {
				log.warn("codelBuilder is null,cannot execute buildWebCodes method");
				return;
			}
			//生成controller类
			codeBuilder.buildController(outputPath, projectModel);

			//生成spring boot web配置类
			buildSpringBootConfig(outputPath, true, false, projectModel, ModuleType.WEB.name());
		} catch (Exception e) {
			log.error("buildWebCodes error", e);
			throw new UncheckedException(e);
		}

	}

	/**
	 * 构建工程下git的gitignore文件
	 * @param outputPath
	 * @param templateName
	 */
	private void buildGitignore(String outputPath, String templateName) {
		try {
			String str = TmplKit.tmplToString(templateName, Maps.newHashMap());
			File file = new File(outputPath, ".gitignore");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildGitignore error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建工程下Readme文件
	 * @param outputPath
	 * @param templateName
	 */
	private void buildReadme(String outputPath, String templateName) {
		try {
			String str = TmplKit.tmplToString(templateName, Maps.newHashMap());
			File file = new File(outputPath, "readme.md");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildReadme error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建logback.xml文件
	 */
	private void buildLogbackXml(String outputPath, String templateName, ProjectRequestModel projectModel) {
		try {
			Map<String, Object> data = new HashMap<>(2);
			data.put("contextName", projectModel.getParentArtifactId());
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());
			String str = TmplKit.tmplToString(templateName, data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath, "logback.xml");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildLogbackXml error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 构建application.properties文件
	 */
	private void buildPropertiesFile(String outputPath, String templateName, String outFileName,
			ProjectRequestModel projectModel) {
		try {
			Map<String, Object> data = Maps.newHashMap();
			String appName = projectModel.getArtifactId() + ".web.xxx.com";
			if (projectModel.getProjectType().equalsIgnoreCase(ProjectType.simpleApp.name())) {
				appName = projectModel.getArtifactId() + ".app.xxx.com";
			}
			data.put("appName", appName);
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());
			String str = TmplKit.tmplToString(templateName, data);
			File file = new File(outputPath, outFileName);
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildPropertiesFile error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 生成mybatis配置文件
	 * @param outputPath
	 * @param projectModel
	 */
	private void buildMybatisXml(String outputPath, ProjectRequestModel projectModel) {
		try {
			Map<String, Object> data = new HashMap<>(2);
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());
			String str = TmplKit.tmplToString("mybatis.ftl", data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath, "mybatis.xml");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildMybatisXml error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 创建web项目时，对应的ServletInitializer
	 * @param outputPath
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildSpringBootConfig(String outputPath, boolean buildWebCfg, boolean forTest,
			ProjectRequestModel projectModel,String moduleType) {
		try {
			String business = projectModel.getBusinessName().replace('.', '/');
			outputPath += "/" + projectModel.getBasePackage().replace('.', '/');
			outputPath += "/" + business;

			Map<String, Object> data = new HashMap<>(2);
			data.put("package", projectModel.getBasePackage());
			data.put("business", projectModel.getBusinessName());
			data.put("daoType", projectModel.getDaoType());
			data.put("projectType", projectModel.getProjectType());
			data.put("useDbFlag", projectModel.getUseDbFlag());
			data.put("useRpcFlag", projectModel.getUseRpcFlag());
			data.put("forTest", forTest);
			data.put("moduleType", moduleType);


			String beanConfig = "springboot/BeanConfiguration.ftl";

			//生成BeanConfiguration.java类
			String str = TmplKit.tmplToString(beanConfig, data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath + "/config", "BeanConfiguration.java");
			TmplKit.strToFile(str, file);

			if (buildWebCfg) {
				//WebMvcConfiguration.java类
				str = TmplKit.tmplToString("springboot/WebMvcConfiguration.ftl", data);
				str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
				file = new File(outputPath + "/config", "WebMvcConfiguration.java");
				TmplKit.strToFile(str, file);


				//WebInitConfiguration.java类
				str = TmplKit.tmplToString("springboot/WebInitConfiguration.ftl", data);
				str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
				file = new File(outputPath + "/config", "WebInitConfiguration.java");
				TmplKit.strToFile(str, file);


				//ServletInitializer.java类
				str = TmplKit.tmplToString("springboot/ServletInitializer.ftl", data);
				str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
				file = new File(outputPath, "ServletInitializer.java");
				TmplKit.strToFile(str, file);
			}

			//ServletInitializer.java类
			str = TmplKit.tmplToString("springboot/StartUpApplication.ftl", data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			file = new File(outputPath, "StartUpApplication.java");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildSpringBootConfig error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 创建web项目时，对应的heathcheck.jsp文件
	 * @param outputPath
	 * @param templateName
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildHeathCheckFile(String outputPath, String templateName) {
		try {
			Map<String, Object> data = new HashMap<>(2);
			String str = TmplKit.tmplToString(templateName, data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath, "heathcheck.jsp");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 创建数据库连接池属性配置文件
	 * @param outputPath
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void buildDbPropertyFile(String outputPath, ProjectRequestModel projectModel) {
		try {
			Map<String, Object> data = new HashMap<>(2);
			data.put("userName", projectModel.getUserName());
			data.put("password", projectModel.getPassword());
			data.put("host", projectModel.getDbHost());
			data.put("port", projectModel.getPort());
			data.put("dataSourceId", SequenceUtil.generateId());
			String str = TmplKit.tmplToString("datasource.ftl", data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			File file = new File(outputPath, "datasource01.properties");
			TmplKit.strToFile(str, file);
			//公共db属性文件
			str = TmplKit.tmplToString("datasource-common.ftl", data);
			str = StringUtils.replaceAll(str, "\\@\\{", "\\$\\{");
			file = new File(outputPath, "common.properties");
			TmplKit.strToFile(str, file);
		} catch (Exception e) {
			log.error("buildDbPropertyFile error", e);
			throw new UncheckedException(e);
		}
	}

	/**
	 * 复制静态资源文件，递归复制目录下的所有文件以及子目录下的文件
	 * @param staticPath
	 */
	public void copyStaticFile(String staticPath, String destDirPath) {
		try {
			File staticFile = new File(staticPath);
			if (staticFile.isFile()) {
				FileUtils.copyFileToDirectory(staticFile, new File(destDirPath));
			} else {
				FileUtils.copyDirectory(staticFile, new File(destDirPath));
			}
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

}
