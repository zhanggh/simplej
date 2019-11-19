package com.haven.simplej.security.controller;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.security.model.AppCertRefModel;
import com.haven.simplej.security.service.AppCertRefService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 接入方对应的证书关联管理表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/appcertref")
public class AppCertRefController {
	
	@Autowired
	private AppCertRefService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<AppCertRefModel> get(@PathVariable("id") long id){

		AppCertRefModel model = new AppCertRefModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<AppCertRefModel>> search(@RequestBody AppCertRefModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody AppCertRefModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody AppCertRefModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody AppCertRefModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}