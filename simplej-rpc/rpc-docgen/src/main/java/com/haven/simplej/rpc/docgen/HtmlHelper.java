package com.haven.simplej.rpc.docgen;

import com.google.common.collect.Maps;
import com.haven.simplej.rpc.docgen.model.MavenCfgModel;
import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.model.RpcModelMeta;
import com.haven.simplej.rpc.model.ServiceMeta;
import com.haven.simplej.script.FreeMarkerUtil;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2018/9/21 21:09
 * @version 1.0
 */
public class HtmlHelper {

	/**
	 * api文档输出目录
	 */
	private static final String OUTPUT_DIR = "api_docs";
	/**
	 * 默认输出文档的编码
	 */
	private static final String ENCODE = "utf-8";

	/**
	 * 创建api文档首页
	 */
	public static void createIndexHtml(String defaultServiceId, String defaultMethodId)
			throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("basePackage", DocsGenerator.getBasePackage());
		data.put("serviceId", defaultServiceId);
		data.put("methodId", defaultMethodId);
		String resp = FreeMarkerUtil.getText("index.ftl", data);
		String outFileName = OUTPUT_DIR + "/" + DocsGenerator.getBasePackage() + "/index.html";
		FileUtils.writeStringToFile(new File(outFileName), resp, ENCODE);
	}

	/**
	 * 创建rpc服务列表页面
	 */
	public static void createServiceListHtml(List<ServiceMeta> serviceList) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("basePackage", DocsGenerator.getBasePackage());
		data.put("serviceList", serviceList);
		String resp = FreeMarkerUtil.getText("service_list.ftl", data);
		String outFileName = OUTPUT_DIR + "/" + DocsGenerator.getBasePackage() + "/service_list.html";
		FileUtils.writeStringToFile(new File(outFileName), resp, ENCODE);
	}

	/**
	 * 创建方法列表页面，每个service对应的methodList页面
	 */
	public static void createMethodListHtml(ServiceMeta serviceMeta) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("basePackage", DocsGenerator.getBasePackage());
		data.put("methods", serviceMeta.getMethods());
		int start = serviceMeta.getServiceName().lastIndexOf(".");
		String serviceName = serviceMeta.getServiceName().substring(start + 1);
		data.put("serviceName", serviceName);
		String resp = FreeMarkerUtil.getText("method_list.ftl", data);
		String outFileName = OUTPUT_DIR + "/" + DocsGenerator.getBasePackage() + "/service_methods/" + serviceMeta.getServiceId() +
				"_list.html";
		FileUtils.writeStringToFile(new File(outFileName), resp, ENCODE);
	}

	/**
	 * 创建方法详情页面，每个方法一个详情页面
	 */
	public static void createMethodDetailHtml(MethodMeta methodMeta, ServiceMeta serviceMeta,
			MavenCfgModel mavenCfgModel)
			throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("basePackage", DocsGenerator.getBasePackage());
		data.put("methodMeta", methodMeta);
		data.put("serviceMeta", serviceMeta);
		data.put("mavenCfgModel", mavenCfgModel);
		int start = serviceMeta.getServiceName().lastIndexOf(".");
		String serviceName = serviceMeta.getServiceName().substring(start + 1);
		data.put("shortServiceName", serviceName);
		String resp = FreeMarkerUtil.getText("method_detail.ftl", data);
		String outFileName = OUTPUT_DIR + "/" + DocsGenerator.getBasePackage() + "/method/" + methodMeta.getMethodId() +
				".html";
		FileUtils.writeStringToFile(new File(outFileName), resp, ENCODE);
	}

	/**
	 * 创建model详情页面
	 */
	public static void createModelHtml(RpcModelMeta rpcModelMeta) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("basePackage", DocsGenerator.getBasePackage());
		data.put("model", rpcModelMeta);
		String resp = FreeMarkerUtil.getText("model_detail.ftl", data);
		String outFileName = OUTPUT_DIR + "/" + DocsGenerator.getBasePackage() + "/model/" + rpcModelMeta.getModelId() +
				".html";
		FileUtils.writeStringToFile(new File(outFileName), resp, ENCODE);
	}


}
