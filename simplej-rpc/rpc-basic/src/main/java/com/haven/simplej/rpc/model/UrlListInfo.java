package com.haven.simplej.rpc.model;

import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/9/27 19:58
 * @version 1.0
 */
@Data
public class UrlListInfo {

	/**
	 * 摘要信息，用于与本地判断信息是否发生变更
	 */
	private String md5;


	/**
	 * url 列表
	 */
	private List<UrlInfo> urlInfoList;
}
