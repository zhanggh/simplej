package com.haven.simplej.rpc.center.web;

import com.haven.simplej.rpc.annotation.RpcMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: havenzhang
 * @date: 2018/5/21 23:43
 * @version 1.0
 */
@Controller
public class RpcServerController {

	@RequestMapping(value = "/rpc/server/index",method = RequestMethod.GET)
	@RpcMethod(timeout = 3000)
	public String index(String name, HttpServletRequest request){

		return "hello";
	}
}
