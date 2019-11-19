package com.haven.simplej.db.sql.query;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public enum Operator {
	empty(" "),
	exists("exists"),
	not_like("not like"),
	like("like"),
	eq("="),
	ne("!="),
	or("or"),
	and("and"),
	in("in"),
	not_in("not in"),
	lg(">"),
	lt("<"),
	lg_eq(">="),
	lt_eq("<=");

	private String symbol;
	private String name;
	Operator(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol(){
		return symbol;
	}

	public String getName(){
		return this.name();
	}
}
