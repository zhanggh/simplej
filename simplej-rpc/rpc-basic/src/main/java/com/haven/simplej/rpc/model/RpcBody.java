package com.haven.simplej.rpc.model;

import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/4/26 11:44
 * @version 1.0
 */
@Data
public class RpcBody<T> {

	private T body;
}
