package com.haven.simplej.db.sql.query;

import com.haven.simplej.db.sql.query.enums.LikeType;
import com.vip.vjtools.vjkit.base.type.Pair;

import java.util.List;

/**
 * Created by haven.zhang on 2019/1/6.
 */
public class ConditionTest {

	public static void main(String[] args) {

		Condition or = new Condition();
		or.addEq("val1", "33333");
		or.addNotEq("val2", "dfdtg");

		Condition or2 = new Condition();
		or2.addEq("val1", "33333");
		or2.addNotEq("val2", "dfdtg");

		Condition cdt = new Condition();
		cdt.addEq("t0.name", "$t1.name");
		cdt.addEq("age", 30);
		cdt.addNotEq("job", "software Enginer");
		cdt.addLike("intrest", LikeType.LEFT, "play boo");
		cdt.addNotLike("character", LikeType.MIDDLE, "Introvert");
		cdt.addIn("school", "中山大学", "广东财经");
		cdt.addNotIn("major", "语言学", "人文");
		cdt.setPage(2, 10);
		cdt.addGroupBy("name", "age");
		cdt.addBetween("height", "150", "190");
		cdt.addOr(or);
		cdt.addOr(or2);
		Pair<String,List<Object>> p = cdt.getResult();
		System.out.println(p.getLeft());
		p.getRight().forEach(e->{
			System.out.print(e + ",");
		});
	}
}
