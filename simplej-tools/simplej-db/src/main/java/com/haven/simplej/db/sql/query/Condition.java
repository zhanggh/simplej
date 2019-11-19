package com.haven.simplej.db.sql.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.sql.query.enums.LikeType;
import com.haven.simplej.db.sql.query.util.TableUtil;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import com.vip.vjtools.vjkit.collection.MapUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * 表查询的where 条件
 * 注意，该实例非线程安全
 * @author haven.zhang
 * @date 2019/1/3.
 */
@Getter
public class Condition {

	/**
	 * 变量符号，如$t.username 代表一条变量 t.username
	 */
	private static final CharSequence VARIABLE_SYMBOL = "$";
	/**
	 * 条件key value
	 */
	private Map<String, Pair<Operator, Object>> params = Maps.newHashMap();
	/**
	 * 翻页sql
	 */
	private String limitStr;

	/**
	 * 排序默认方式，降序
	 */
	private static final String DEFAULT_ORDER = "desc";
	/**
	 * 排序字段
	 */
	private Pair<String, Set<String>> sortcolumns;

	/**
	 * between条件
	 */
	private TreeMap<String, Pair<Object, Object>> betweenParams = Maps.newTreeMap();

	/**
	 * like 条件的字段信息
	 */
	private TreeMap<String, Pair<LikeType, String>> likeMap = Maps.newTreeMap();

	/**
	 * NOT like 条件的字段信息
	 */
	private TreeMap<String, Pair<LikeType, String>> notLikeMap = Maps.newTreeMap();


	/**
	 * 分组字段
	 */
	private TreeSet<String> groupBy = Sets.newTreeSet();


	/**
	 * or 子条件
	 */
	private List<Condition> orList = Lists.newArrayList();

	/**
	 * 条件对应的值
	 */
	private List<Object> conditionValues = Lists.newArrayList();
	/**
	 * 最终输出的条件sql
	 */
	private String conditionSql;

	/**
	 * 表名与别名映射
	 */
	private Map<String, String> tableAlias = Maps.newHashMap();

	public Condition() {
		super();
	}

	public Condition(BaseDomain domain) {
		super();
		addConditions(domain);
	}


	public void addOr(Condition or) {
		orList.add(or);
	}

	/**
	 * 增加 exists 条件的key和value
	 * @param existsCondition 条件字段值
	 */
	public void addExists(boolean existsCondition) {
		Pair<Operator, Object> pair = new Pair<>(Operator.exists, existsCondition);
		params.put(Operator.exists.getSymbol(), pair);
	}

	/**
	 * 翻页 查询
	 * @param pageNum 页码，从1开始
	 * @param pageSize 页大小
	 */
	public void setPage(int pageNum, int pageSize) {
		StringBuilder sql = new StringBuilder(30);
		sql.append(" limit ");
		sql.append((pageNum - 1) * pageSize);
		sql.append(",");
		sql.append(pageSize);
		limitStr = sql.toString();
	}


	/**
	 * 增加等于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addEq(String key, Object value) {
		Pair<Operator, Object> pair = new Pair<>(Operator.eq, value);
		params.put(key, pair);
	}

	/**
	 * 增加不等于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addNotEq(String key, Object value) {

		Pair<Operator, Object> pair = new Pair<>(Operator.ne, value);
		params.put(key, pair);
	}

	/**
	 * 增加大于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addLg(String key, Object value) {

		Pair<Operator, Object> pair = new Pair<>(Operator.lg, value);
		params.put(key, pair);
	}

	/**
	 * 增加大于等于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addLgEq(String key, Object value) {

		Pair<Operator, Object> pair = new Pair<>(Operator.lg_eq, value);
		params.put(key, pair);
	}

	/**
	 * 增加小于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addLt(String key, Object value) {

		Pair<Operator, Object> pair = new Pair<>(Operator.lt, value);
		params.put(key, pair);
	}

	/**
	 * 增加小于等于条件的key和value
	 * @param key 条件字段名
	 * @param value 条件字段值
	 */
	public void addLtEq(String key, Object value) {

		Pair<Operator, Object> pair = new Pair<>(Operator.lt_eq, value);
		params.put(key, pair);
	}


