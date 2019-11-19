package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.RoleMenuRefModel;
import com.haven.simplej.authen.service.RoleMenuRefService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 角色与菜单关联表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/rolemenuref")
public class RoleMenuRefController extends AbstractBaseAuthenController {
	
	@Autowired
	private RoleMenuRefService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<RoleMenuRefModel> get(@PathVariable("id") long id){

		RoleMenuRefModel model = new RoleMenuRefModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<RoleMenuRefModel>> search(@RequestBody RoleMenuRefModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody RoleMenuRefModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody RoleMenuRefModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody RoleMenuRefModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}