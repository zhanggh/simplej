package com.haven.simplej.security.enums;

/**
 * DESede 加密算法枚举
 * Created by haven.zhang on 2019/1/3.
 */
public enum DESedeAlgorithms {

	Desede_ECB_ZeroBytePadding("Desede/ECB/ZeroBytePadding"),
	Desede_ECB_ISO10126Padding("Desede/ECB/ISO10126Padding"),
	Desede_ECB_PKCS7Padding("Desede/ECB/PKCS7Padding"),
	Desede_ECB_PKCS5Padding("Desede/ECB/PKCS5Padding"),
	Desede_ECB_NoPadding("Desede/ECB/NoPadding"),

	Desede_CBC_PKCS5Padding("Desede/CBC/PKCS5Padding"),
	Desede_CBC_PKCS7Padding("Desede/CBC/PKCS7Padding"),
	Desede_CBC_ZeroBytePadding("Desede/CBC/ZeroBytePadding"),
	Desede_CBC_ISO10126Padding("Desede/CBC/ISO10126Padding"),
	Desede_CBC_NoPadding("Desede/CBC/NoPadding"),

	Desede_PCBC_PKCS5Padding("Desede/PCBC/PKCS5Padding"),
	Desede_PCBC_NoPadding("Desede/PCBC/NoPadding"),
	Desede_PCBC_ZeroBytePadding("Desede/PCBC/ZeroBytePadding"),
	Desede_PCBC_PKCS7Padding("Desede/PCBC/PKCS7Padding"),
	Desede_PCBC_ISO10126Padding("Desede/PCBC/ISO10126Padding"),

	Desede_CFB_PKCS5Padding("Desede/CFB/PKCS5Padding"),
	Desede_CFB_NoPadding("Desede/CFB/NoPadding"),
	Desede_CFB_ZeroBytePadding("Desede/CFB/ZeroBytePadding"),
	Desede_CFB_PKCS7Padding("Desede/CFB/PKCS7Padding"),
	Desede_CFB_ISO10126Padding("Desede/CFB/ISO10126Padding"),

	Desede_OFB_PKCS5Padding("Desede/OFB/PKCS5Padding"),
	Desede_OFB_NoPadding("Desede/OFB/NoPadding"),
	Desede_OFB_ZeroBytePadding("DES/OFB/ZeroBytePadding"),
	Desede_OFB_PKCS7Padding("Desede/OFB/PKCS7Padding"),
	Desede_OFB_ISO10126Padding("Desede/OFB/ISO10126Padding"),

	Desede_CTR_PKCS5Padding("Desede/CTR/PKCS5Padding"),
	Desede_CTR_NoPadding("Desede/CTR/NoPadding"),
	Desede_CTR_ZeroBytePadding("Desede/CTR/ZeroBytePadding"),
	Desede_CTR_PKCS7Padding("Desede/CTR/PKCS7Padding"),
	Desede_CTR_ISO10126Padding("Desede/CTR/ISO10126Padding");
	private String value;

	DESedeAlgorithms(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
