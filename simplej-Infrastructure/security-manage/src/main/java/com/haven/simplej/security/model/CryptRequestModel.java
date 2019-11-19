package com.haven.simplej.security.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: havenzhang
 * @Date: 2019/4/7 22:44
 * @Version 1.0
 */
@Data
public class CryptRequestModel {

	/**
	 * 秘钥id
	 */
	private Long keyId;
	/**
	 * appid
	 */
	private String appId;

	/**
	 * 1-加密，2-解密
	 */
	private int mode;

	/**
	 * 待加密或者解密的报文
	 */
	private List<String> msg;

	/**
	 * 加密或者解密后，返回数据的编码
	 */
	private String respEncode;
}