	/**
	 * 增加in条件的key和value
	 * @param key 条件字段名
	 * @param values 条件字段值
	 */
	public void addIn(String key, Object... values) {

		List list = ArrayUtil.asList(values);
		Pair<Operator, Object> pair = new Pair<>(Operator.in, list);
		params.put(key, pair);
	}

	/**
	 * 增加not in条件的key和value
	 * @param key 条件字段名
	 * @param values 条件字段值
	 */
	public void addNotIn(String key, Object... values) {

		List list = ArrayUtil.asList(values);
		Pair<Operator, Object> pair = new Pair<>(Operator.not_in, list);
		params.put(key, pair);
	}


	/**
	 * 指定group by 字段
	 * @param columns 分组的列
	 */
	public void addGroupBy(String... columns) {

		if (columns != null) {
			for (String col : columns) {
				groupBy.add(col);
			}
		}
	}


	/**
	 * 指定排序字段以及排序方式
	 * @param order 排序方式，asc 、desc
	 * @param colums 列
	 */
	public void setSortcolumns(String order, String[] colums) {
		sortcolumns = new Pair<>(order, new HashSet<>(ArrayUtil.asList(colums)));
	}

	/**
	 * 指定降序排序字段
	 * @param colums
	 */
	public void setSortcolumns(String... colums) {
		sortcolumns = new Pair<>(DEFAULT_ORDER, new HashSet<>(ArrayUtil.asList(colums)));
	}

	/**
	 * 增加like条件
	 * @param column 字段名
	 * @param type like的方式
	 * @param value 值
	 */
	public void addLike(String column, LikeType type, String value) {
		Pair<LikeType, String> likeValue = new Pair<>(type, value);
		likeMap.put(column, likeValue);
	}

	/**
	 * 增加not like条件
	 * @param column 字段名
	 * @param type like的方式
	 * @param value 值
	 */
	public void addNotLike(String column, LikeType type, String value) {
		Pair<LikeType, String> likeValue = new Pair<>(type, value);
		notLikeMap.put(column, likeValue);
	}

	/**
	 * 增加between条件
	 * @param column
	 * @param begin 开始
	 * @param end 结束
	 */
	public void addBetween(String column, Object begin, Object end) {

		betweenParams.put(column, new Pair<>(begin, end));
	}

