package com.haven.simplej.rpc.docgen.util;

import com.haven.simplej.rpc.docgen.DocsGenerator;
import com.haven.simplej.rpc.docgen.HtmlHelper;
import com.haven.simplej.rpc.docgen.model.MavenCfgModel;
import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.model.RpcModelMeta;
import com.haven.simplej.rpc.model.ServiceMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/21 17:46
 * @version 1.0
 */
@Slf4j
public class DocGenTest {

	public static void main(String[] args) {
		log.info("--------------------");
		String artifactId = "demo1";
		String groupId = "com.haven.simplej";
		String version = "1.0";
		String basePackage = "com.haven.simplej.demo.rpc";
		DocsGenerator.scanPackage(basePackage);
		MavenCfgModel mavenCfgModel = new MavenCfgModel(artifactId, groupId, version);
		try {
			//api首页
			List<ServiceMeta> list = new ArrayList<>(DocsGenerator.getServiceMetaList());
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
