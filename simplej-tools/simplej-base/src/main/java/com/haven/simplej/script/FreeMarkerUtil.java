package com.haven.simplej.script;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by haven.zhang on 2019/1/3.
 */
@Slf4j
public class FreeMarkerUtil {

	private static final String DEFAULT_ENCODE="UTF-8";

	private static Configuration cfg = new Configuration();

	static {
		try {

			// 设定去哪里读取相应的ftl模板文件
			File templatesDir = new File("templates");
			if (templatesDir.exists()) {
				cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "templates");
			} else {
				URL configUrl = FreeMarkerUtil.class.getClassLoader().getResource("/BOOT-INF/classes/templates");
				if (configUrl == null) {
					configUrl = FreeMarkerUtil.class.getResource("/BOOT-INF/classes/templates");
				}
				if (configUrl != null) {
					cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/BOOT-INF/classes/templates");
				} else {
					cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/templates");
				}
			}
			cfg.setDefaultEncoding("UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static String getText(String tmplName, Map<String, Object> params) throws IOException, TemplateException {
		try (ByteArrayOutputStream ba = new ByteArrayOutputStream(); //
				Writer writer = new OutputStreamWriter(ba, Charset.forName(DEFAULT_ENCODE))) {
			cfg.getTemplate(tmplName).process(params, writer);
			return ba.toString(DEFAULT_ENCODE);
		}
	}
}
