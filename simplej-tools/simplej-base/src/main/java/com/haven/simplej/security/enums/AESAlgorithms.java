package com.haven.simplej.security.enums;

/**
 * AES 加密算法枚举
 * Created by haven.zhang on 2019/1/3.
 */
public enum AESAlgorithms {

	AES_ECB_ZeroBytePadding("AES/ECB/ZeroBytePadding"),
	AES_ECB_ISO10126Padding("AES/ECB/ISO10126Padding"),
	AES_ECB_PKCS7Padding("AES/ECB/PKCS7Padding"),
	AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),
	AES_ECB_NoPadding("AES/ECB/NoPadding"),

	AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
	AES_CBC_PKCS7Padding("AES/CBC/PKCS7Padding"),
	AES_CBC_ZeroBytePadding("AES/CBC/ZeroBytePadding"),
	AES_CBC_ISO10126Padding("AES/CBC/ISO10126Padding"),
	AES_CBC_NoPadding("AES/CBC/NoPadding"),

	AES_PCBC_PKCS5Padding("AES/PCBC/PKCS5Padding"),
	AES_PCBC_NoPadding("AES/PCBC/NoPadding"),
	AES_PCBC_ZeroBytePadding("AES/PCBC/ZeroBytePadding"),
	AES_PCBC_PKCS7Padding("AES/PCBC/PKCS7Padding"),
	AES_PCBC_ISO10126Padding("AES/PCBC/ISO10126Padding"),

	AES_CFB_PKCS5Padding("AES/CFB/PKCS5Padding"),
	AES_CFB_NoPadding("AES/CFB/NoPadding"),
	AES_CFB_ZeroBytePadding("AES/CFB/ZeroBytePadding"),
	AES_CFB_PKCS7Padding("AES/CFB/PKCS7Padding"),
	AES_CFB_ISO10126Padding("AES/CFB/ISO10126Padding"),

	AES_OFB_PKCS5Padding("AES/OFB/PKCS5Padding"),
	AES_OFB_NoPadding("AES/OFB/NoPadding"),
	AES_OFB_ZeroBytePadding("DES/OFB/ZeroBytePadding"),
	AES_OFB_PKCS7Padding("AES/OFB/PKCS7Padding"),
	AES_OFB_ISO10126Padding("AES/OFB/ISO10126Padding"),

	AES_CTR_PKCS5Padding("AES/CTR/PKCS5Padding"),
	AES_CTR_NoPadding("AES/CTR/NoPadding"),
	AES_CTR_ZeroBytePadding("AES/CTR/ZeroBytePadding"),
	AES_CTR_PKCS7Padding("AES/CTR/PKCS7Padding"),
	AES_CTR_ISO10126Padding("AES/CTR/ISO10126Padding");

	private String value;

	AESAlgorithms(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
