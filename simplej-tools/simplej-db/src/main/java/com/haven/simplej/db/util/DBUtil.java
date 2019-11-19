package com.haven.simplej.db.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.collection.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author haven.zhang
 * @date 2019/1/20.
 */
@Slf4j
public class DBUtil {

	/**
	 * 公共的db配置
	 */
	private static final String COMMON_DB_PROP = "common.properties";
	/**
	 * sql操作过滤的字段
	 */
	private static final List<String> excule_fields = Arrays.asList(new String[]{"Fields", "class", "tableName"});

	/**
	 * DataSourceid 的集合，用于判断是否存在重复的id
	 */
	private static final Set<String> dataSourceIdSet = Sets.newHashSet();

	/**
	 * db连接池属性配置
	 */
	private static List<Properties> propList;
	/**
	 * 是否分库，默认否
	 */
	private static boolean shardingDataSource = false;

	public static boolean isShardingDataSource() {
		return shardingDataSource;
	}

	/**
	 * 读取resource目录下的数据库配置，支持多个数据源配置
	 *
	 * @return Map
	 */
	public static synchronized List<Properties> readDataSourceProp(String path) {

		if (CollectionUtil.isNotEmpty(propList)) {
			return propList;
		}

		try {
			propList = Lists.newArrayList();
			//read db properties file
			List<Pair<String, InputStream>> pairs = PropertyManager.getResource(path);
			Properties commonProp = new Properties();
			for (Pair<String, InputStream> pair : pairs) {
				if (pair.getLeft().endsWith(COMMON_DB_PROP)) {
					commonProp.load(pair.getRight());
					PropertyManager.getProp().putAll(commonProp);
				}
			}

			for (Pair<String, InputStream> pair : pairs) {
				if (pair.getLeft().endsWith(COMMON_DB_PROP)) {
					continue;
				}
				Properties dbProp = new Properties();
				dbProp.load(pair.getRight());
				if (MapUtil.isEmpty(dbProp)) {
					log.debug("properties file :{} is empty", pair.getLeft());
					continue;
				}
				//每一数据源都会有一个唯一的id
				String dataSourceId = dbProp.getProperty(Constant.DATA_SOURCE_ID, SequenceUtil.generateId());
				if(dataSourceIdSet.contains(dataSourceId)){
					log.error("dataSourceId:{} is exist,please check properties",dataSourceId);
					throw new UncheckedException("dataSourceId:"+dataSourceId+" is exist,please check properties");
				}else {
					dataSourceIdSet.add(dataSourceId);
				}
				dbProp.putAll(commonProp);
				boolean propertyEnable = Boolean.parseBoolean(dbProp.getProperty(Constant.DB_ENABLE, "true"));
				if (!propertyEnable) {
					continue;
				}
				String url = dbProp.getProperty(Constant.URL);
				String host = dbProp.getProperty(Constant.DB_HOST);
				String port = dbProp.getProperty(Constant.DB_PORT);
				String schema = dbProp.getProperty(Constant.SCHEMA);
				if (StringUtil.contains(schema, Constant.SCHEMA_SPLIT_SYMBOL)
						||StringUtil.contains(schema, Constant.SCHEMA_PARAM_SYMBOL)) {
					schema = StringUtil.EMPTY;
				}
				url = String.format(url, host, port, schema);
				dbProp.put(DruidDataSourceFactory.PROP_URL, url);
				propList.add(dbProp);
			}
			if (propList.size() > 1) {
				shardingDataSource = true;
			}
		} catch (IOException e) {
			log.error("readDataSourceProp error", e);
		}

		return propList;
	}

