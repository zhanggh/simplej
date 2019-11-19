package com.haven.simplej.response.model;

import com.haven.simplej.response.enums.RespCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author haven.zhang
 * @date 2019/1/14.
 */
@Setter
@Getter
@ToString
public class Response {

	private RespCode respCode;

	private String respMsg;

	public Response(RespCode code,String msg){
		this.respCode = code;
		this.respMsg = msg;
	}
}