	/**
	 * 获取条件sql以及参数
	 * @return
	 */
	public Pair<String, List<Object>> getResult() {
		StringBuilder sql = new StringBuilder(40);

		if (StringUtils.isNotEmpty(this.conditionSql)) {
			return new Pair<>(this.conditionSql, conditionValues);
		}
		//组装一般条件
		params.forEach((k, v) -> {
			sql.append(" ").append(k).append(" ");
			if (v.getLeft() == Operator.eq || v.getLeft() == Operator.ne || v.getLeft() == Operator.lg
					|| v.getLeft() == Operator.lg_eq || v.getLeft() == Operator.lt || v.getLeft() == Operator.lt_eq) {
				sql.append(v.getLeft().getSymbol());

				if (v.getRight() instanceof String) {
					String value = (String) v.getRight();
					if (StringUtils.startsWith(value, VARIABLE_SYMBOL)) {
						//如果value是一个变量
						sql.append(" ").append(value.substring(1)).append(" and");
					} else {
						conditionValues.add(value);
						sql.append(" ").append("?").append(" and");
					}
				} else {
					conditionValues.add(v.getRight());
					sql.append(" ").append("?").append(" and");
				}
			} else if (v.getLeft() == Operator.not_in || v.getLeft() == Operator.in) {
				List values = (List) v.getRight();
				sql.append(v.getLeft().getSymbol()).append(" (");
				StringBuilder inSql = new StringBuilder(30);
				values.forEach(e -> {
					conditionValues.add(e);
					inSql.append("?").append(",");

				});
				sql.append(inSql.substring(0, inSql.length() - 1)).append(")").append(" and");
			} else if (v.getLeft() == Operator.exists) {
				boolean flag = (boolean) v.getRight();
				sql.append(v.getLeft().getSymbol()).append(" ").append(flag ? "1==1" : "1!=1").append(" and");
			} else {
				throw new UncheckedException(new Exception("not support sql operator"));
			}

		});
		//between条件
		betweenParams.forEach((k, v) -> {
			sql.append(" ").append(k).append(" between ");
			conditionValues.add(v.getLeft());
			sql.append("?");
			sql.append(" and ");
			conditionValues.add(v.getRight());
			sql.append("?");
			sql.append(" and");
		});
		//组装like条件
		likeMap.forEach((k, v) -> {
			sql.append(" ").append(k).append(" like ");
			conditionValues.add(v.getRight());
			if (v.getLeft() == LikeType.LEFT) {
				sql.append("'").append("?").append("%'").append(" and");
			} else if (v.getLeft() == LikeType.MIDDLE) {
				sql.append("'%").append("?").append("%'").append(" and");
			} else if (v.getLeft() == LikeType.RIGHT) {
				sql.append("'%").append("?").append("'").append(" and");
			}
		});
		//组装NOT like条件
		notLikeMap.forEach((k, v) -> {
			sql.append(" ").append(k).append(" not like ");
			conditionValues.add(v.getRight());
			if (v.getLeft() == LikeType.LEFT) {
				sql.append("'").append("?").append("%'").append(" and");
			} else if (v.getLeft() == LikeType.MIDDLE) {
				sql.append("'%").append("?").append("%'").append(" and");
			} else if (v.getLeft() == LikeType.RIGHT) {
				sql.append("'%").append("?").append("'").append(" and");
			}
		});


		StringBuilder sql2 = new StringBuilder();
		if (sql.length() > 0) {
			sql2.append(sql.substring(0, sql.length() - 3));
		}

		//增加or子条件
		orList.forEach(e -> {
			Pair<String, List<Object>> p = e.getResult();
			conditionValues.addAll(p.getRight());
			sql2.append("or (").append(p.getLeft()).append(") ");
		});

		//排序sql
		StringBuilder sort = new StringBuilder(10);
		if (sortcolumns != null) {
			sortcolumns.getRight().forEach(e -> {
				sort.append(e).append(",");
			});
		}

		//分组sql
		StringBuilder groupBysql = new StringBuilder(10);
		groupBy.forEach(e -> groupBysql.append(e).append(","));

		if (sort.length() > 0 && groupBysql.length() > 0) {
			throw new UncheckedException(new Exception("order by and group by Can't happen at the same time"));
		}
		if (sort.length() > 0) {
			sql2.append("order by ").append(sort.substring(0, sort.length() - 1)).append(" ")
					.append(sortcolumns.getLeft()).append(" ");
		}

		if (groupBysql.length() > 0) {
			sql2.append("group by ").append(groupBysql.substring(0, groupBysql.length() - 1)).append(" ");
		}
		//增加分页
		if (StringUtils.isNoneEmpty(limitStr)) {
			sql2.append(limitStr);
		}
		this.conditionSql = sql2.toString();
		return new Pair<>(this.conditionSql, conditionValues);
	}

	/**
	 * 增加等于条件
	 * @param domain
	 */
	private void addConditions(BaseDomain domain) {
		Class domainClz = TableUtil.getDomainClz(domain.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		for (PropertyDescriptor pd : pdas) {
			if (DBUtil.isExeculeField(pd.getName())) {
				continue;
			}

			Object value = ReflectionUtil.getFieldValue(domain, pd.getName());
			if (value != null) {
				this.addEq(TableUtil.getColumn(domain, pd.getName()), value);
			}
		}

	}

	/**
	 * 是否有条件
	 * @return
	 */
	public boolean hasConditions() {
		return MapUtil.isNotEmpty(params) || MapUtil.isNotEmpty(betweenParams) || MapUtil.isNotEmpty(notLikeMap)
				|| MapUtil.isNotEmpty(likeMap);
	}

}
