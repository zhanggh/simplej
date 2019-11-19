package com.haven.simplej.security.manager;

import com.haven.simplej.security.model.CryptRequestModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 加解密管理器 负责各种类型的加密、解密
 * @Author: havenzhang
 * @Date: 2019/4/7 22:49
 * @Version 1.0
 */
@Component
public class CryptManager {

	/**
	 * 加密
	 * @param requestModel
	 * @return
	 */
	public List<String> encrypt(CryptRequestModel requestModel){

		return null;
	}

	/**
	 * 解密
	 * @param requestModel
	 * @return
	 */
	public List<String> decrypt(CryptRequestModel requestModel){

		return null;
	}
}
