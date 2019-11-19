package com.haven.simplej.test.common;

import com.haven.simplej.exception.UncheckedException;

import java.util.Random;

/**
 * Created by haven.zhang on 2018/12/28.
 */
public class StringTest {
	public static void main(String[] args) {
		String str = "xxx@{sdfsdf}xxx";
		System.out.println(str.replaceAll("@\\{","\\$\\{"));

		try{
			if(new Random().nextBoolean()){
				throw new UncheckedException("test error");
			}
		}finally {
			System.out.println("finally---------------");
		}
	}
}
