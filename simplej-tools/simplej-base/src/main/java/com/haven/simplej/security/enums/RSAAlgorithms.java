package com.haven.simplej.security.enums;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public enum  RSAAlgorithms {


	RSA_ECB_PKCS1Padding("RSA/ECB/PKCS1Padding"),
	RSA_ECB_OAEPWithSHA1("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
	RSA_ECB_OAEPWithSHA256("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),
	RSA_ECB_NoPadding("RSA/ECB/NoPadding");

	private String value;

	RSAAlgorithms(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}