package com.haven.simplej.db.datasource;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.db.model.ReadWriteDataSourceKey;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 读写分离数据源lookupKey 管理器
 * 说明（约定）：
 * 1.同一个group下可以有多个master服务
 * 2.同一个group下的某个master,可以存在多个slaver服务
 * 3.同一个group下的所有master类型的数据源对应的schema不可以同名，
 * 比如master1的schema为payment001,master2对应的schema不能与master1的一样，否则写操作的时候无法知道往哪个master写
 * @author haven.zhang
 * @date 2019/1/23.
 */
@Slf4j
public class ReadWriteKeyManager {
	/**
	 * 读写分离数据源lookukey管理器，keyManager的key为数据源groupName,value为ReadWriteDataSourceKey实例
	 */
	private static Map<String, List<ReadWriteDataSourceKey>> keyManager = Maps.newHashMap();

	/**
	 * dbName 与 groupName 映射
	 */
	private static Map<String, String> dbNameGroupMap = Maps.newHashMap();

	/**
	 * 获取指定库分组下对应库名的slaver 数据源的lookupKey
	 * @param groupName 数据源分组名
	 * @param dbName 库名
	 * @return
	 */
	public static DataSourceLookupKey getReadKey(String groupName, String dbName) {

		DataSourceLookupKey target = null;
		List<ReadWriteDataSourceKey> readWriteDataSourceKeys = keyManager.get(groupName);
		if (CollectionUtil.isEmpty(readWriteDataSourceKeys)) {
			log.error("cannot find group:{} dataSources", groupName);
			throw new UncheckedException("cannot find group:" + groupName + " dataSources");
		}
		if (CollectionUtil.isNotEmpty(readWriteDataSourceKeys)) {
			String childGroup = groupName + ":" + dbName;
			for (ReadWriteDataSourceKey readWriteDataSourceKey : readWriteDataSourceKeys) {
				if (readWriteDataSourceKey.getWriteKey() != null && StringUtils.equals(readWriteDataSourceKey.getWriteKey().getChildGroupName(), childGroup)) {
					target = readWriteDataSourceKey.getReadKey();
				} else {
					for (DataSourceLookupKey readLookupKey : readWriteDataSourceKey.getReadKeys()) {
						String readKey = readLookupKey.getChildGroupName();
						if (StringUtils.equals(readKey, childGroup)) {
							target = readWriteDataSourceKey.getReadKey();
							break;
						}
					}
				}
			}
		}

		if (target == null) {
			log.error("cannot find dataSource readKey ,group:{} ,dbName:{},all keys:{}", groupName, dbName, JSON.toJSONString(readWriteDataSourceKeys, true));
			throw new UncheckedException("cannot find dataSource readKey");
		}
		return target;
	}

	/**
	 * 获取对应分组下指定库名的master datasource lookupkey
	 * @param groupName 数据源分组名
	 * @param dbName 库名
	 * @return
	 */
	public static DataSourceLookupKey getWriteKey(String groupName, String dbName) {
		DataSourceLookupKey target = null;
		List<ReadWriteDataSourceKey> readWriteDataSourceKeys = keyManager.get(groupName);
		if (CollectionUtil.isEmpty(readWriteDataSourceKeys)) {
			log.error("cannot find group:{} dataSources", groupName);
			throw new UncheckedException("cannot find group:" + groupName + " dataSources");
		}
		if (CollectionUtil.isNotEmpty(readWriteDataSourceKeys)) {
			for (ReadWriteDataSourceKey readWriteDataSourceKey : readWriteDataSourceKeys) {
				if (readWriteDataSourceKey.getWriteKey() == null) {
					continue;
				}
				String readKey = readWriteDataSourceKey.getWriteKey().getChildGroupName();
				String readKey2 = groupName + ":" + dbName;
				if (StringUtils.equals(readKey, readKey2)) {
					target = readWriteDataSourceKey.getWriteKey();
				}
			}
		}
		if (target == null) {
			log.error("cannot find dataSource writeKey ,group:{} ,dbName:{},all keys:{}", groupName, dbName, JSON.toJSONString(readWriteDataSourceKeys, true));
			throw new UncheckedException("cannot find dataSource writeKey");
		}
		return target;
	}


