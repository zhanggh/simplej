package com.haven.simplej.db.sql.query;

import com.google.common.collect.Lists;
import com.haven.simplej.db.sql.query.enums.JoinType;
import com.haven.simplej.db.sql.query.util.TableUtil;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 生成表查询sql语句，支持多表关联查询
 * 如：inner join / left join /right join / full join
 * Created by haven.zhang on 2019/1/6.
 */
@Setter
@Getter
public class QuerySql {

	/**
	 * 条件对应的值
	 */
	List<Object> conditionValues = Lists.newArrayList();
	/**
	 * 查询sql
	 */
	private String querySql;
	/**
	 * where语句的查询条件
	 */
	private Condition whereCondition;


	/**
	 * left join tables
	 */
	private List<JoinTable> joinTables = Lists.newArrayList();
	/**
	 * 要查询的列
	 */
	protected String selectColums;

	/**
	 * sql语句中紧跟着from语句的表，比如：select * from xxxx t0 left join zzz t1 on t0 = t1 ;
	 * 这里的fromTable是xxxx表
	 */
	private String fromTable;


	public QuerySql() {
		selectColums = "*,";
	}

	public QuerySql(String fromTable) {
		this.fromTable = fromTable;
		TableUtil.addTables(fromTable);
		selectColums = TableUtil.getColumn(fromTable, "*") + ",";
	}


	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
		TableUtil.addTables(fromTable);
	}

	/**
	 * 增加关联查询表
	 * @param table join tablename
	 * @param type join type,as left join / inner join /right join/full join
	 * @param onCondition join on condition
	 */
	public void addJoin(String table, JoinType type, Condition onCondition) {
		TableUtil.addTables(table);
		joinTables.add(new JoinTable(table, type, onCondition));
		selectColums += TableUtil.getAlias(table) + ".*,";
	}


	/**
	 * 指定查询的 字段
	 * 一步到位设置所有要查询的列
	 * @param columns 查询的列
	 */
	public void setSelectColums(String... columns) {
		if (columns != null) {
			StringBuilder cols = new StringBuilder(20);
			for (String col : columns) {
				cols.append(col).append(",");
			}
			selectColums = cols.substring(0, cols.length() - 1);
		}
	}


	/**
	 * 指定查询的 字段
	 * 一步到位设置所有要查询的列
	 * @param columns 查询的列
	 */
	public void setSelectColums(List<String> columns) {
		if (columns != null) {
			StringBuilder cols = new StringBuilder(20);
			for (String col : columns) {
				cols.append(col).append(",");
			}
			selectColums = cols.substring(0, cols.length() - 1);
		}
	}


	/**
	 * 获取查询sql
	 * @return Pair<String , List < Object>>
	 */
	public Pair<String, List<Object>> getSqlAndArgs() {

		if (StringUtils.isNotEmpty(this.querySql)) {
			return new Pair<>(this.querySql, conditionValues);
		}

		StringBuilder sql = new StringBuilder(50);
		sql.append("select ");
		if (CollectionUtil.isNotEmpty(whereCondition.getGroupBy())) {
			StringBuilder colums = new StringBuilder();
			whereCondition.getGroupBy().forEach(e -> colums.append(" ").append(e).append(" ,"));
			sql.append(colums.substring(0, colums.length() - 1));
		} else {
			sql.append(selectColums.substring(0, selectColums.length() - 1));
		}

		sql.append(" from ");
		sql.append(fromTable).append(" ");
		sql.append(TableUtil.getAlias(fromTable));

		StringBuilder joinSql = new StringBuilder(30);
		joinTables.forEach(e -> {
			if (!e.getOnCondition().hasConditions()) {
				throw new UncheckedException("join conditon must not be empty");
			}
			joinSql.append(" \n").append(e.getJoinType().getSymbol()).append(" ").append(e.getTableName());
			joinSql.append(" ").append(TableUtil.getAlias(e.getTableName()));
			joinSql.append(" on ");
			joinSql.append(e.getOnCondition().getResult().getLeft());
			conditionValues.addAll(e.getOnCondition().getResult().getRight());
		});
		sql.append(joinSql);
		if (whereCondition.hasConditions()) {
			sql.append(" \nwhere ");
			sql.append(whereCondition.getResult().getLeft());
		}
		conditionValues.addAll(whereCondition.getResult().getRight());
		this.querySql = sql.toString();
		return new Pair<>(this.querySql, conditionValues);
	}

}
