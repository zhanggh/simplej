package com.haven.simplej.db.sql.query.enums;

/**
 * Created by haven.zhang on 2019/1/6.
 */
public enum JoinType {
	INNER_JOIN("inner join"),
	LEFT_JOIN("left join"),
	RIGHT_JOIN("right join"),
	FULL_JOIN("full join");

	private String symbol;
	private String name;

	JoinType(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol(){
		return symbol;
	}

	public String getName(){
		return this.name();
	}
}
