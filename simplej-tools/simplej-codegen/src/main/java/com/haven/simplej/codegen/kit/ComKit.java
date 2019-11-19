package com.haven.simplej.codegen.kit;

import java.io.File;
import java.io.IOException;

import com.google.common.base.CaseFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComKit {

	private ComKit() {
		super();
	}

	public static String underlineToLowerCamel(String str) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str.toLowerCase());
	}

	public static String underlineToUpperCamel(String str) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str.toLowerCase());
	}

	public static String lowerToUpperCamel(String str) {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, str);
	}

	public static boolean createNewFile(File file) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			return parent.mkdirs();
		}
		try {
			return file.createNewFile();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

}
