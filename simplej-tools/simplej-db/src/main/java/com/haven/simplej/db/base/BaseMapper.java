package com.haven.simplej.db.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dao接口
 * @author haven.zhang
 * @param <T>
 */
public interface BaseMapper<T extends BaseDomain> {

	/**
	 * 通过ID读取对象
	 * @param id
	 * @return
	 */
	T get(@Param("id") Long id);

	/**
	 * 根据条件统计数据
	 * @param model
	 * @return
	 */
	int count(T model);

	/**
	 * 根据条件分页查询数据
	 * @param model
	 * @return
	 */
	List<T> query(T model);

	/**
	 * 创建数据
	 * @param model
	 * @return
	 */
	int create(T model);

	/**
	 * 修改数据，条件为id
	 * @param model
	 * @return
	 */
	int update(T model);

	/**
	 *  修改数据，条件为指定的whereKeys
	 * @param model
	 * @param whereKeys
	 * @return
	 */
	int updateByKeys(T model,List<String> whereKeys);

	/**
	 * 根据主键清理数据
	 * @param model
	 * @return
	 */
	int remove(T model);


	/**
	 * 批量创建数据
	 * @param models
	 * @return
	 */
	int[] batchInsert(List<T> models);

	/**
	 * 批量修改数据，默认的更新条件为id=xxx
	 * @param models
	 * @return
	 */
	int[] batchUpdate(List<T> models,List<String> whereKeys);
}
