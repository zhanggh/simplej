package com.haven.simplej.security.controller;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.security.model.AppKeyRefModel;
import com.haven.simplej.security.service.AppKeyRefService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 操作日志表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/appkeyref")
public class AppKeyRefController {
	
	@Autowired
	private AppKeyRefService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<AppKeyRefModel> get(@PathVariable("id") long id){

		AppKeyRefModel model = new AppKeyRefModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<AppKeyRefModel>> search(@RequestBody AppKeyRefModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody AppKeyRefModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody AppKeyRefModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody AppKeyRefModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}