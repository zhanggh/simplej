package com.haven.simplej.authen.sql;

/**
 * 自定义的sql语句
 * @Author: havenzhang
 * @Date: 2019/4/6 21:06
 * @Version 1.0
 */

public class SqlBuilder {

	/**
	 * 查询用户权限列表的失去了
	 * @return
	 */
	public static String getUserAuthoritySql(){
		StringBuilder sql = new StringBuilder("SELECT t1.* FROM menu_info t1 ,role_menu_ref t2,user_role_ref t3,user_info t4");
		sql.append(" WHERE t4.user_code = t3.user_code AND t3.role_code = t2.role_code");
		sql.append(" AND t2.menu_code = t1.menu_code AND t4.user_code = ?");
		return sql.toString();
	}
}
