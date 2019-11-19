package com.haven.simplej.rpc.server.http.controller;

import com.google.common.collect.Maps;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.rpc.server.http.helper.RpcTestCaseHelper;
import com.haven.simplej.rpc.server.http.model.Http2RpcModel;
import com.haven.simplej.rpc.server.http.model.TestCaseModel;
import com.haven.simplej.script.FreeMarkerUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 测试用例管理控制类
 * @author: havenzhang
 * @date: 2018/9/28 21:49
 * @version 1.0
 */
@Controller
@RequestMapping("/rpc")
@Slf4j
public class RpcCaseController {

	/**
	 * 保存测试用例
	 */
	@ResponseBody
	@RequestMapping("/case/save")
	public JsonResponse<String> saveCase(Http2RpcModel model, TestCaseModel testCaseModel, HttpServletRequest request)
			throws IOException {

		RpcTestCaseHelper.saveCase(model, testCaseModel, request);
		return ResponseBuilder.build(true);
	}


	/**
	 * 查询测试用例列表
	 */
	@RequestMapping("/case/list")
	public void queryCaseList(Http2RpcModel model, HttpServletResponse response) throws IOException,
			TemplateException {

		List<TestCaseModel> caseList = RpcTestCaseHelper.queryCaseList(model);
		if (CollectionUtil.isNotEmpty(caseList)) {
			Map<String, Object> data = Maps.newHashMap();
			data.put("caseList", caseList);
			String resp = FreeMarkerUtil.getText("test_case_list.ftl", data);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resp);
			return;
		}
		response.getWriter().write("fail");
	}

	/**
	 * 获取测试用例详情
	 */
	@ResponseBody
	@RequestMapping("/case/detail")
	public String getCaseParams(TestCaseModel model) throws IOException {
		String resp = RpcTestCaseHelper.readFile(RpcTestCaseHelper.getCaseFileName(model.getTestCaseId()));
		if (StringUtil.isNotEmpty(resp)) {
			return resp;
		}
		return "fail";
	}

	/**
	 * 获取上一次响应的结果
	 */
	@ResponseBody
	@RequestMapping("/case/response")
	public String getCaseRespParams(TestCaseModel model) throws IOException {
		String resp = RpcTestCaseHelper.readFile(RpcTestCaseHelper.getCaseRespFileName(model.getTestCaseId()));
		if (StringUtil.isNotEmpty(resp)) {
			return resp;
		}
		return "fail";
	}

}
