package com.haven.simplej.rpc.valid;

import com.google.common.collect.Lists;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.model.RpcErrorInfo;
import com.haven.simplej.rpc.validate.RpcParamValidator;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/5/14 22:57
 * @version 1.0
 */
public class ParameterValidTest {

	@Test
	public void test1(){
//		Method method= ReflectionUtil.getMethod(ParameterValidTest.class,"test2",String.class,String.class);
		Method method= ReflectionUtil.getMethod(ParameterValidTest.class,"test3",Person.class,Person.class);
		Annotation[][]  ans = method.getParameterAnnotations();
		RpcParamValidator.validate(method,new Object[]{new Person(),null});
		System.out.println(ans);
	}

	public void test2(@RpcParam(required = false,alias = "param") String param,@RpcParam String param2){

	}

	public void test3(@RpcStruct Person person, Person person2){

	}


	public void test4(int param1,Integer param2,float param3){
		System.out.println("test4");
	}


	@Test
	public void test5() throws InvocationTargetException, IllegalAccessException {
		Method method= ReflectionUtil.getMethod(ParameterValidTest.class,"test4",int.class,Integer.class,float.class);
		for (Class<?> parameterType : method.getParameterTypes()) {

			if(parameterType.equals(Integer.class)){
				System.out.println(parameterType.getName());
			}
			if(parameterType.equals(int.class)){
				System.out.println(parameterType.getName());
			}
			if(parameterType.equals(float.class)){
				System.out.println(parameterType.getName());
			}
		}
		List<Object> list = Lists.newArrayList();
		list.add(1);
		list.add(null);
		list.add(3);
		method.invoke(new ParameterValidTest(),list.toArray());
	}
}
