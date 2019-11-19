package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.GroupInfoModel;
import com.haven.simplej.authen.service.GroupInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 小组信息，一个部门下面对应多个小组 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/groupinfo")
public class GroupInfoController extends AbstractBaseAuthenController {
	
	@Autowired
	private GroupInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<GroupInfoModel> get(@PathVariable("id") long id){

		GroupInfoModel model = new GroupInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<GroupInfoModel>> search(@RequestBody GroupInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody GroupInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody GroupInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody GroupInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}