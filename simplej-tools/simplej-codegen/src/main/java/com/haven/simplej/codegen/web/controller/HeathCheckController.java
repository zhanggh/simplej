package com.haven.simplej.codegen.web.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: havenzhang
 * @Date: 2019/3/8 16:51
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HeathCheckController {

	@RequestMapping("/")
	public String index(HttpServletRequest req){

		log.info(JSON.toJSONString(req.getParameterMap(),true));

		return "success";
	}
}
