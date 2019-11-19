package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.UserRoleRefModel;
import com.haven.simplej.authen.service.UserRoleRefService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 用户角色关联表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/userroleref")
public class UserRoleRefController extends AbstractBaseAuthenController {
	
	@Autowired
	private UserRoleRefService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<UserRoleRefModel> get(@PathVariable("id") long id){

		UserRoleRefModel model = new UserRoleRefModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<UserRoleRefModel>> search(@RequestBody UserRoleRefModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody UserRoleRefModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody UserRoleRefModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody UserRoleRefModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}