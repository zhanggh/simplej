package com.haven.simplej.security.enums;

/**
 * DES 加密算法枚举
 * Created by haven.zhang on 2019/1/3.
 */
public enum DESAlgorithms {

	DES_ECB_ZeroBytePadding("DES/ECB/ZeroBytePadding"),
	DES_ECB_ISO10126Padding("DES/ECB/ISO10126Padding"),
	DES_ECB_PKCS5Padding("DES/ECB/PKCS5Padding"),
	DES_ECB_PKCS7Padding("DES/ECB/PKCS7Padding"),
	DES_ECB_NoPadding("DES/ECB/NoPadding"),

	DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),
	DES_CBC_NoPadding("DES/CBC/NoPadding"),
	DES_CBC_ZeroBytePadding("DES/CBC/ZeroBytePadding"),
	DES_CBC_PKCS7Padding("DES/CBC/PKCS7Padding"),
	DES_CBC_ISO10126Padding("DES/CBC/ISO10126Padding"),

	DES_PCBC_PKCS5Padding("DES/PCBC/PKCS5Padding"),
	DES_PCBC_NoPadding("DES/PCBC/NoPadding"),
	DES_PCBC_ZeroBytePadding("DES/PCBC/ZeroBytePadding"),
	DES_PCBC_PKCS7Padding("DES/PCBC/PKCS7Padding"),
	DES_PCBC_ISO10126Padding("DES/PCBC/ISO10126Padding"),

	DES_CFB_PKCS5Padding("DES/CFB/PKCS5Padding"),
	DES_CFB_NoPadding("DES/CFB/NoPadding"),
	DES_CFB_ZeroBytePadding("DES/CFB/ZeroBytePadding"),
	DES_CFB_PKCS7Padding("DES/CFB/PKCS7Padding"),
	DES_CFB_ISO10126Padding("DES/CFB/ISO10126Padding"),

	DES_OFB_PKCS5Padding("DES/OFB/PKCS5Padding"),
	DES_OFB_NoPadding("DES/OFB/NoPadding"),
	DES_OFB_ZeroBytePadding("DES/OFB/ZeroBytePadding"),
	DES_OFB_PKCS7Padding("DES/OFB/PKCS7Padding"),
	DES_OFB_ISO10126Padding("DES/OFB/ISO10126Padding"),

	DES_CTR_PKCS5Padding("DES/CTR/PKCS5Padding"),
	DES_CTR_NoPadding("DES/CTR/NoPadding"),
	DES_CTR_ZeroBytePadding("DES/CTR/ZeroBytePadding"),
	DES_CTR_PKCS7Padding("DES/CTR/PKCS7Padding"),
	DES_CTR_ISO10126Padding("DES/CTR/ISO10126Padding");

	private String value;

	DESAlgorithms(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
