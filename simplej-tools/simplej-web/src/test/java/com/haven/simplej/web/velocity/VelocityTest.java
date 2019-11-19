package com.haven.simplej.web.velocity;

import com.google.common.collect.Maps;
import com.haven.simplej.io.FileUtil;
import com.haven.simplej.script.VelocityUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: havenzhang
 * @Date: 2019/4/8 22:27
 * @Version 1.0
 */
public class VelocityTest {

	@Test
	public void test() throws IOException {
		String template = FileUtil.getResourceStr("/template/error_template.html");
		Map<String, Object> params = Maps.newHashMap();
		params.put("respMsg","hello");
		String msg = VelocityUtil.getText(template,params);

		System.out.println("-------------------------------");
		System.out.println(msg);
	}
}
