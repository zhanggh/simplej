package com.haven.simplej.rpc.docgen;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.docgen.model.MavenCfgModel;
import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.model.RpcModelMeta;
import com.haven.simplej.rpc.model.ServiceMeta;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * rpc api 文档生成器插件
 * @author: havenzhang
 * @date: 2019/1/15 17:14
 * @version 1.0
 */
@Mojo(name = "rpcApiDocGen", defaultPhase = LifecyclePhase.PACKAGE)
@Slf4j
public class RpcApiDocGen extends AbstractMojo {

	/**
	 * 扫描的类所在的包路径
	 */
	@Parameter(property = "basePackage")
	private String basePackage;

	//	@Parameter(expression  = "${project.artifactId}")
	//	private String artifactId;
	//
	//	@Parameter(expression  = "${project.groupId}")
	//	private String groupId;
	//
	//	@Parameter(property = "${project.version}")
	//	private String version;

	private MavenProject project;

	@Override
	public void execute() {

		System.out.println("----------------------MavenProject----------------------------");

		DocsGenerator.scanPackage(basePackage);
		try {
			FileUtils.writeStringToFile(new File("maven_context.txt"),JSON.toJSONString(getPluginContext(), true));
			project = (MavenProject) getPluginContext().get("project");
			MavenCfgModel mavenCfgModel = new MavenCfgModel(project.getArtifactId(), project.getGroupId(),
					project.getVersion());
			System.out.println(JSON.toJSONString(mavenCfgModel));
			//api首页
			List<ServiceMeta> list = new ArrayList<>(DocsGenerator.getServiceMetaList());
			if(CollectionUtil.isEmpty(list)){
				System.out.println("----------------------List<ServiceMeta> is empty----------------------");
				return;
			}
			ServiceMeta svcMeta = list.get(0);
			HtmlHelper.createIndexHtml(svcMeta.getServiceId(), svcMeta.getMethods().get(0).getMethodId());
			HtmlHelper.createServiceListHtml(list);
			for (ServiceMeta serviceMeta : DocsGenerator.getServiceMetaList()) {
				HtmlHelper.createMethodListHtml(serviceMeta);
				for (MethodMeta method : serviceMeta.getMethods()) {
					HtmlHelper.createMethodDetailHtml(method, serviceMeta, mavenCfgModel);
				}
			}
			for (RpcModelMeta rpcModelMeta : DocsGenerator.getModelList()) {
				HtmlHelper.createModelHtml(rpcModelMeta);
			}
		} catch (Exception e) {
			log.error("execute error", e);
		}

		System.out.println("-----------------------------------rpcApiDocGen-----------------------------------");
	}
}
