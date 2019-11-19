package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.UserInfoModel;
import com.haven.simplej.authen.service.UserInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 用户信息表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/userinfo")
public class UserInfoController extends AbstractBaseAuthenController {
	
	@Autowired
	private UserInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<UserInfoModel> get(@PathVariable("id") long id){

		UserInfoModel model = new UserInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<UserInfoModel>> search(@RequestBody UserInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody UserInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody UserInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody UserInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}