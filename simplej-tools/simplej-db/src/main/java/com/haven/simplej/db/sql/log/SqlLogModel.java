//package com.haven.simplej.db.sql.log;
//
//import lombok.Data;
//
///**
// * @author: havenzhang
// * @date: 2018/9/28 17:34
// * @version 1.0
// */
//@Data
//public class SqlLogModel implements Comparable<SqlLogModel>{
//
//	/**
//	 * 当前服务的域或者命名空间
//	 */
//	private String namespace;
//	/**
//	 * 执行SQL语句
//	 */
//	private String sql;
//
//	/**
//	 * 请求链路id
//	 */
//	private String msgId;
//
//	/**
//	 * 当前请求时间
//	 */
//	private long currentTime;
//
//	/**
//	 * SQL执行耗时
//	 */
//	private long cost;
//
//	/**
//	 * SQL参数
//	 */
//	private String paramValues;
//
//
//	public String toString(){
//		StringBuilder sb =new StringBuilder();
//		sb.append(currentTime).append(",");
//		sb.append(msgId).append(",");
//		sb.append(sql).append(",");
//		sb.append(paramValues).append(",");
//		sb.append(cost).append("\n");
//		return sb.toString();
//	}
//
//	@Override
//	public int compareTo(SqlLogModel o) {
//		return Long.compare(o.cost,cost);
//	}
//}
