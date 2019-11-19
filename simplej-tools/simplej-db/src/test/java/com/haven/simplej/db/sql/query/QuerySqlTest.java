package com.haven.simplej.db.sql.query;


import com.haven.simplej.db.sql.query.enums.JoinType;
import com.haven.simplej.db.sql.query.util.TableUtil;

/**
 * Created by haven.zhang on 2019/1/6.
 */
public class QuerySqlTest extends TableUtil {
	public static void main(String[] args) {
		String tb="user_info";
		QuerySql querySql = new QuerySql(tb);
		querySql.setFromTable(tb);
		Condition condition = new Condition();
		condition.addEq(getColumn(tb, "col1"), "sdfsdfs");
		condition.addNotEq(getColumn(tb, "col2"), "sdfsdfs");
		condition.addLg(getColumn(tb, "col3"), 555);
		condition.addLtEq(getColumn(tb, "col4"), 6666);
		condition.addGroupBy(getColumn(tb,"col5"));
		condition.addGroupBy(getColumn(tb,"col6"));
		condition.setPage(2,20);
		querySql.setWhereCondition(condition);

		String tb1="job";
		Condition on = new Condition();
		on.addEq(getColumn(tb1, "col1"), "$" + getColumn(tb, "col1"));
		on.addNotEq(getColumn(tb1, "col2"), 300);

		querySql.addJoin(tb1, JoinType.INNER_JOIN, on);

		String tb2="job2";
		Condition on2 = new Condition();
		on2.addEq(getColumn(tb2, "col1"), "$"+ getColumn(tb, "col1"));
		on2.addNotEq(getColumn(tb2, "col2"), 300);

		querySql.addJoin(tb2, JoinType.LEFT_JOIN, on2);
		querySql.setSelectColums(getColumn(tb, "*"), getColumn(tb1, "col1"), getColumn(tb2, "col1"));

		System.out.println(querySql.getSqlAndArgs().getLeft());
		querySql.getSqlAndArgs().getRight().forEach(e -> {
			System.out.print(e + ",");
		});
	}
}
