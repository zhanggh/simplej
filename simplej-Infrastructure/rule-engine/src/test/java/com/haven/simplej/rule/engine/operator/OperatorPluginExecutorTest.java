package com.haven.simplej.rule.engine.operator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haven.simplej.io.FileUtil;
import com.haven.simplej.rule.engine.plugin.OperatorPluginExecutor;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 操作符执行测试类
 * @author: havenzhang
 * @date: 2019/4/18 22:04
 * @version 1.0
 */
public class OperatorPluginExecutorTest {


	@Test
	public void compareTest(){
		String javaCode = "org.apache.commons.lang3.StringUtils#compare";

		List params = Lists.newArrayList();
		params.add("4444444");
		params.add("4344444");
		int result = OperatorPluginExecutor.executeUtil(javaCode,params);
		System.out.println(result);
	}

	@Test
	public void elTest(){
		String elCode = "#value1 > #value2";
		String elCode2 = " (#value1 - #value2)* 3/ #value4";

		Map<String,Object> params = Maps.newHashMap();
		params.put("value1",2);
		params.put("value2",33);
		params.put("value3",4);
		params.put("value4",4);
		params.put("value5",2);
		params.put("value6",3);
		boolean result = OperatorPluginExecutor.executeEl(elCode,params,Boolean.class);
		System.out.println(result);
		int result2 = OperatorPluginExecutor.executeEl(elCode2,params,Integer.class);
		System.out.println(result2);

		String elCode3 = "#date1.after(#date2)";
		params.put("date1",new Date());
		params.put("date2", DateUtils.StringToDate("20180101","yyyyMMdd"));
		boolean result3 = OperatorPluginExecutor.executeEl(elCode3,params,Boolean.class);
		System.out.println(result3);
	}


	@Test
	public void elTest2(){
		String elCode = "#param1.after(#param2) and #param1.before(#param3) ";
		Map<String,Object> params = Maps.newHashMap();
		params.put("param1",new Date());
		params.put("param2",DateUtil.addDays(new Date(),3));
		params.put("param3",DateUtil.addDays(new Date(),3));
		Boolean result3 = OperatorPluginExecutor.executeEl(elCode,params, Boolean.class);
		System.out.println(result3);
	}


	@Test
	public void elStringTest(){
		String elCode = "#param1!=null and !#param1.equals(#param2)   ";
		elCode = "#param1==#param2 ";
//		elCode.trim().length()
//		elCode.compareTo("x")>0
//		"".equals()
		Map<String,Object> params = Maps.newHashMap();
		params.put("param1",2);
		params.put("param2",2.00);
		params.put("param3",DateUtil.addDays(new Date(),3));
		Boolean result3 = OperatorPluginExecutor.executeEl(elCode,params, Boolean.class);
		System.out.println(result3);
	}

	@Test
	public void groovyTest() throws IOException {
		String script = FileUtil.getResourceStr("/templates/script_template_test");

		int result = OperatorPluginExecutor.executeGroovy(1L,script,Maps.newHashMap());

		System.out.println(result);
	}


	@Test
	public void dateTest(){
		Date date = DateUtil.addDays(new Date(),0);
		Date date1 =	new Date();
		System.out.println(date.getTime());
		System.out.println(date1.getTime());

		System.out.println(date.getTime() ==date1.getTime());

		System.out.println(org.apache.commons.lang3.time.DateUtils.truncatedCompareTo(date,new Date(),10));



		String dateString = DateUtils.dateToString(date,"yyyyMMddHHmmss");
		System.out.println(dateString);
	}


	@Test
	public void elCollectionTest(){
		String elCode = "#param1==null or #param1.size()==0";

		Map<String,Object> params = Maps.newHashMap();
		params.put("param1",2);
		params.put("param2",2.00);
		params.put("param3",DateUtil.addDays(new Date(),3));
		params.size();
		List list = Lists.newArrayList();
		list.size();
		Set set = Sets.newHashSet();
		set.size();
		Map map = Maps.newHashMap();
		params.put("param1",map);
		Boolean result3 = OperatorPluginExecutor.executeEl(elCode,params, Boolean.class);
		System.out.println(result3);
	}

}
