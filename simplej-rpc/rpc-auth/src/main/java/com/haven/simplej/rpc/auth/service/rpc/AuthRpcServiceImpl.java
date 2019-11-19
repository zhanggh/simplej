package com.haven.simplej.rpc.auth.service.rpc;

import com.haven.simplej.rpc.auth.model.AuthResponseModel;
import com.haven.simplej.rpc.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: havenzhang
 * @date: 2018/10/13 19:55
 * @version 1.0
 */
@Service
@Slf4j
public class AuthRpcServiceImpl implements AuthService {
	@Override
	public AuthResponseModel queryAuthInfo(String namespace) {
		return null;
	}
}
