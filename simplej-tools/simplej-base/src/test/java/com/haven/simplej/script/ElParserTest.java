package com.haven.simplej.script;

import org.junit.Test;

/**
 * @author: havenzhang
 * @date: 2019/4/18 20:50
 * @version 1.0
 */
public class ElParserTest {

	@Test
	public void test(){
		String expr = "'hello'+' word'";
		String value  = ExpressionParserUtil.parser(expr,null,String.class);
		System.out.println(value);
	}
}
