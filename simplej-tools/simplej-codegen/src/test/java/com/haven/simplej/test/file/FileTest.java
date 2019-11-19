package com.haven.simplej.test.file;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 23:00
 * @Version 1.0
 */
public class FileTest {

	@Test
	public void dirTest(){
		System.out.println(System.getProperty("user.dir"));

	}

	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		String src = "D:\\dev_codes\\java_projects\\simplej-tools\\demo2\\src\\main\\resources\\static";
		String dest = "D:\\个人资料\\太空计划\\yingjienet-H-ui.admin.Pro-V1.0.6\\static";
		FileUtils.copyDirectory(new File(src),new File(dest));
		System.out.println("-----------------------");
	}
}
