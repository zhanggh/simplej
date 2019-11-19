package com.haven.simplej.test.template;

import com.google.common.collect.Maps;
import com.haven.simplej.codegen.kit.TmplKit;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author haven.zhang
 * @date 2019/1/11.
 */
public class TemplateTest {
	public static void main(String[] args) throws IOException, TemplateException {
		String path = "./output/template/";
		Map<String,Object> data = Maps.newHashMap();
		String str = TmplKit.tmplToString("src/main/webapp/webxml.ftl", data);


		File file = new File(path, "web.xml");
		TmplKit.strToFile(str, file);

		System.out.println("-----------------------");
	}
}
