package com.haven.simplej.codegen.dao;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.haven.simplej.cache.CacheManager;
import com.haven.simplej.codegen.PropertyKey;
import com.haven.simplej.codegen.model.EntityField;
import com.haven.simplej.codegen.model.EntityIndex;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haven.simplej.codegen.model.EntityInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库表结构信息查询
 * @author haven.zhang
 * dao
 */
@Slf4j
@Repository
@Data
public class EntityInfoDao {

	private CommonDao dao;
	private Map<String, EntityInfo> cache = Maps.newConcurrentMap();

	private static String queryTableSql = "SELECT "//
			+ "TABLE_CATALOG AS `catalog`, "//
			+ "TABLE_SCHEMA AS `schema`, "//
			+ "TABLE_NAME AS tableName, "//
			+ "TABLE_COMMENT AS comment "//
			+ "FROM INFORMATION_SCHEMA.`TABLES` WHERE "//
			+ "TABLE_SCHEMA = ? "//
			+ "AND TABLE_NAME = ?";

	private static String queryColumnSql = "SELECT "//
			+ "COLUMN_NAME AS columnName, "//
			+ "ORDINAL_POSITION AS position, "//
			+ "COLUMN_DEFAULT AS columnDefault, "//
			+ "IS_NULLABLE AS nullable, "//
			+ "DATA_TYPE AS dataType, "//
			+ "CHARACTER_MAXIMUM_LENGTH AS characterLength, "//
			+ "NUMERIC_PRECISION AS numericPrecision, "//
			+ "NUMERIC_SCALE AS numericScale, "//
			+ "COLUMN_TYPE AS columnType, "//
			+ "COLUMN_COMMENT AS comment, "//
			+ "EXTRA AS extra, "//
			+ "COLUMN_KEY AS columnKey "//
			+ "FROM INFORMATION_SCHEMA.`COLUMNS` WHERE "//
			+ "TABLE_SCHEMA = ? "//
			+ "AND TABLE_NAME = ? "//
			+ "ORDER BY ORDINAL_POSITION";

	private static String queryIndexSql = "SELECT "//
			+ "NON_UNIQUE AS nonUnique, "//
			+ "INDEX_NAME AS indexName, "//
			+ "SEQ_IN_INDEX AS seqInIndex, "//
			+ "COLUMN_NAME AS columnName "//
			+ "FROM INFORMATION_SCHEMA.`STATISTICS` WHERE "//
			+ "TABLE_SCHEMA = ? "//
			+ "AND TABLE_NAME = ? "//
			+ "ORDER BY INDEX_NAME, SEQ_IN_INDEX";


	public EntityInfo getEntityInfo(String name) {
		String key = "getTableInfo:" + name;
		EntityInfo entityInfo = CacheManager.getObject(key);
		if (entityInfo != null) {
			return entityInfo;
		}
		String schema = PropertyManager.get(PropertyKey.TABLE_SCHEMA);
		String catalog = PropertyManager.get("table.index." + name);
		String tabName = name;
		if (catalog != null && !"1".equals(catalog.trim())) {
			String format = name + "%0" + catalog.length() + "d";
			tabName = String.format(format, 1);
			log.info(tabName);
		} else {
			catalog = "1";
		}
		String[] params = StringUtils.split(tabName, ".");
		if (StringUtils.isEmpty(schema)) {
			if (ArrayUtils.isEmpty(params)) {
				throw new UncheckedException("schema must set in properties file,as table.schema=test or table.names=test.payment_list....");
			}
			schema = params[0];
			tabName = params[1];
		} else if (ArrayUtils.getLength(params) == 2) {
			tabName = params[1];
		}
		entityInfo = getEntityInfo(schema, tabName, catalog);
		CacheManager.putLocal(key, entityInfo);
		return entityInfo;
	}
	/**
	 * @param schema
	 * @param name
	 * @return
	 */
	public EntityInfo getEntityInfo(String schema, String name, String catalog) {
		log.info("SQL: {}; PARAMS: {} {}", queryTableSql, schema, name);
		String key = schema + ":" + name + ":" + catalog;
		EntityInfo entity = cache.get(key);
		if (entity != null) {
			return entity;
		}
		entity = dao.getObj(queryTableSql, new Object[]{schema, name}, EntityInfo.class);
		if (entity != null) {
			entity.setFields(this.getEntityFields(schema, name));
			entity.setIndexes(this.getEntityIndex(schema, name));
		}
		entity.init(catalog);
		cache.put(key, entity);
		return entity;
	}

	/**
	 * @param schema
	 * @param name
	 * @return
	 */
	public List<EntityField> getEntityFields(String schema, String name) {
		log.info("SQL: {}; PARAMS: {} {}", queryColumnSql, schema, name);
		String key = "getEntityFields:" + schema + ":" + name;
		List<EntityField> list = CacheManager.getObject(key);
		if (CollectionUtil.isNotEmpty(list)) {
			return list;
		}
		list = dao.getObjs(queryColumnSql, new Object[]{schema, name}, EntityField.class);
		CacheManager.putLocal(key, list);
		return list;
	}

	/**
	 * @param schema
	 * @param name
	 * @return
	 */
	public List<EntityIndex> getEntityIndex(String schema, String name) {
		log.info("SQL: {}; PARAMS: {} {}", queryIndexSql, schema, name);
		String key = "getEntityIndex:" + schema + ":" + name;
		List<EntityIndex> list = CacheManager.getObject(key);
		if (CollectionUtil.isNotEmpty(list)) {
			return list;
		}
		list = dao.getObjs(queryIndexSql, new Object[]{schema, name}, EntityIndex.class);
		CacheManager.putLocal(key, list);
		return list;
	}

}
