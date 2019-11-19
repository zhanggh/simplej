package com.haven.simplej.security.controller;

import com.haven.simplej.authen.annotation.AuthAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.security.model.CertificateInfoModel;
import com.haven.simplej.security.service.CertificateInfoService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 接入方对应的key关联管理表 Controller
 */
@Slf4j
@AuthAccess
@RestController
@RequestMapping("/authen/certificateinfo")
public class CertificateInfoController {
	
	@Autowired
	private CertificateInfoService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<CertificateInfoModel> get(@PathVariable("id") long id){

		CertificateInfoModel model = new CertificateInfoModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<CertificateInfoModel>> search(@RequestBody CertificateInfoModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody CertificateInfoModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody CertificateInfoModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody CertificateInfoModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}