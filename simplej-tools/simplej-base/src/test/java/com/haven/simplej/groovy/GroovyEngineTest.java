package com.haven.simplej.groovy;

import com.haven.simplej.script.groovy.GroovyExecutor;
import com.haven.simplej.script.groovy.GroovyFactory;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: havenzhang
 * @Date: 2019/3/11 23:00
 * @Version 1.0
 */
@Slf4j
public class GroovyEngineTest {

	@Test
	public void scriptParseTest1() throws Exception {
		String scriptText1 = "def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;};" + "def getTime(){return date.getTime();}";
		String scriptText2 = "def getTime(){return new Date().getTime();}; return getTime()";
		String scriptText3 = "def f2(){return 'Hello World! I am ' + name+' time:'+date.getTime()} ; return f2()";
		String scriptText4 = "1+1";
		Binding binding = new Binding();
		binding.setVariable("date", new Date());
		binding.setVariable("name", "zhangsan");
		String alias = "script01";
		long start = System.currentTimeMillis();
		Object resp = GroovyExecutor.getExecutor().runScript(scriptText3, binding, alias);
		long end = System.currentTimeMillis();
		System.out.println(resp + ",cost:" + (end - start));

		for (int i = 0; i < 10; i++) {
			start = System.currentTimeMillis();
			resp = GroovyExecutor.getExecutor().runScript(scriptText3, binding, alias);
			end = System.currentTimeMillis();
			System.out.println(resp + ",cost" + i + ":" + (end - start));
		}


		resp = GroovyExecutor.getExecutor().runScript(scriptText2, binding, alias);
		System.out.println(resp);

	}

}
