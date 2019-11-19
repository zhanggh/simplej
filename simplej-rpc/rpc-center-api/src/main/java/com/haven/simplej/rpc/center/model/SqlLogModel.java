package com.haven.simplej.rpc.center.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2018/9/28 17:34
 * @version 1.0
 */
@Doc(value = "sql执行情况统计信息，上报至服务治理中心，进行统计分析")
@RpcStruct
@Data
public class SqlLogModel implements Comparable<SqlLogModel>{

	@Doc(value = "当前服务的域或者命名空间")
	@RpcParam(required = true)
	private String namespace;

	@RpcParam(required = true)
	@Doc(value = "SQL语句")
	private String sql;

	@RpcParam(required = true)
	@Doc(value = "请求链路id")
	private String msgId;

	@RpcParam(required = true)
	@Doc(value = "SQL执行当前时间")
	private long currentTime;

	@RpcParam(required = true)
	@Doc(value = "SQL执行延时")
	private long cost;

	@RpcParam(required = true)
	@Doc(value = "SQL语句执行的参数")
	private String paramValues;


	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append(currentTime).append(",");
		sb.append(msgId).append(",");
		sb.append(sql).append(",");
		sb.append(paramValues).append(",");
		sb.append(cost).append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(SqlLogModel o) {
		return Long.compare(o.cost,cost);
	}
}
