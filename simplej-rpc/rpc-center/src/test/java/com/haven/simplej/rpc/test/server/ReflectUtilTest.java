package com.haven.simplej.rpc.test.server;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.center.service.ISequenceService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author: havenzhang
 * @date: 2019/10/12 12:38
 * @version 1.0
 */
public class ReflectUtilTest {

	public static void main(String[] args) throws NoSuchMethodException {
		Method method = ISequenceService.class.getMethod("getNextShortSeqNo", String.class, String.class, int.class);

		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] annotation : annotations) {
			for (Annotation an : annotation) {
				if (an.annotationType().equals(RpcParam.class)) {
					System.out.println(an.annotationType().getName());
				}

			}
		}
		System.out.println(JSON.toJSONString(annotations));
	}
}
