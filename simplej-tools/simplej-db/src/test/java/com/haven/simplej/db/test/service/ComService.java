package com.haven.simplej.db.test.service;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/3 23:54
 * @version 1.0
 */
public interface ComService {

	void update();

	List query();

	List get();

	int save(String name);

	int save2(String name);
}
