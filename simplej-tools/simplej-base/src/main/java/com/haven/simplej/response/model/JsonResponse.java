package com.haven.simplej.response.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@ToString
public class JsonResponse<T> {

	private T msg;

	private String respCode;

	private String respMsg;

	public void setRespCode(String respCode) {
		this.respCode = StringUtils.lowerCase(respCode);
	}


}