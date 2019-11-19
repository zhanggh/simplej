package com.haven.simplej.db.sql.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.db.sql.query.util.TableUtil;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * sql语句构建
 * @author haven.zhang
 * @date 2019/1/8.
 */
public class SqlBuilder {


	/**
	 * 构建软删除sql
	 * @param table
	 * @param keys
	 * @return
	 */
	public static String buildSoftDeletedSql(String table, String[] keys) {
		StringBuilder deletedSql = new StringBuilder(40);
		deletedSql.append("update ").append(table).append(" set is_deleted = 1");
		deletedSql.append(" where ");
		for (String key : keys) {
			deletedSql.append(key).append(" = ? and");
		}
		return deletedSql.substring(0, deletedSql.length() - 3);
	}

	/**
	 * 构建硬删除sql
	 * @param table
	 * @param keys
	 * @return
	 */
	public static String buildHardDeletedSql(String table, String[] keys) {
		StringBuilder deletedSql = new StringBuilder(40);
		deletedSql.append("delete from ").append(table);
		deletedSql.append(" where ");
		for (String key : keys) {
			deletedSql.append(key).append(" = ? and");
		}
		return deletedSql.substring(0, deletedSql.length() - 3);
	}

	/**
	 * 构建update语句
	 * @param table 表名
	 * @param obj 更新的对象
	 * @param args 参数
	 * @param whereKeys where 条件的列名
	 * @return update语句
	 */
	public static String buildUpdateSql(String table, Object obj, List args, String[] whereKeys) {
		Class domainClz = TableUtil.getDomainClz(obj.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		Set<String> keys = new HashSet<>(Arrays.asList(whereKeys));
		StringBuilder updateSql = new StringBuilder(60);
		updateSql.append("update ").append(table).append(" set ");
		for (PropertyDescriptor pd : pdas) {

			if (DBUtil.isExeculeField(pd.getName())) {
				continue;
			}
			if (keys.contains(pd.getName()) || keys.contains(StringUtils.upperCase(pd.getName()))) {
				continue;
			}
			Object value = ReflectionUtil.getFieldValue(obj, pd.getName());
			if (value != null) {
				args.add(value);
				updateSql.append(StringUtil.underscoreName(pd.getName())).append(" = ? ,");
			}
		}
		updateSql = new StringBuilder(updateSql.toString().substring(0,updateSql.length()-1));
		updateSql.append(" where ");
		for (String whereKey : whereKeys) {
			Object value = ReflectionUtil.getFieldValue(obj, whereKey);
			if (value == null) {
				throw new UncheckedException(
						obj.getClass() + " must has " + whereKey + " field,and it value must not be null");
			}
			args.add(value);
			updateSql.append(StringUtil.underscoreName(whereKey)).append(" = ? and ");
		}
		return updateSql.substring(0, updateSql.length() - 4);
	}

	/**
	 * 构建update语句 指定条件为主键id
	 * @param table 表名
	 * @param obj 更新的对象
	 * @param args 参数
	 * @return update语句
	 */
	public static String buildUpdateByIdSql(String table, Object obj, List args) {
		Class domainClz = TableUtil.getDomainClz(obj.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		StringBuilder updateSql = new StringBuilder(60);
		updateSql.append("update ").append(table).append(" set ");
		for (PropertyDescriptor pd : pdas) {
			if (DBUtil.isExeculeField(pd.getName()) || StringUtils.equalsIgnoreCase("id", pd.getName())) {
				continue;
			}
			Object value = ReflectionUtil.getFieldValue(obj, pd.getName());
			if (value != null) {
				args.add(value);
				updateSql.append(StringUtil.underscoreName(pd.getName())).append(" = ? ,");
			}
		}

		Object id = ReflectionUtil.getFieldValue(obj, "id");
		if (id == null) {
			throw new UncheckedException(obj.getClass() + " must has id field,and it value must not be null");
		}
		args.add(id);
		updateSql = new StringBuilder(updateSql.toString().substring(0,updateSql.length()-1));
		updateSql.append(" where id = ?");
		return updateSql.toString();
	}

	/**
	 * 构建批量更新sql
	 * @param table 表名
	 * @param objs 更新集合
	 * @param args 更新值
	 * @param whereKeys 条件字段
	 * @return
	 */
	public static String buildBatchUpdateSql(String table, Object[] objs, List<Object[]> args, String[] whereKeys) {
		if (objs == null || objs.length == 0) {
			throw new UncheckedException("batch update objects must not be empty");
		}
		Class domainClz = TableUtil.getDomainClz(objs[0].getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		//where key
		List<String> keyList = Arrays.asList(whereKeys);
		Set<String> keys = new HashSet<>(keyList);
		StringBuilder sql = new StringBuilder(60);

		//循环生成多条update语句
		for (Object obj : objs) {
			List<Object> lineArgs = Lists.newArrayList();
			//where条件的参数值
			Map<String,Object> keyValueMap = Maps.newHashMap();
			StringBuilder updateSql = new StringBuilder(60);
			updateSql.append("update ").append(table).append(" set ");
			for (PropertyDescriptor pd : pdas) {
				if (DBUtil.isExeculeField(pd.getName())) {
					continue;
				}

				Object value = ReflectionUtil.getFieldValue(obj, pd.getName());
				if (value != null) {
					if (keys.contains(pd.getName()) || keys.contains(StringUtils.upperCase(pd.getName()))
							|| keys.contains(StringUtils.lowerCase(pd.getName()))) {
						keyValueMap.put(StringUtils.upperCase(pd.getName()),value);
						continue;
					}
					lineArgs.add(value);
					updateSql.append(StringUtil.underscoreName(pd.getName())).append(" = ? ,");
				}
			}
			for (String key : keyList) {
				lineArgs.add(keyValueMap.get(StringUtils.upperCase(key)));
			}
			updateSql.deleteCharAt(updateSql.length() - 1);
			updateSql.append(" where ");
			for (String whereKey : whereKeys) {
				Object value = ReflectionUtil.getFieldValue(obj, whereKey);
				if (value == null) {
					throw new UncheckedException(
							obj.getClass() + " must has " + whereKey + " field,and it value must not be null");
				}
				updateSql.append(StringUtil.underscoreName(whereKey)).append(" = ? and ");
			}
			if(StringUtil.isEmpty(sql.toString())){
				sql.append(updateSql.substring(0, updateSql.length() - 4)).append(";");
			}
			args.add(lineArgs.toArray());
		}
		return sql.toString();
	}

	/**
	 * 构建insert语句
	 * @param table 插入的表名
	 * @param obj 插入的对象
	 * @param args 参数值
	 * @return sql
	 */
	public static String buildInsertSql(String table, Object obj, List args) {
		Class domainClz = TableUtil.getDomainClz(obj.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		StringBuilder inserSql = new StringBuilder(80);
		inserSql.append("insert into ").append(table).append(" (");
		StringBuilder fields = new StringBuilder(60);
		StringBuilder placeholder = new StringBuilder(60);

		for (PropertyDescriptor pd : pdas) {
			if (DBUtil.isExeculeField(pd.getName())) {
				continue;
			}
			Object value = ReflectionUtil.getFieldValue(obj, pd.getName());
			if (value != null) {
				args.add(value);
				placeholder.append("?,");
				fields.append(StringUtil.underscoreName(pd.getName())).append(",");
			}
		}
		inserSql.append(fields.substring(0, fields.length() - 1)).append(")");
		inserSql.append(" values (").append(placeholder.substring(0, placeholder.length() - 1)).append(")");
		return inserSql.toString();
	}

	/**
	 * 构建批量insert语句，相当于多条insert语句
	 * @param table 插入的表名
	 * @param objs 插入的对象
	 * @param args 参数值
	 * @return sql
	 */
	public static String buildBatchInsertSql(String table, Object[] objs, List<Object[]> args) {
		if (objs == null || objs.length == 0) {
			throw new UncheckedException("batch insert objects must not be empty");
		}
		Class domainClz = TableUtil.getDomainClz(objs[0].getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		StringBuilder inserSql = new StringBuilder(80);
		List<Object> lineArgs = Lists.newArrayList();
		for (Object obj : objs) {

			StringBuilder fields = new StringBuilder(60);
			StringBuilder placeholder = new StringBuilder(60);
			for (PropertyDescriptor pd : pdas) {
				if (DBUtil.isExeculeField(pd.getName())) {
					continue;
				}
				Object value = ReflectionUtil.getFieldValue(obj, pd.getName());
				if (value != null) {
					lineArgs.add(value);
					placeholder.append("?,");
					fields.append(StringUtil.underscoreName(pd.getName())).append(",");
				}
			}
			if(!inserSql.toString().startsWith("insert into ")){
				inserSql.append("insert into ").append(table).append(" (");
				inserSql.append(fields.substring(0, fields.length() - 1)).append(")");
				inserSql.append(" values (").append(placeholder.substring(0, placeholder.length() - 1)).append(")");
			}
			args.add(lineArgs.toArray());
			lineArgs.clear();
		}

		return inserSql.toString();
	}
}
