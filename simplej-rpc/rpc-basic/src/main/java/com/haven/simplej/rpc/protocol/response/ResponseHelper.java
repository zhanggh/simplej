package com.haven.simplej.rpc.protocol.response;

import com.haven.simplej.rpc.enums.MsgFlag;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcResponse;

//import javax.validation.constraints.NotNull;

/**
 * 响应对象帮助类
 * @author: havenzhang
 * @date: 2018/4/26 21:27
 * @version 1.0
 */
public class ResponseHelper {


	/**
	 * 构建返回对象
	 * @param respCode 错误码
	 * @param respMsg 错误描述
	 * @return RpcResponse
	 */
	public static RpcResponse buildResponse(String respCode, String respMsg, RpcHeader header) {
		RpcResponse response = new RpcResponse();
		response.setHeader(header);
		header.setRespCode(respCode);
		header.setRespMsg(respMsg);
		response.setMsgFlag((byte) MsgFlag.BUSINESS.getValue());
		return response;
	}
	/**
	 * 构建返回对象
	 * @param error 错误码
	 * @param respMsg 错误描述
	 * @return RpcResponse
	 */
	public static RpcResponse buildResponse(RpcError error, String respMsg, RpcHeader header) {
		RpcResponse response = new RpcResponse();
		response.setHeader(header);
		response.setMsgFlag((byte) MsgFlag.BUSINESS.getValue());
		header.setRespCode(error.getErrorCode());
		header.setRespMsg(respMsg);
		return response;
	}
	/**
	 * 构建返回对象
	 * @param body 返回结果
	 * @return RpcResponse
	 */
	public static RpcResponse buildResponse(Object body, RpcHeader header) {
		RpcResponse response = new RpcResponse();
		response.setHeader(header);
		header.setRespCode(RpcError.SUCCESS.getErrorCode());
		header.setRespMsg(RpcError.SUCCESS.getErrorMsg());
		response.setBody(body);
		response.setMsgFlag((byte) MsgFlag.BUSINESS.getValue());
		return response;
	}
	/**
	 * 构建返回对象
	 * @param body 返回结果
	 * @return RpcResponse
	 */
	public static void setResponse(Object body, RpcHeader header,RpcResponse response) {
		response.setHeader(header);
		header.setRespCode(RpcError.SUCCESS.getErrorCode());
		header.setRespMsg(RpcError.SUCCESS.getErrorMsg());
		response.setBody(body);
		response.setMsgFlag((byte) MsgFlag.BUSINESS.getValue());
	}
	/**
	 * 构建返回对象
	 * @param body 返回结果
	 * @return RpcResponse
	 */
	public static void setResponse(byte[] body, RpcHeader header,RpcResponse response) {
		response.setHeader(header);
		header.setRespCode(RpcError.SUCCESS.getErrorCode());
		header.setRespMsg(RpcError.SUCCESS.getErrorMsg());
		response.setBytesArray(body);
		response.setMsgFlag((byte) MsgFlag.BUSINESS.getValue());
	}
}
