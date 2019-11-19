package com.haven.simplej.response.builder;

import com.haven.simplej.response.enums.RespCode;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.response.model.PageInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 响应对象构建器
 * @author haven.zhang
 * @date 2019/1/9.
 */
public class ResponseBuilder {

	public static <T> JsonResponse<PageInfo<T>> build(List<T> data, long count) {
		JsonResponse resp = new JsonResponse<>();
		PageInfo page = new PageInfo<>(count, 0, 0);
		page.setData(data);
		resp.setMsg(page);
		resp.setRespCode(StringUtils.lowerCase(RespCode.SUCCESS.name()));
		resp.setRespMsg(RespCode.SUCCESS.name());
		return resp;
	}


	public static <T> JsonResponse<T> build(T data) {
		JsonResponse resp = new JsonResponse<>();
		resp.setRespCode(StringUtils.lowerCase(RespCode.SUCCESS.name()));
		resp.setRespMsg(RespCode.SUCCESS.name());
		resp.setMsg(data);
		return resp;
	}


	public static JsonResponse build(boolean isSucces) {
		JsonResponse resp = new JsonResponse<>();
		resp.setRespCode(isSucces ? RespCode.SUCCESS.name() : RespCode.FAIL.name());
		return resp;
	}


	public static <T> JsonResponse<T> build(String respCode, String respMsg) {
		JsonResponse resp = new JsonResponse<>();
		resp.setRespCode(StringUtils.lowerCase(respCode));
		resp.setRespMsg(respMsg);
		return resp;
	}

	public static <T> JsonResponse<T> build(String respCode, String respMsg, Object respData) {
		JsonResponse resp = new JsonResponse<>();
		resp.setRespCode(StringUtils.lowerCase(respCode));
		resp.setRespMsg(respMsg);
		resp.setMsg(respData);
		return resp;
	}

	public static <T> JsonResponse<T> buildResponse(String respMsg, Object respData) {
		JsonResponse resp = new JsonResponse<>();
		resp.setRespCode(StringUtils.lowerCase(RespCode.SUCCESS.name()));
		resp.setRespMsg(respMsg);
		resp.setMsg(respData);
		return resp;
	}
}
