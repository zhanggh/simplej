package com.haven.simplej.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by haven.zhang on 2019/1/8.
 */
@Setter
@Getter
public class UserInfo {
	String name;
	int age;
	public UserInfo(String name,int age){
		this.name = name;
		this.age = age;
	}
}
