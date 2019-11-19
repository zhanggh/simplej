package com.haven.simplej.codegen.kit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


import com.haven.simplej.property.PropertyManager;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class TmplKit {

	private TmplKit() {
	}

	public static String tmplToString(String tmplName, Map<String, Object> data) throws IOException, TemplateException {
		try (ByteArrayOutputStream ba = new ByteArrayOutputStream(); //
				Writer writer = new OutputStreamWriter(ba, Charset.forName("UTF-8"))) {
			TmplConfigGetter.tmplConfig.getTemplate(tmplName).process(data, writer);
			return ba.toString("UTF-8");
		}
	}

	public static void strToFile(String str, File file) throws IOException {

		FileUtils.writeByteArrayToFile(file,str.getBytes("utf-8"));
	}

	private static class TmplConfigGetter {
		private static Configuration tmplConfig;

		static {
			try {
				Configuration cfg = new Configuration();
				// 设定去哪里读取相应的ftl模板文件
				File templatesDir = new File("templates");
				if (templatesDir.exists()) {
					cfg.setClassForTemplateLoading(TmplConfigGetter.class, "templates");
				} else {
					URL configUrl = TmplConfigGetter.class.getClassLoader().getResource("/BOOT-INF/classes/templates");
					if (configUrl == null) {
						configUrl = TmplConfigGetter.class.getResource("/BOOT-INF/classes/templates");
					}
					if (configUrl != null) {
						cfg.setClassForTemplateLoading(TmplConfigGetter.class, "/BOOT-INF/classes/templates");
					} else {
						cfg.setClassForTemplateLoading(TmplConfigGetter.class, "/templates");
					}
				}
				cfg.setDefaultEncoding("UTF-8");
				tmplConfig = cfg;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
