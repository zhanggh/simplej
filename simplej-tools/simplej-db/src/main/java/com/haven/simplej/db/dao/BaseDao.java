package com.haven.simplej.db.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.db.sql.builder.SqlBuilder;
import com.haven.simplej.db.sql.log.SlowQuerySqlLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基于jdbcTemplate实现的orm db操作工具类
 * Created by haven.zhang on 2018/1/7.
 */

@Slf4j
@Data
public abstract class BaseDao {

	/**
	 * spring 提供的jdbc模板
	 */
	protected JdbcTemplate jdbcTemplate;


	/**
	 * 计算总笔数
	 * @param sql sql语句
	 * @param args 参数
	 * @return 笔数
	 */
	public Integer count(String sql, Object[] args) {
		long start = System.currentTimeMillis();
		log.debug("sql:{} params:{}", sql, Arrays.toString(args));
		List<Integer> ls = jdbcTemplate.queryForList(sql, args, Integer.class);
		if (ls.isEmpty())
			return null;
		Integer result = ls.iterator().next();
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args, System.currentTimeMillis() - start);
		return result;
	}

	/**
	 * 查询单条记录
	 * @param sql sql语句
	 * @param args 参数，必须按顺序
	 * @param clz 映射的类
	 * @param <T> 泛型
	 * @return 返回对象
	 */
	public <T> T getObj(String sql, Object[] args, Class clz) {
		long start = System.currentTimeMillis();
		log.debug("sql:{} params:{}", sql, Arrays.toString(args));
		List ls = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(clz));
		if (ls.isEmpty())
			return null;
		T resp = (T) ls.iterator().next();
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args, System.currentTimeMillis() - start);
		return resp;
	}

	/**
	 * 查询多笔记录
	 * @param sql sql语句
	 * @param args 参数，必须按顺序
	 * @param clz 映射的类
	 * @return 返回结果集
	 */
	public List getObjs(String sql, Object[] args, Class clz) {
		long start = System.currentTimeMillis();
		log.debug("sql:{} params:{}", sql, Arrays.toString(args));
		List values = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(clz));
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args, System.currentTimeMillis() - start);
		return values;
	}

	/**
	 * 查询
	 * @param sql 查询sql
	 * @param args 参数
	 * @return map key为表字段名，value为字段值
	 */
	public List<Map<String, Object>> query(String sql, List args) {
		long start = System.currentTimeMillis();
		log.debug("sql:{} params:{}", sql, JSON.toJSONString(args));
		List<Map<String, Object>> list;
		if (args == null) {
			list = jdbcTemplate.queryForList(sql);
		} else {
			list = jdbcTemplate.queryForList(sql, args.toArray());
		}

		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args == null ? null : args.toArray(), System.currentTimeMillis() - start);
		return list;
	}

	/**
	 * 软删除
	 * @param table 待删除的表
	 * @param keys where条件
	 * @param args 参数
	 * @return int 更新行数
	 */
	public int delete(String table, String[] keys, Object[] args) {
		long start = System.currentTimeMillis();
		String sql = SqlBuilder.buildSoftDeletedSql(table, keys);
		log.debug("sql:{} params:{}", sql, Arrays.toString(args));
		int result = jdbcTemplate.update(sql, args);
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args, System.currentTimeMillis() - start);
		return result;
	}


	/**
	 * 更新
	 * @param table 表
	 * @param obj 更新对象
	 * @param whereKeys where条件
	 * @return int 更新行数
	 */
	public int update(String table, Object obj, String[] whereKeys) {
		long start = System.currentTimeMillis();
		List args = Lists.newArrayList();
		String sql = SqlBuilder.buildUpdateSql(table, obj, args, whereKeys);
		log.debug("sql:{} params:{}", sql, Arrays.toString(args.toArray()));
		int result = jdbcTemplate.update(sql, args.toArray());
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args.toArray(), System.currentTimeMillis() - start);
		return result;
	}

	/**
	 * 更新操作
	 * @param sql update语句
	 * @param args 条件参数
	 * @return int 更新行数
	 */
	public int update(String sql, Object[] args) {
		long start = System.currentTimeMillis();
		int result = jdbcTemplate.update(sql, args);
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args, System.currentTimeMillis() - start);
		return result;
	}


	/**
	 * 更新表，条件为id字段，obj对象必须包含id字段，并且不能为空
	 * @param table 表
	 * @param obj 更新对象
	 * @return int 更新行数
	 */
	public int updateById(String table, Object obj) {
		long start = System.currentTimeMillis();
		List args = Lists.newArrayList();
		String sql = SqlBuilder.buildUpdateByIdSql(table, obj, args);
		log.debug("sql:{} params:{}", sql, Arrays.toString(args.toArray()));
		int result = jdbcTemplate.update(sql, args.toArray());
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args.toArray(), System.currentTimeMillis() - start);
		return result;
	}


	/**
	 * 批量更新  @TODO 待修正
	 * @param table 更新表名
	 * @param objs 更新集合
	 * @param whereKeys 更新sql的where 条件字段
	 * @return 更新结果
	 */
	public int[] batchUpdate(String table, Object[] objs, String[] whereKeys) {
		long start = System.currentTimeMillis();
		List<Object[]> args = Lists.newArrayList();
		String sql = SqlBuilder.buildBatchUpdateSql(table, objs, args, whereKeys);
		log.debug("sql:{} params:{}", sql, JSON.toJSONString(args));
		int[] result = jdbcTemplate.batchUpdate(sql, args);
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args.toArray(), System.currentTimeMillis() - start);
		return result;
	}

	/**
	 * 插入单挑记录
	 * @param table 插入表
	 * @param obj 对象
	 * @return 插入结果
	 */
	public int insert(String table, Object obj) {
		long start = System.currentTimeMillis();
		List args = Lists.newArrayList();
		String sql = SqlBuilder.buildInsertSql(table, obj, args);
		log.debug("sql:{} params:{}", sql, Arrays.toString(args.toArray()));
		int result = jdbcTemplate.update(sql, args.toArray());
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args.toArray(), System.currentTimeMillis() - start);
		return result;
	}

	/**
	 * 批量插入记录 @TODO 待修正
	 * @param table 插入表
	 * @param objs 对象集合
	 * @return 插入结果
	 */
	public int[] batchInsert(String table, Object[] objs) {
		long start = System.currentTimeMillis();
		List<Object[]> args = Lists.newArrayList();
		String sql = SqlBuilder.buildBatchInsertSql(table, objs, args);
		log.debug("sql:{} params:{}", sql, JSON.toJSONString(args));
		int[] result = jdbcTemplate.batchUpdate(sql, args);
		//记录SQL日志
		SlowQuerySqlLogger.log(sql, args.toArray(), System.currentTimeMillis() - start);
		return result;
	}
}
