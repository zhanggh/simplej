package com.haven.simplej.rpc.model;

import com.haven.simplej.rpc.exception.RpcError;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/5/14 23:30
 * @version 1.0
 */
@Data
public class RpcErrorInfo {
	private RpcError error;
	private String errorMsg;
}
