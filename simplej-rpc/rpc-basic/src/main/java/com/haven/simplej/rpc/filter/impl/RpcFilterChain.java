package com.haven.simplej.rpc.filter.impl;

import com.google.common.collect.Lists;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;

/**
 * 责任链执行器
 * @author: havenzhang
 * @date: 2019/1/26 19:28
 * @version 1.0
 */
@Slf4j
public class RpcFilterChain implements FilterChain {

	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters = Lists.newArrayList();

	/**
	 * 当前链路位置
	 */
	private int pos = 0;

	/**
	 * 请求处理器
	 */
	private IServiceProcessor processor;

	/**
	 * 迭代器
	 */
	private Iterator<RpcFilter> filterIterable;


	@Override
	public void doFilter(RpcRequest request, RpcResponse response) {
		try {
			if (pos >= filters.size()) {
				//责任链执行完之后，交给processor执行最终请求调用
				if (processor != null) {
					RpcResponse rsp = processor.process(request);
					if (rsp.getHeader() == null) {
						rsp.setHeader(new RpcHeader());
					}

					response.setHeader(rsp.getHeader());
					response.setBody(rsp.getBody());
					response.setFileTransfer(rsp.isFileTransfer());
					//该字段一定要和请求一致，原样返回
					response.setMsgFlag(request.getMsgFlag());
					response.setBytesArray(rsp.getBytesArray());
				} else {
					log.warn("processor is null,please check your config");
				}
			} else {
				//对责任链进行迭代执行
				RpcFilter filter = filterIterable.next();
				pos++;
				if (response.getHeader() != null && StringUtil.isNotEmpty(response.getHeader().getRespCode())) {
					log.debug("response code is not empty,code:{} ,return", response.getHeader().getRespCode());
					return;
				}
				filter.doFilter(request, response, this);
			}
		} catch (Exception e) {
			log.error("process error", e);
			response.setHeader(request.getHeader());
			response.setMsgFlag(request.getMsgFlag());
			if (e instanceof RpcException) {
				RpcException rpcException = (RpcException) e;
				response.getHeader().setRespCode(rpcException.getRespCode());
				response.getHeader().setRespMsg(rpcException.getRespMsg());
			} else {
				String error = ExceptionUtils.getStackTrace(e);
				response.getHeader().setRespCode(RpcError.SYSTEM_BUSY.getErrorCode());
				response.getHeader().setRespMsg(error);
			}
		}
	}


	/**
	 * 设置业务处理器，以及过滤器
	 * @param processor 处理器
	 * @param filters 过滤器
	 */
	public void init(IServiceProcessor processor, List<RpcFilter> filters) {
		if (CollectionUtil.isNotEmpty(filters)) {
			synchronized (this.filters) {
				HashSet<RpcFilter> filterTreeSet = new HashSet<>(filters);
				//去重
				this.filters.addAll(new ArrayList<>(filterTreeSet));
				//filters 进行升序
				Collections.sort(this.filters, Comparator.comparingInt(RpcFilter::getOrder));
				this.filterIterable = this.filters.iterator();
			}
		}
		this.processor = processor;
	}
}
