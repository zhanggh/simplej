package com.haven.simplej.db.dao;

import com.google.common.collect.Lists;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.sql.query.Condition;
import com.haven.simplej.db.sql.query.JoinTable;
import com.haven.simplej.db.sql.query.QuerySql;
import com.haven.simplej.db.sql.query.util.TableUtil;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

/**
 * 通用的dao类，包含了常规的增删改查操作
 * 假如这里提供的方法还不能直接满足业务操作时，可以在继承该类进行自定义的dao实现
 * 如果需要写sql语句，请直接使用mybatis的mapper.xml进行配置
 * @author haven.zhang
 * @date 2018/1/8.
 */
public class CommonDao extends BaseDao {

	public CommonDao() {
		super();
	}

	public CommonDao(JdbcTemplate jdbcTemplate) {
		super();
		setJdbcTemplate(jdbcTemplate);
	}

	public CommonDao(DataSource dataSource){
		super();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		setJdbcTemplate(jdbcTemplate);
	}

	@Override
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * 保存一个对象
	 * @param domain 保存的domain
	 * @return 影响行数，0 失败，>=1成功
	 */
	public int save(BaseDomain domain) {
		return this.insert(domain.getTableName(), domain);
	}

	/**
	 * 批量保存
	 * @param domainList  保存的domain
	 * @return 影响行数，0 失败，>=1成功
	 */
	public <T extends BaseDomain> int[] batchInsert(List<T> domainList) {
		if (CollectionUtil.isEmpty(domainList)) {
			throw new UncheckedException("batch insert objs must not be empty");
		}
		return this.batchInsert(domainList.get(0).getTableName(), domainList.toArray());
	}

	/**
	 * 软删除
	 * @param domain 删除的domain
	 * @param whereKeys 删除条件字段
	 * @return
	 */
	public int delete(BaseDomain domain, String[] whereKeys) {
		List args = Lists.newArrayList();
		Object value = null;
		for (String whereKey : whereKeys) {
			value = ReflectionUtil.getFieldValue(domain, whereKey);
			if (value == null) {
				throw new UncheckedException("delete table condition value cannot be null");
			}
			args.add(value);
		}
		return this.delete(domain.getTableName(), whereKeys, args.toArray());
	}

	/**
	 * 软删除,默认的更新条件是id
	 * @param domain 删除的domain
	 * @return 删除行数，0 失败，>=1成功
	 */
	public int delete(BaseDomain domain) {
		List args = Lists.newArrayList();
		Object value = null;
		String whereKey = Constant.TABLE_PRIMARY_KEY;
		value = ReflectionUtil.getFieldValue(domain, whereKey);
		if (value == null) {
			throw new UncheckedException("delete table id value cannot be null");
		}
		args.add(value);

		return this.delete(domain.getTableName(), new String[]{whereKey}, args.toArray());
	}

	/**
	 * 更新记录，更新条件字段为id
	 * @param domain 更新的domain
	 * @return 影响行数，0 失败，>=1成功
	 */
	public int update(BaseDomain domain) {

		return this.updateById(domain.getTableName(), domain);
	}

	/**
	 * 更新记录，更新条件可以自定义
	 * @param domain 更新的domain
	 * @param whereKeys 条件字段
	 * @return 影响行数，0 失败，>=1成功
	 */
	public int update(BaseDomain domain, String[] whereKeys) {
		return this.update(domain.getTableName(), domain, whereKeys);
	}

	/**
	 * 批量更新，更新条件可以自定义
	 * @param domains 更新的domain
	 * @param whereKeys 条件字段
	 * @return 影响行数，0 失败，>=1成功
	 */
	public <T extends BaseDomain> int[] batchUpdate(List<T> domains, String[] whereKeys) {
		if (CollectionUtil.isEmpty(domains)) {
			throw new UncheckedException("batch update objs must not be empty");
		}
		return this.batchUpdate(domains.get(0).getTableName(), domains.toArray(), whereKeys);
	}

	/**
	 * 批量更新，更新条件可以自定义
	 * @param domains
	 * @return
	 */
	public <T extends BaseDomain> int[] batchUpdate(List<T> domains) {
		if (CollectionUtil.isEmpty(domains)) {
			throw new UncheckedException("batch update objs must not be empty");
		}
		return this.batchUpdate(domains.get(0).getTableName(), domains.toArray(), new String[]{
				Constant.TABLE_PRIMARY_KEY});
	}


