package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.DepartmentInfoModel;
import com.haven.simplej.authen.service.DepartmentInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 部门信息 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/departmentinfo")
public class DepartmentInfoController extends AbstractBaseAuthenController{
	
	@Autowired
	private DepartmentInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<DepartmentInfoModel> get(@PathVariable("id") long id){

		DepartmentInfoModel model = new DepartmentInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<DepartmentInfoModel>> search(@RequestBody DepartmentInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody DepartmentInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody DepartmentInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody DepartmentInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}