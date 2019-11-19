package com.haven.simplej.rpc.mock.model;

import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/9/27 19:54
 * @version 1.0
 */
@Data
public class MockInfo {

	/**
	 * 摘要信息，用于与本地判断信息是否发生变更
	 */
	private String md5;

	/**
	 * mock的方法信息
	 */
	private List<MockMethod> mockMethodList;
}
