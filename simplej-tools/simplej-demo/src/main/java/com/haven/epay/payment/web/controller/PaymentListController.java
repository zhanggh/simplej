package com.haven.epay.payment.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.haven.epay.payment.model.PaymentListModel;
import com.haven.epay.payment.service.PaymentListService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * 交易流水表 Controller
 */
@Slf4j
@RestController
@RequestMapping("/payment/paymentlist")
public class PaymentListController {
	
	@Autowired
	private PaymentListService service;

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<PaymentListModel> get(@PathVariable("id") long id){

		PaymentListModel model = new PaymentListModel();
    	model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public JsonResponse<PageInfo<PaymentListModel>> search(@RequestBody PaymentListModel model){

		return this.service.search(model);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public JsonResponse<Integer> remove(@RequestBody PaymentListModel model){

		return ResponseBuilder.build(this.service.remove(model) != 0);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public JsonResponse<Long> save(@RequestBody PaymentListModel model){

        this.service.save(model);
		return ResponseBuilder.build(model.getId() != null);
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public JsonResponse<Long> update(@RequestBody PaymentListModel model){

        this.service.update(model);
		return ResponseBuilder.build(model.getId() != null);
	}
}