package com.haven.simplej.db.manager;

import com.google.common.collect.Maps;
import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.datasource.DatasourceFactory;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源管理器
 * @author: havenzhang
 * @date: 2019/4/24 23:52
 * @version 1.0
 */
public class DatasourceManager {

	private CommonDao dao;

	private JdbcTemplate jdbcTemplate;

	private DataSourceTransactionManager transactionManager;

	private DataSource dataSource;

	/**
	 * 初始化数据源、事务管理器、jdbcTemplate,dao等
	 * @param ip
	 * @param port
	 * @param userName
	 * @param passwd
	 */
	public CommonDao initDao(String ip, String port, String userName, String passwd, String schema) {

		DataSource dataSource = initDatasource(ip, port, userName, passwd, schema);
		initTransactionManager(dataSource);

		JdbcTemplate jdbcTemplate = initJdbcTemplate(dataSource);
		return initCommonDao(jdbcTemplate);

	}


	public DataSource initDatasource(String ip, String port, String userName, String passwd, String schema) {
		Properties properties = new Properties();
		properties.putAll(PropertyManager.getProp());
		properties.put("groupName", Constant.DEFAULT_DATA_SOURCE_GROUP_NAME);
		properties.put(Constant.SCHEMA, schema);
		properties.put(Constant.SCHEMA, schema);
		properties.put(Constant.USERNAME, userName);
		properties.put(Constant.PASSWORD, passwd);
		String url = PropertyManager.get(PropertyKey.DB_URL);
		String[] dbNames = StringUtil.split(schema, Constant.SCHEMA_SPLIT_SYMBOL);
		if (dbNames != null && dbNames.length > 1) {
			schema = StringUtil.EMPTY;
		}
		url = String.format(url, ip, port, schema);
		properties.put(Constant.URL, url);
		dataSource = DatasourceFactory.createDataSource(properties);
		return dataSource;
	}

	public void initTransactionManager(DataSource dataSource) {
		Map<String, Object> prop = Maps.newHashMap();
		prop.put("dataSource", dataSource);
		SpringContext.registerBean("transactionManager" + SequenceUtil.generateId(), DataSourceTransactionManager.class, prop);
		transactionManager = SpringContext.getBean(DataSourceTransactionManager.class);
	}


	public JdbcTemplate initJdbcTemplate(DataSource dataSource) {
		Map<String, Object> prop = Maps.newHashMap();
		prop.put("dataSource", dataSource);
		SpringContext.registerBean("jdbcTemplate" + SequenceUtil.generateId(), JdbcTemplate.class, prop);
		return jdbcTemplate = SpringContext.getBean(JdbcTemplate.class);
	}

	public CommonDao initCommonDao(JdbcTemplate jdbcTemplate) {
		Map<String, Object> prop = Maps.newHashMap();
		prop.put("jdbcTemplate", jdbcTemplate);
		SpringContext.registerBean("commonDao" + SequenceUtil.generateId(), CommonDao.class, prop);
		dao = SpringContext.getBean(CommonDao.class);
		return dao;
	}


}
