package com.haven.simplej.db.base;


import com.haven.simplej.response.model.PageInfo;
import com.haven.simplej.response.model.JsonResponse;

import java.util.List;

/**
 * 通用业务类接口
 * @author haven.zhang
 * @param <T>
 */
public interface BaseService<T extends BaseDomain> {

	/**
	 * 通过ID读取对象
	 * @param model
	 * @return
	 */
	T get(T model);

	/**
	 * 根据条件统计数据
	 * @param model
	 * @return
	 */
	int count(T model);

	/**
	 * 根据条件查询数据
	 * @param model
	 * @return
	 */
	List<T> query(T model);

	/**
	 * 根据条件分页查询数据
	 * @param model
	 * @return
	 */
	JsonResponse<PageInfo<T>> search(T model);

	/**
	 * 创建数据
	 * @param model
	 * @return
	 */
	int create(T model);

	/**
	 * 批量创建数据
	 * @param models
	 * @return
	 */
	int[] batchInsert(List<T> models);



	/**
	 * 修改数据，更新条件为id=xxx
	 * @param model
	 * @return
	 */
	int update(T model);

	/**
	 * 指定条件进行update
	 * @param domain
	 * @param whereKeys
	 * @return
	 */
	public int update(T domain, String... whereKeys);

	/**
	 * 批量修改数据，更新条件为id=xxx
	 * @param models
	 * @return
	 */
	int[] batchUpdate(List<T> models,String...whereKeys);

	/**
	 * 保存数据
	 * @param model
	 * @return
	 */
	int save(T model);

	/**
	 * 根据主键清理数据
	 * @param model
	 * @return
	 */
	int remove(T model);
}
