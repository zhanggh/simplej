package com.haven.simplej.authen.match;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 21:38
 * @Version 1.0
 */
public class UrlMachTest {


	@Test
	public void matchTest() {
		String url = "/get/33xxxx";
		Pattern p = Pattern.compile("/get/[a-zA-z0-9]*/test");
		Matcher m = p.matcher(url);
		System.out.println(m.matches());
	}
}
