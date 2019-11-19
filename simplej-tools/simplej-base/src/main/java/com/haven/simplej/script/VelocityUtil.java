package com.haven.simplej.script;


import com.haven.simplej.security.DigestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * velocity模板 eval操作管理类
 **/
@Slf4j
public class VelocityUtil {
	static VelocityEngine ve = new VelocityEngine();

	static {
		ve.init();
	}

	public static String getText(Map<String, String> map, String key){
		return getText(map, key, new HashMap<>());
	}

	public static String getText(Map<String, String> map, String key, Map<String, Object> params){

		VelocityContext context = new VelocityContext();
		if(params!=null){
			params.forEach((k,v)->context.put(k,v));
		}
		StringWriter writer = new StringWriter();
		log.info("获取Text key：{}", key);
		// 转换输出
		ve.evaluate(context, writer, key, map.get(key));
		return writer.toString();
	}

	public static String getText(String templateContext, Map<String, Object> params){

		VelocityContext context = new VelocityContext();
		if(params!=null){
			params.forEach((k,v)->context.put(k,v));
		}
		StringWriter writer = new StringWriter();
		//		log.info("获取Text key：{}", key);
		// 转换输出
		ve.evaluate(context, writer, DigestUtils.md5Hex(templateContext.getBytes()), templateContext);
		return writer.toString();
	}
}