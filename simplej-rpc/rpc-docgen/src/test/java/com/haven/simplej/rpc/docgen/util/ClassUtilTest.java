package com.haven.simplej.rpc.docgen.util;

import java.util.Set;

/**
 * @author: havenzhang
 * @date: 2019/1/15 22:12
 * @version 1.0
 */
public class ClassUtilTest {

	public static void main(String[] args) {
		Set<Class> classList = ClassUtil.getClasses("com.haven.simplej.property");
		System.out.println(classList);
		Object[] ts = classList.toArray();
		for (Object t : ts) {
			Class<?> tt = (Class<?>) t;
			System.out.println(tt.getName());
		}
	}
}
