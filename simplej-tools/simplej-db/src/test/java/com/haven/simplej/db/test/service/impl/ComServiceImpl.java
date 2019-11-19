package com.haven.simplej.db.test.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.annotation.Query;
import com.haven.simplej.db.annotation.DBShardingStrategy;
import com.haven.simplej.db.annotation.TableShardingStrategy;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.datasource.DataSourceHolder;
import com.haven.simplej.db.test.service.ComService;
import com.haven.simplej.exception.UncheckedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/9/3 23:55
 * @version 1.0
 */
//@Service
@DataSource(dbName = "test")
//@DataSource("000001")
public class ComServiceImpl implements ComService {

	@Autowired
	private CommonDao dao;

	@Override
	@Transactional
	public void update() {
		String sql1 = "update hkpay.t_bank_info set Fbank_name='xxxxx' where Fbank_type='4982'";
		String sql2 = "update test.opt_log set created_by='xxxa' where id=1";
		int ret = dao.getJdbcTemplate().update(sql1);
		System.out.println(ret);
		ret = dao.getJdbcTemplate().update(sql2);
		System.out.println(ret);
		if (ret == 1) {
			throw new UncheckedException("rollback test");
		}
	}

	//	@Query(groupName = "testGroup", dbNameExpression = "test${01}...${06}@2", tableExpression = "userinfo_${0}...${2}")
	//	@Query(groupName = "testGroup", dbNameExpression = "test${01}...${06}@2" )
	@Query(groupName = "testGroup")
	public List query() {
		String dbName = DataSourceHolder.getCurrentDbName();
		String tableName = DataSourceHolder.getCurrenTableName();
		//		String sql1 = String.format("select * from  %s.%s", dbName, tableName);
		String sql1 = String.format("select * from  %s.userinfo", dbName, tableName);
		System.out.println("query sql:" + sql1);
		return dao.query(sql1,null);
	}

	@DataSource(dbName = "test03", groupName = "testGroup")
	public List get() {
		String dbName = DataSourceHolder.getCurrentDbName();
		String sql1 = String.format("select * from  %s.userinfo", dbName);
		System.out.println("query sql:" + sql1);
		CommonDao commonDao = new CommonDao(DataSourceHolder.getDataSource());
		return commonDao.getJdbcTemplate().queryForList(sql1);
	}

	@Override
	@DBShardingStrategy(strategyBeanName = "shardingWithNameStrategy", expression = "#name")
	public int save(String name) {
		System.out.println(DataSourceHolder.getCurrentDbName());
		String dbName = DataSourceHolder.getCurrentDbName();
		String sql1 = String.format("insert into %s.userinfo(name) values(?)", dbName);
		System.out.println("insert sql:" + sql1);
		return dao.getJdbcTemplate().update(sql1, name);
	}

	@Override
	@TableShardingStrategy(strategyBeanName = "shardingWithNameStrategy", expression = "#name")
	@DBShardingStrategy(strategyBeanName = "shardingWithNameStrategy", expression = "#name")
	public int save2(String name) {
		System.out.println(DataSourceHolder.getCurrentDbName());
		String dbName = DataSourceHolder.getCurrentDbName();
		String tableName = DataSourceHolder.getCurrenTableName();
		String sql1 = String.format("insert into %s.%s(name) values(?)", dbName, tableName);
		System.out.println("insert sql:" + sql1);
		return dao.getJdbcTemplate().update(sql1, name);
	}
}
