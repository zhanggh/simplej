package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.OptLogModel;
import com.haven.simplej.authen.service.OptLogService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 操作日志表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/optlog")
public class OptLogController  extends AbstractBaseAuthenController{
	
	@Autowired
	private OptLogService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<OptLogModel> get(@PathVariable("id") long id){

		OptLogModel model = new OptLogModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<OptLogModel>> search(@RequestBody OptLogModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody OptLogModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody OptLogModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody OptLogModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}