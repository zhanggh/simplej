package com.haven.simplej.db.base;

import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.sql.query.Condition;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.response.model.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * 业务类基类，如果dao类型是CommonDao的时候需要继承该类
 * @author haven.zhang
 * @param <T>
 */
public abstract class BaseServiceImpl2<T extends BaseDomain> implements BaseService<T> {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
    protected CommonDao dao;


	@Override
	public T get(T domain) {

		return this.dao.getDomain(domain);
	}

	@Override
	public int count(T model) {
		return this.dao.count(model);
	}

	@Override
	public List<T> query(T model) {
		return this.dao.query(model);
	}

	@Override
	public JsonResponse<PageInfo<T>> search(T model) {
		long count = this.count(model);
		List<T> data = Collections.emptyList();
		if (count > 0) {
			//默认构造的条件的运行关系是：等于
			Condition condition = new Condition(model);
//			condition.setPage();
			data = this.dao.query(model,condition);
		}
		return ResponseBuilder.build(data, count);
	}

	@Override
	public int create(T model) {
		return this.dao.save(model);
	}

	@Override
	public int update(T model) {
		return this.dao.update(model);
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
		return this.dao.delete(model);
	}

	/**
	 * 批量创建数据
	 * @param models
	 * @return
	 */
	@Override
	public int[] batchInsert(List<T> models){

		return this.dao.batchInsert(models);
	}

	/**
	 * 批量修改数据，更新条件可以指定，默认为id
	 * @param models
	 * @return
	 */
	@Override
	public int[] batchUpdate(List<T> models,String...whereKeys){

		return this.dao.batchUpdate(models, whereKeys);
	}

	/**
	 * 更新记录，更新条件可以自定义
	 * @param domain 更新的domain
	 * @param whereKeys 条件字段
	 * @return 影响行数，0 失败，>=1成功
	 */
	public int update(T domain, String... whereKeys) {
		return this.dao.update(domain, whereKeys);
	}
}
