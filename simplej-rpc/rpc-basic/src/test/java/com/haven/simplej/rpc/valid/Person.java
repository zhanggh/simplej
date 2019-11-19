package com.haven.simplej.rpc.valid;

import com.haven.simplej.rpc.annotation.RpcParam;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/5/15 22:09
 * @version 1.0
 */
@Data
public class Person{
	@RpcParam
	String name;
	@RpcParam
	int age;
}