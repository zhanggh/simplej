package com.haven.simplej.rpc.registry.sql;

/**
 * ServiceInfo 相关的SQL语句
 * @author: havenzhang
 * @date: 2018/5/5 20:00
 * @version 1.0
 */
public class InstanceSqlBuilder {

	public static String getInstanceByServiceId(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t1.* FROM rpc_register.service_instance_ref t2,rpc_register.instance_info t1 WHERE t1.id=t2"
				+ ".`instance_id` "
				+ "AND t2.type = ? AND t1.status = 1 AND t2.`service_id` = ?");
		return sql.toString();
	}


	public static String queryInstanceRefSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t2.* FROM rpc_register.service_instance_ref t2  WHERE  t2.`instance_id` = ? AND t2.`service_id` = ?");
		return sql.toString();
	}
}
