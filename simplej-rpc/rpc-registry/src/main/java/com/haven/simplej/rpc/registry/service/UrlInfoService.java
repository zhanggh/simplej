package com.haven.simplej.rpc.registry.service;

import com.haven.simplej.db.base.BaseService;
import com.haven.simplej.rpc.model.UrlInfo;
import com.haven.simplej.rpc.model.UrlListInfo;
import com.haven.simplej.rpc.registry.model.UrlInfoModel;

/**
 * web服务url信息 Service
 */
public interface UrlInfoService extends BaseService<UrlInfoModel> {
	/**
	 * 注册url信息
	 * @param urlInfo
	 */
	boolean regitster(UrlInfo urlInfo);

	/**
	 * 注销url服务
	 * @param urlInfo
	 */
	boolean unRegitster(UrlInfo urlInfo);

	/**
	 * 查询url服务信息
	 * @param namespace
	 * @return
	 */
	UrlListInfo getUrlList(String namespace);

	/**
	 * 心跳更新
	 * @param urlInfo
	 * @return
	 */
	boolean heartbeat(UrlInfo urlInfo);
}