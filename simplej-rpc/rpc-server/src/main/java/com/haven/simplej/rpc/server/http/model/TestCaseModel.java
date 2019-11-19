package com.haven.simplej.rpc.server.http.model;

import lombok.Data;

/**
 * 测试用例信息
 * @author: havenzhang
 * @date: 2019/1/12 11:14
 * @version 1.0
 */
@Data
public class TestCaseModel {

	/**
	 * 测试用例名称
	 */
	private String testCaseName;

	/**
	 * 测试用例id
	 */
	private String testCaseId;

	/**
	 * 上一次响应的结果
	 */
	private String lastResponse;
}