	/**
	 * 向key管理器加入某个数据库分组的master 数据源的 lookupKey
	 * @param groupName 数据源分组名
	 * @param writeKey master 数据源的lookupkey
	 */
	public static synchronized void addWriteKey(String groupName, DataSourceLookupKey writeKey) {
		log.debug("addWriteKey:{}", writeKey);
		if (writeKey == null) {
			throw new UncheckedException("writeKey is empty,please check datasource properties config");
		}
		dbNameGroupMap.put(writeKey.getDbName() + "_" + Constant.MASTER_FLAG, groupName);
		if (keyManager.get(groupName) == null) {
			ReadWriteDataSourceKey key = new ReadWriteDataSourceKey(writeKey, null);
			List<ReadWriteDataSourceKey> keys = Lists.newArrayList();
			keys.add(key);
			keyManager.put(groupName, keys);
		} else {
			boolean flag = false;
			List<ReadWriteDataSourceKey> readWriteDataSourceKeys = keyManager.get(groupName);
			for (ReadWriteDataSourceKey readWriteDataSourceKey : readWriteDataSourceKeys) {
				if (readWriteDataSourceKey.getWriteKey() == null) {
					for (DataSourceLookupKey readLookupKey : readWriteDataSourceKey.getReadKeys()) {
						//同一个组下面，同样的schema，则可以认为是子组，比如一主多从
						String readKey = readLookupKey.getChildGroupName();
						String wrKey = writeKey.getChildGroupName();
						if (StringUtils.equals(readKey, wrKey)) {
							readWriteDataSourceKey.setWriteKey(writeKey);
							flag = true;
						}
					}
				} else {
					if (readWriteDataSourceKey.getWriteKey().equals(writeKey)) {
						//如果已经存在了该写库的key，则不再添加，直接退出
						log.info("the key:{} is already in keyManager", readWriteDataSourceKey.getWriteKey());
						flag = true;
						break;
					}
				}
			}
			if (!flag) {
				readWriteDataSourceKeys.add(new ReadWriteDataSourceKey(writeKey, null));
			}
		}
	}


	/**
	 * 向key管理器加入某个数据库分组的slaver 数据源的 lookupKey
	 * @param groupName 数据源分组名
	 * @param readKey slaver 数据源的lookupkey
	 */
	public static synchronized void addReadKey(String groupName, DataSourceLookupKey readKey) {

		log.debug("addReadKey:{}", readKey);
		if (readKey == null) {
			throw new UncheckedException("readKey is empty,please check datasource properties config");
		}
		dbNameGroupMap.put(readKey.getDbName() + "_" + Constant.SLAVER_FLAG, groupName);
		if (keyManager.get(groupName) == null) {
			//如果管理器中不存在某个分组的key信息，则加入一个新的ReadWriteDataSourceKey
			ReadWriteDataSourceKey key = new ReadWriteDataSourceKey(null, null);
			key.getReadKeys().add(readKey);
			List<ReadWriteDataSourceKey> keys = Lists.newArrayList();
			keys.add(key);
			keyManager.put(groupName, keys);
		} else {
			List<ReadWriteDataSourceKey> readWriteDataSourceKeys = keyManager.get(groupName);
			boolean flag = false;
			for (ReadWriteDataSourceKey readWriteDataSourceKey : readWriteDataSourceKeys) {
				if (readWriteDataSourceKey.getWriteKey() != null) {
					String Key1 = readWriteDataSourceKey.getWriteKey().getChildGroupName();
					if (StringUtils.equals(Key1, readKey.getChildGroupName())) {
						//如果存在ReadWriteDataSourceKey的writeKey不为空，并且子分组相同，则向该ReadWriteDataSourceKey加入新的key
						readWriteDataSourceKey.getReadKeys().add(readKey);
						flag = true;
					}
				} else {
					if (CollectionUtil.isNotEmpty(readWriteDataSourceKey.getReadKeys())) {
						String Key1 = readWriteDataSourceKey.getReadKeys().get(0).getChildGroupName();
						if (StringUtils.equals(Key1, readKey.getChildGroupName())) {
							readWriteDataSourceKey.getReadKeys().add(readKey);
							flag = true;
						}
					}
				}
			}
			if (!flag) {
				//当分组下不存在新增的key对应的子分组的时候，加入一个新的ReadWriteDataSourceKey
				ReadWriteDataSourceKey key = new ReadWriteDataSourceKey(null, null);
				key.getReadKeys().add(readKey);
				readWriteDataSourceKeys.add(key);
			}
		}
	}

	public static Map<String, List<ReadWriteDataSourceKey>> getKeyManager() {
		return keyManager;
	}

	public static Map<String, String> getDbNameGroupMap() {
		return dbNameGroupMap;
	}
}
