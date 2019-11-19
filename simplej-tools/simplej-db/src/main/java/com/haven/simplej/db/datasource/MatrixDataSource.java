package com.haven.simplej.db.datasource;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多数据源混合
 * 0.单数据源（或者说单数据库服务）架构
 * 1.一主多从架构，一个master ，N个slaver，master与slaver对应的库名是一样的，写操作在master，读操作可以在master，也可以在slaver
 * 2.N主N从架构（数据分片），N个master,N个slaver,
 * 3.N主（数据分片），0个slaver
 * 4.0个主，N个slaver，比如只读场景
 * 5.不支持多master并且数据不分片的架构
 * 数据不分片是指同样的一笔数据分别写入到多个master服务
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
public class MatrixDataSource extends AbstractRoutingDataSource {

	private static volatile MatrixDataSource dataSource;

	/**
	 * 连接池初始化状态
	 */
	private static volatile AtomicBoolean state = new AtomicBoolean(false);

	/**
	 * 获取混合数据源（多个数据源的场景）
	 * @param props 数据源配置属性
	 * @return MatrixDataSource
	 */
	public static MatrixDataSource getDataSource(List<Properties> props) {
		if (dataSource == null) {
			synchronized (MatrixDataSource.class) {
				if (dataSource == null) {
					dataSource = new MatrixDataSource();
					dataSource.init(props);
					state.set(true);
				}
			}
		}
		return dataSource;
	}

	public synchronized void init(List<Properties> props) {
		if(CollectionUtil.isEmpty(props)){
			throw new UncheckedException("datasource properties must not be empty,please set it");
		}
		if (dataSource != null && !state.get()) {
			DataSource tmpDataSource = null;
			for (Properties prop : props) {
				tmpDataSource = DatasourceFactory.createDataSource(prop);
			}
			if(props.size() == 1){//只有一个数据源，也就是默认数据源
				Properties prop = props.get(0);
				//用数据库名作为实例名，也作为多数据源路由的key
				String groupName = prop
						.getProperty(Constant.DATA_SOURCE_GROUP_NAME_KEY, Constant.DEFAULT_DATA_SOURCE_GROUP_NAME);
				boolean isMaster = Boolean.parseBoolean(prop.getProperty(Constant.DATA_SOURCE_SERVER_IS_MASTER,
						Constant.DATA_SOURCE_SERVER_MASTER_DEFAULT_FLAG));

				String serverNo = prop.getProperty(Constant.DATA_SOURCE_SERVER_NO, Constant.DEFAULT_DATA_SOURCE_SERVER_NO);
				//默认的数据源，当我们没有显示配置分库策略的时候，默认就是走该数据源做相关数据操作
				//如果一个数据源包含多个数据库，则同样是共享一个默认的数据源
				String lookupKey = "default";
				DataSourceLookupKey defaulLookupKey = new DataSourceLookupKey(lookupKey, groupName, isMaster, serverNo);
				DatasourceFactory.getDataSourceMap().put(defaulLookupKey, tmpDataSource);
				DataSourceHolder.setDefaultKey(defaulLookupKey);
			}
			//多个数据源，每个数据源都有一个lookupkey
			Map<Object, Object> dataSourceMap = DatasourceFactory.getDataSourceMap();
			log.info("setTargetDataSources keys:{}", JSON.toJSONString(dataSourceMap.keySet(),true));
			log.info("keyManager keys:{}", JSON.toJSONString(ReadWriteKeyManager.getKeyManager(),true));
			this.setTargetDataSources(dataSourceMap);
		}
	}

	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceLookupKey key;
		if (DataSourceHolder.getKey() != null) {
			key = DataSourceHolder.getKey();
		} else {
			key = DataSourceHolder.getDefaultKey();
		}
		log.info("determineCurrentLookupKey:{}", key);
		return key;
	}
}