	/**
	 * 检测连接的有效性
	 * @param dataSource
	 * @throws SQLException
	 */
	public static void validateConnection(DataSource dataSource) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement("SELECT 1 FROM dual");
			stmt.executeQuery();
		} catch (SQLException e) {
			throw e;
		} finally {
			close(stmt, connection);
		}
	}

	/**
	 * 关闭连接
	 * @param st
	 * @param conn
	 * @throws SQLException
	 */
	public static void close(Statement st, Connection conn) throws SQLException {
		try {
			if (st != null) {
				st.close(); // 关闭Statement
			}
		} finally {
			if (conn != null) {
				conn.close(); // 关闭连接
			}

		}
	}

	/**
	 * 解析el表达式
	 * @param args 参数
	 * @param paraNames  参数名
	 * @param expression el表达式
	 * @param beanFactory bean工厂
	 * @return el表达式解析结果
	 */
	public static Object getSpelValue(Object[] args, String[] paraNames, String expression, BeanFactory beanFactory) {
		Assert.hasText(expression);
		ExpressionParser ep = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		if (beanFactory != null) {
			context.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}

		if (!ArrayUtils.isEmpty(args) && !ArrayUtils.isEmpty(paraNames)) {
			if (args.length != paraNames.length) {
				throw new IllegalArgumentException("args length must be equal to paraNames length");
			}

			for (int i = 0; i < paraNames.length; i++) {
				context.setVariable(paraNames[i], args[i]);
			}
		}

		return ep.parseExpression(expression).getValue(context);
	}

	/**
	 * 是否过滤字段
	 * @param name
	 * @return
	 */
	public static boolean isExeculeField(String name) {
		boolean match = false;
		for (String field : excule_fields) {
			if (StringUtils.equalsIgnoreCase(field, name)) {
				match = true;
			}
		}
		return match;
	}

	/**
	 * 解析properties配置的schema，例如：test${00}...${99}@${step},将解析成100个库
	 * 其中${step} 是一个变量，代表步长，如step=2时，test${00}...${99}@2，代表：test00,test02,test04,test06...test98
	 * 默认的step是1，默认情况不需要加@${step}，如：test${00}...${99}
	 * @param schema
	 * @return
	 */
	public static String[] parserSchemas(String schema) {
		String[] params = StringUtil.split(schema, Constant.SCHEMA_SPLIT_SYMBOL);
		List<String> dbNames = Lists.newArrayList();
		for (String param : params) {
			if (!StringUtil.contains(param, Constant.SCHEMA_PARAM_SYMBOL)) {
				dbNames.add(param);
			} else {
				String[] values = parseDbNameParams(param);
				String dbNamePrefix = param.substring(0, param.indexOf(Constant.SCHEMA_PARAM_SYMBOL));
				int start = Integer.parseInt(values[0]);
				int end = Integer.parseInt(values[1]);
				int step = 1;//默认的步长
				if (values.length == 3) {
					step = Integer.parseInt(values[2]);
				}
				int len = StringUtil.length(values[1]);
				for (int i = start; i <= end; ) {
					dbNames.add(dbNamePrefix + StringUtil.leftPad(String.valueOf(i), len, "0"));
					i = i + step;
				}
			}
		}
		return ArrayUtil.toArray(dbNames, String.class);
	}

	/**
	 * 解析字符串变量，如：aaa_${000}...${999}
	 * 返回：000,999
	 * @param orgString
	 * @return
	 */
	public static String[] parseDbNameParams(String orgString) {
		int start = orgString.indexOf(Constant.SCHEMA_PARAM_SYMBOL) + 2;
		int end = orgString.indexOf("}");
		int lastStart = orgString.lastIndexOf(Constant.SCHEMA_PARAM_SYMBOL) + 2;
		int lastEnd = orgString.lastIndexOf("}");

		String param1 = orgString.substring(start, end);
		String param2 = orgString.substring(lastStart, lastEnd);
		String[] p2 = StringUtil.split(orgString,"@");
		String step = "1";
		if (p2.length == 2) {
			step = p2[1];
		}
		return new String[]{param1, param2, step};
	}



	public static String getDatasourceId(String id){
		return Constant.DATA_SOURCE_ID + ":" + id;
	}
}
