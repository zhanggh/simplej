package com.haven.simplej.groovy;

import com.haven.simplej.script.groovy.GroovyFactory;

/**
 * @Author: havenzhang
 * @Date: 2019/3/6 21:49
 * @Version 1.0
 */
public class GroovyTest {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Object obj = GroovyFactory.getFactory().getObject("D:\\tencent\\payment_project_codes\\IaPayPreSvr_proj\\branches\\iapay-upi\\iapay-upi_V3.0D0001\\src\\main\\groovy\\Person.groovy");

		System.out.println(obj);
	}
}
