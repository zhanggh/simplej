package com.haven.simplej.codegen.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

/**
 * @author haven.zhang
 * @date 2019/1/14.
 */
@Controller
@RequestMapping("/")
@Slf4j
public class CommonController {

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		model.put("time", new Date());
		return "views/create-project";
	}
}
