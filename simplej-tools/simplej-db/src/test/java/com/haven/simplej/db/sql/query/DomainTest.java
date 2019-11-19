package com.haven.simplej.db.sql.query;

import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.exception.UncheckedException;

/**
 * @Author: havenzhang
 * @Date: 2019/4/2 14:51
 * @Version 1.0
 */
public class DomainTest {

	public void test(){

	}

	public Class getDomainClz(Class domain, int maxDeth) {
		if (maxDeth > 5) {
			throw new UncheckedException("not a domain class");
		}
		if (!domain.getClass().getSuperclass().equals(BaseDomain.class)) {
			return getDomainClz(domain.getClass().getSuperclass(), maxDeth++);
		}
		return domain.getClass().getSuperclass();
	}
}
