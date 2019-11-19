package com.haven.simplej.db.sql.query;

import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.sql.query.enums.JoinType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by haven.zhang on 2019/1/7.
 */
@Setter @Getter
public class JoinTable {
	private BaseDomain domain;
	private String tableName;
	private JoinType joinType;
	private Condition onCondition;

	public JoinTable(String tableName,JoinType type,Condition onCondition){
		this.tableName = tableName;
		this.joinType = type;
		this.onCondition = onCondition;
	}

	public JoinTable(BaseDomain domain,JoinType type,Condition onCondition){
		this.tableName = domain.getTableName();
		this.domain = domain;
		this.joinType = type;
		this.onCondition = onCondition;
	}
}
