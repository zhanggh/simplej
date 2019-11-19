package com.haven.simplej.db.sql.query.util;

import com.google.common.collect.Maps;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;

import java.util.HashSet;
import java.util.Map;

/**
 * 表别名映射工具类
 * 如果查询语句涉及到多张表的时候可以用到该工具类，如果仅仅是单表查询，则不需要该工具
 * @author haven.zhang
 * @date 2019/1/7.
 */
public class TableUtil {
	/**
	 * 表名与别名映射,这个必须是全局唯一的，一个表在一个jvm实例中只能有一个别名
	 */
	private static Map<String, String> tableAlias = Maps.newTreeMap();
	/**
	 * 表字段定义信息
	 */
	private static Map<String, EnumDomainDef> tableFields = Maps.newTreeMap();

	/**
	 * 别名，用于判断生成的别名是否以及存在
	 */
	private static HashSet<String> aliasList = new HashSet<>();
	/**
	 * 表别名序号
	 */
	private static int index = 0;

	/**
	 * 生成表别名
	 * @param table
	 */
	public synchronized static String generateAlias(String table) {
		String alias = "t" + index++;
		if (aliasList.contains(alias)) {
			return generateAlias(table);
		}
		aliasList.add(alias);
		return alias;
	}

	/**
	 * 更加表别名映射
	 * @param tables
	 */
	public synchronized static void addTables(String... tables) {
		if (tables != null) {
			for (String table : tables) {
				if (!tableAlias.containsKey(table)) {
					String alias = generateAlias(table);
					tableAlias.put(table, alias);
				}
			}
		}
	}


	/**
	 * 更加表别名映射
	 * @param tables
	 */
	public synchronized static void addTables(BaseDomain... tables) {
		if (tables != null) {
			for (BaseDomain table : tables) {
				String alias = generateAlias(table.getTableName());
				tableAlias.put(table.getTableName(), alias);
			}
		}
	}

	/**
	 * 获取带别名的列，如：t0.username
	 * @param table
	 * @param column
	 * @return
	 */
	public static String getColumn(String table, String column) {

		if (!tableAlias.containsKey(table)) {
			synchronized (TableUtil.class) {
				if (!tableAlias.containsKey(table)) {
					String alias = generateAlias(table);
					tableAlias.put(table, alias);
				}
			}
		}
		return tableAlias.get(table) + "." + StringUtil.underscoreName(column);
	}

	/**
	 * 获取带别名的列，如：t0.username
	 * @param table
	 * @param column
	 * @return
	 */
	public static String getColumn(BaseDomain table, String column) {

		if (!tableAlias.containsKey(table.getTableName())) {
			synchronized (TableUtil.class) {
				if (!tableAlias.containsKey(table.getTableName())) {
					String alias = generateAlias(table.getTableName());
					tableAlias.put(table.getTableName(), alias);
				}
			}
		}
		return tableAlias.get(table.getTableName()) + "." + StringUtil.underscoreName(column);
	}


	/**
	 * 获取表别名，如：t0
	 * @param table
	 * @return
	 */
	public static String getAlias(String table) {

		if (!tableAlias.containsKey(table)) {
			synchronized (TableUtil.class) {
				if (!tableAlias.containsKey(table)) {
					String alias = generateAlias(table);
					tableAlias.put(table, alias);
				}
			}
		}
		return tableAlias.get(table);
	}


	public static Class getDomainClz(Class domain, int maxDeth) {
		if (maxDeth > 5) {
			throw new UncheckedException("not a domain class");
		}
		if (domain.getSuperclass() != null && !domain.getSuperclass().equals(BaseDomain.class)) {
			return getDomainClz(domain.getSuperclass(), maxDeth++);
		}
		return domain;
	}
}
