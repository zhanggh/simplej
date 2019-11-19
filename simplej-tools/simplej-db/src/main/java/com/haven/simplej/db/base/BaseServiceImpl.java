package com.haven.simplej.db.base;

import com.google.common.collect.Lists;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.PageInfo;
import com.haven.simplej.response.model.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 业务类基类，如果dao类型是mybatis的时候需要继承该类
 * @author haven.zhang
 * @param <T>
 */
public abstract class BaseServiceImpl<T extends BaseDomain> implements BaseService<T> {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	protected abstract BaseMapper<T> getBaseMapper();

	@Override
	public T get(T domain) {
		return this.getBaseMapper().get(domain.getId());
	}

	@Override
	public int count(T model) {
		return this.getBaseMapper().count(model);
	}

	@Override
	public List<T> query(T model) {
		return this.getBaseMapper().query(model);
	}

	@Override
	public JsonResponse<PageInfo<T>> search(T model) {
		long count = this.count(model);
		List<T> data = Collections.emptyList();
		if (count > 0) {
			data = this.query(model);
		}
		return ResponseBuilder.build(data, count);
	}

	@Override
	public int create(T model) {
		return this.getBaseMapper().create(model);
	}

	@Override
	public int update(T model) {
		return this.getBaseMapper().update(model);
	}

	@Override
	public int update(T model, String... whereKeys) {
		List<String> params = Lists.newArrayList();
		for (String whereKey : whereKeys) {
			params.add(whereKey);
		}
		return this.getBaseMapper().updateByKeys(model, params);
	}

	@Override
	public int save(T model) {
		if (model.getId() != null) {
			return this.update(model);
		}
		return this.create(model);
	}

	@Override
	public int remove(T model) {
		return this.getBaseMapper().remove(model);
	}

	/**
	 * 批量创建数据
	 * @param models
	 * @return
	 */
	@Override
	public int[] batchInsert(List<T> models) {

		return this.getBaseMapper().batchInsert(models);
	}

	/**
	 * 批量修改数据，更新条件可以指定，默认为id
	 * @param models
	 * @return
	 */
	@Override
	public int[] batchUpdate(List<T> models, String... whereKeys) {
		List<String> params = Lists.newArrayList();
		for (String whereKey : whereKeys) {
			params.add(whereKey);
		}
		return this.getBaseMapper().batchUpdate(models, params);
	}
}
