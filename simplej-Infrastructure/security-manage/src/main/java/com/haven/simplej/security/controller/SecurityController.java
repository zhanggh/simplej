package com.haven.simplej.security.controller;

import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.security.manager.CryptManager;
import com.haven.simplej.security.model.CryptRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 安全中心在线加解密、签名验签、颁发证书等
 * @Author: havenzhang
 * @Date: 2019/4/7 22:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/security")
public class SecurityController {

	@Autowired
	private CryptManager cryptManager;

	@RequestMapping(value = "/encrypt", method = RequestMethod.POST)
	public JsonResponse<List<String>> encrypt(@RequestBody CryptRequestModel requestModel){
		List<String> msg = cryptManager.encrypt(requestModel);
		return ResponseBuilder.build(msg);
	}

	@RequestMapping(value = "/decrypt", method = RequestMethod.POST)
	public JsonResponse<List<String>> decrypt(@RequestBody CryptRequestModel requestModel){
		List<String> msg = cryptManager.decrypt(requestModel);
		return ResponseBuilder.build(msg);
	}
}
