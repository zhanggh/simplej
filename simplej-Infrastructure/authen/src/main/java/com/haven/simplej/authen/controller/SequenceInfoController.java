package com.haven.simplej.authen.controller;

import java.util.*;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.authen.model.SequenceInfoModel;
import com.haven.simplej.authen.service.SequenceInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 序列号生成器表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/sequenceinfo")
public class SequenceInfoController extends AbstractBaseAuthenController {
	
	@Autowired
	private SequenceInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<SequenceInfoModel> get(@PathVariable("id") long id){

		SequenceInfoModel model = new SequenceInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<SequenceInfoModel>> search(@RequestBody SequenceInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody SequenceInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody SequenceInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody SequenceInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}