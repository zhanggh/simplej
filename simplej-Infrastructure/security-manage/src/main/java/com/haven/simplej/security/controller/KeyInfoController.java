package com.haven.simplej.security.controller;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.security.model.KeyInfoModel;
import com.haven.simplej.security.service.KeyInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 秘钥信息表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/keyinfo")
public class KeyInfoController {
	
	@Autowired
	private KeyInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<KeyInfoModel> get(@PathVariable("id") long id){

		KeyInfoModel model = new KeyInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<KeyInfoModel>> search(@RequestBody KeyInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody KeyInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody KeyInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody KeyInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}