package com.haven.simplej.rpc.server.http.helper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.http.model.Http2RpcModel;
import com.haven.simplej.rpc.server.http.model.TestCaseModel;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * rpc 自助测试帮助类
 * @author: havenzhang
 * @date: 2018/10/12 16:02
 * @version 1.0
 */
@Slf4j
public class RpcTestCaseHelper {


	/**
	 * 测试用例参数分隔符
	 */
	public static final String CASE_PARAM_SPLIT_SYMBOL = ",";
	/**
	 * 测试用例存储文件
	 */
	public static final String TEST_CASE_FILE = "logs/test_case.log";

	/**
	 * 判断测试用例是否已经存在
	 * @param caseId 用例编号
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean isExist(String caseId) throws IOException {
		File caseFile = new File(TEST_CASE_FILE);
		boolean exist = false;
		if (caseFile.exists()) {
			List<String> lines = FileUtils.readLines(caseFile, RpcConstants.DEFAULT_ENCODE);
			for (String line : lines) {
				String[] params = StringUtil.split(line, CASE_PARAM_SPLIT_SYMBOL);
				if (params == null || params.length != 3) {
					continue;
				}
				if (StringUtil.equalsIgnoreCase(params[0], caseId)) {
					exist = true;
				}
			}
		} else {
			return false;
		}
		return exist;
	}


	/**
	 * 测试用例参数文件
	 */
	public static String getCaseFileName(String caseId) {
		return "logs/" + caseId;
	}

	/**
	 * 测试用例响应结果文件
	 */
	public static String getCaseRespFileName(String caseId) {
		return "logs/" + caseId + "_last_response";
	}


	/**
	 * 保存测试用例
	 */
	public static boolean saveCase(Http2RpcModel model, TestCaseModel testCaseModel, HttpServletRequest request)
			throws IOException {
		File caseFile = new File(RpcTestCaseHelper.TEST_CASE_FILE);
		String caseId = DigestUtils.md2Hex(testCaseModel.getTestCaseName());
		if (RpcTestCaseHelper.isExist(caseId)) {
			log.info("case is exist");
			return true;
		}

		StringBuilder sb = new StringBuilder(caseId);
		sb.append(RpcTestCaseHelper.CASE_PARAM_SPLIT_SYMBOL).append(testCaseModel.getTestCaseName());
		sb.append(RpcTestCaseHelper.CASE_PARAM_SPLIT_SYMBOL).append(model.getMethodId()).append("\n");

		Map<String, String> paramMap = Maps.newHashMap();
		request.getParameterMap().forEach((k, v) -> paramMap.put(k.replaceAll("\\.", "_"), request.getParameter(k)));

		// 保存测试用例
		FileUtils.writeStringToFile(caseFile, sb.toString(), RpcConstants.DEFAULT_ENCODE, true);

		File caseHtml = new File(RpcTestCaseHelper.getCaseFileName(caseId));
		FileUtils.writeStringToFile(caseHtml, JSON.toJSONString(paramMap, true), RpcConstants.DEFAULT_ENCODE);

		File caseResp = new File(RpcTestCaseHelper.getCaseRespFileName(caseId));
		FileUtils.writeStringToFile(caseResp, testCaseModel.getLastResponse(), RpcConstants.DEFAULT_ENCODE);
		return true;
	}

	/**
	 * 查询测试用例列表
	 */
	public static List<TestCaseModel> queryCaseList(Http2RpcModel model) throws IOException {
		File caseFile = new File(RpcTestCaseHelper.TEST_CASE_FILE);
		List<TestCaseModel> caseList = Lists.newArrayList();
		if (caseFile.exists()) {
			List<String> lines = FileUtils.readLines(caseFile, RpcConstants.DEFAULT_ENCODE);
			if (CollectionUtil.isEmpty(lines)) {
				return caseList;
			}
			for (String line : lines) {
				String[] params = StringUtil.split(line, RpcTestCaseHelper.CASE_PARAM_SPLIT_SYMBOL);
				if (params == null || params.length < 3) {
					continue;
				}
				if (StringUtil.equalsIgnoreCase(model.getMethodId(), params[2])) {
					TestCaseModel model1 = new TestCaseModel();
					model1.setTestCaseId(params[0]);
					model1.setTestCaseName(params[1]);
					caseList.add(model1);
				}
			}
		}
		return caseList;
	}

	/**
	 * 读文件内容
	 * @param fileName 文件名
	 */
	public static String readFile(String fileName) throws IOException {
		File caseFile = new File(fileName);
		String paramJson = null;
		if (caseFile.exists()) {
			paramJson = FileUtils.readFileToString(caseFile, RpcConstants.DEFAULT_ENCODE);
		}
		return paramJson;
	}
}