	/**
	 * 单表查询
	 * 默认的查询条件运算关系是：=
	 * 如果需要自定义条件，请用getDomain(BaseDomain domain,Condition condition)方法
	 * @param domain
	 * @return BaseDomain
	 */
	public <T extends BaseDomain> T getDomain(T domain) {
		QuerySql sql = new QuerySql(domain.getTableName());

		Condition condition = new Condition();
		Class domainClz = TableUtil.getDomainClz(domain.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		for (PropertyDescriptor pd : pdas) {
			if (DBUtil.isExeculeField(pd.getName())) {
				continue;
			}
			Object value = ReflectionUtil.getFieldValue(domain, pd.getName());
			if (value != null) {
				condition.addEq(TableUtil.getColumn(domain, pd.getName()), value);
			}
		}
		sql.setWhereCondition(condition);
		return this.getObj(sql.getSqlAndArgs().getLeft(), sql.getSqlAndArgs().getRight().toArray(), domain.getClass());
	}


	/**
	 * 单表查询
	 *
	 * 如果需要自定义条件，请用getDomain(BaseDomain domain,Condition condition)方法
	 * @param domain
	 * @return BaseDomain
	 */
	public <T extends BaseDomain> T getDomain(T domain, Condition condition) {
		QuerySql sql = new QuerySql(domain.getTableName());
		sql.setWhereCondition(condition);
		return this.getObj(sql.getSqlAndArgs().getLeft(), sql.getSqlAndArgs().getRight().toArray(), domain.getClass());
	}

	/**
	 * 单表查询多行记录
	 * 如果需要自定义条件，请用query(BaseDomain domain,Condition condition)方法
	 * @param domain 查询入参
	 * @return 查询结果集
	 */
	public <T extends BaseDomain> List<T> query(BaseDomain domain) {
		QuerySql sql = new QuerySql(domain.getTableName());

		Condition condition = new Condition();
		Class domainClz = TableUtil.getDomainClz(domain.getClass(), 5);
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
		for (PropertyDescriptor pd : pdas) {
			if (DBUtil.isExeculeField(pd.getName())) {
				continue;
			}
			Object value = ReflectionUtil.getFieldValue(domain, pd.getName());
			if (value != null) {
				//增加等于条件的运行关系
				condition.addEq(TableUtil.getColumn(domain, pd.getName()), value);
			}
		}
		sql.setWhereCondition(condition);
		return this.getObjs(sql.getSqlAndArgs().getLeft(), sql.getSqlAndArgs().getRight().toArray(), domain.getClass());
	}


	/**
	 * 单表查询多行数据
	 * 如果需要自定义条件，请用query(BaseDomain domain,Condition condition)方法
	 * @param domain
	 * @return BaseDomain
	 */
	public <T extends BaseDomain> List<T> query(BaseDomain domain, Condition condition) {
		QuerySql sql = new QuerySql(domain.getTableName());
		sql.setWhereCondition(condition);
		return this.getObjs(sql.getSqlAndArgs().getLeft(), sql.getSqlAndArgs().getRight().toArray(), domain.getClass());
	}

	/**
	 * 复杂的多表关联查询，如inner join /left join /right join
	 * @param masterDomain 主表，跟在from语句后面的表，比如 select t0.* ,t1.* from masterDomain t0,left join xxxx t1 on t0.id = t1.id
	 * @param joinTables 多个表对应的domain实例
	 * @param where where条件
	 * @return List<List               <               BaseDomain>> 返回的每一行数据包含多个不同的domain实例
	 */
	public <T extends BaseDomain> List<List<T>> query(BaseDomain masterDomain, List<JoinTable> joinTables,
			Condition where) {
		List<List<T>> resp = Lists.newArrayList();
		QuerySql sql = new QuerySql(masterDomain.getTableName());
		sql.setWhereCondition(where);
		if (CollectionUtil.isNotEmpty(joinTables)) {
			for (JoinTable table : joinTables) {
				sql.addJoin(table.getTableName(), table.getJoinType(), table.getOnCondition());
			}
		}
		//查询
		List<Map<String, Object>> result = this.query(sql.getSqlAndArgs().getLeft(), sql.getSqlAndArgs().getRight());
		result.forEach(e -> {
			List<T> domains = Lists.newArrayList();
			T domain1 = BeanUtil.copyFromMap(e, masterDomain.getClass());
			domains.add(domain1);
			for (JoinTable table : joinTables) {
				T domain = BeanUtil.copyFromMap(e, table.getDomain().getClass());
				domains.add(domain);
			}
			resp.add(domains);
		});
		return resp;
	}


	/**
	 * 计算行数
	 * @param domain
	 * @param <T>
	 * @return
	 */
	public <T extends BaseDomain> Integer count(T domain) {

		return count(domain, null);
	}

	/**
	 * 计算行数
	 * @param domain
	 * @param condition
	 * @param <T>
	 * @return
	 */
	public <T extends BaseDomain> Integer count(T domain, Condition condition) {
		if (condition == null) {
			condition = new Condition();
			Class domainClz = TableUtil.getDomainClz(domain.getClass(), 5);
			PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(domainClz);
			for (PropertyDescriptor pd : pdas) {
				if (DBUtil.isExeculeField(pd.getName())) {
					continue;
				}
				Object value = ReflectionUtil.getFieldValue(domain, pd.getName());
				if (value != null) {
					condition.addEq(TableUtil.getColumn(domain, pd.getName()), value);
				}
			}
		}
		StringBuilder sql = new StringBuilder("select count(*) from ");
		sql.append(domain.getTableName()).append(" ");
		sql.append(TableUtil.getAlias(domain.getTableName()));

		if(StringUtils.isNotEmpty(condition.getResult().getLeft())){
			sql.append(" where ").append(condition.getResult().getLeft());
			return count(sql.toString(), condition.getResult().getRight().toArray());
		}
		return count(sql.toString(),null);
	}

}
