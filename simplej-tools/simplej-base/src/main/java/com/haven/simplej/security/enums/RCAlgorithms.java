package com.haven.simplej.security.enums;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public enum RCAlgorithms {


	RC2("RC2"),
	RC4("RC4");

	private String value;

	RCAlgorithms(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}