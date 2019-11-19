package com.haven.simplej.codegen.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.codegen.model.EntityField;
import com.haven.simplej.codegen.model.EntityIndex;
import com.haven.simplej.codegen.service.EntityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haven.simplej.codegen.model.EntityInfo;
import com.haven.simplej.codegen.dao.EntityInfoDao;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author haven.zhang
 *
 */
@Slf4j
@Service
public class EntityInfoServiceImpl implements EntityInfoService {

	@Autowired
	private EntityInfoDao entityInfoDao;


	private Map<String, EntityInfo> entities = Maps.newConcurrentMap();


	/**
	 * 查询表信息
	 * @param name
	 * @return
	 */
	public EntityInfo getEntity(String name) {
		EntityInfo entity = entities.get(name);
		if (entity == null) {
			entity = entityInfoDao.getEntityInfo(name);
			log.info("entity : {}", entity);
			entities.put(name, entity);
		}
		return entity;
	}

	@Override
	public EntityInfo getEntity(String tableName, int useDbFlag) {
		if (useDbFlag == 1) {//如果是需要访问数据库
			return getEntity(tableName);
		}
		return buildEntity();
	}

	/**
	 * 构建模拟数据
	 * @return EntityInfo
	 */
	private EntityInfo buildEntity() {
		EntityInfo entityInfo = new EntityInfo();

		entityInfo.setFields(buildFields());
		entityInfo.setIndexes(buildIndexs());
		entityInfo.setComment("just for test");
		entityInfo.setName("Userinfo");
		entityInfo.setTableName("userinfo");
		entityInfo.setSchema("test");
		return entityInfo;
	}

	public List<EntityIndex> buildIndexs(){
		List<EntityIndex> indexes = Lists.newArrayList();
		EntityIndex index = new EntityIndex();
		index.setIndexName("idx_user_code");
		index.setSeqInIndex("1");
		index.setColumnName("user_code");
		indexes.add(index);

		index = new EntityIndex();
		index.setIndexName("PRIMARY");
		index.setSeqInIndex("1");
		index.setColumnName("Id");
		indexes.add(index);
		return indexes;
	}

	public List<EntityField> buildFields(){
		List<EntityField> fields = Lists.newArrayList();
		//主键
		EntityField field = new EntityField();
		field.setColumnName("Id");
		field.setPosition(1);
		field.setComment("主键");
		field.setNullable("NO");
		field.setDataType("int");
		field.setNumericPrecision(10L);
		field.setNumericScale(0L);
		field.setColumnType("int(11)");
		field.setColumnKey("PRI");
		field.setExtra("auto_increment");//主键
		fields.add(field);

		//用户编号
		field = new EntityField();
		field.setColumnName("user_code");
		field.setPosition(2);
		field.setComment("用户编号");
		field.setNullable("NO");
		field.setDataType("varchar");
		field.setCharacterLength(50L);
		field.setColumnType("varchar(50)");
		field.setColumnKey("UNI");
		field.setExtra("");//
		fields.add(field);

		//用户编号
		field = new EntityField();
		field.setColumnName("user_name");
		field.setPosition(3);
		field.setComment("用户名");
		field.setNullable("NO");
		field.setDataType("varchar");
		field.setCharacterLength(50L);
		field.setColumnType("varchar(50)");
		field.setExtra("");//
		fields.add(field);

		//删除标志
		field = new EntityField();
		field.setColumnName("is_deleted");
		field.setPosition(4);
		field.setComment("软删除标志：1-已删除，0-正常");
		field.setNullable("NO");
		field.setDataType("tinyint");
//		field.setCharacterLength(50L);
		field.setNumericPrecision(3L);
		field.setNumericScale(0L);
		field.setColumnType("tinyint(3) unsigned");
		field.setExtra("");//
		fields.add(field);

		//创建时间
		field = new EntityField();
		field.setColumnName("create_time");
		field.setPosition(5);
		field.setComment("创建时间");
		field.setNullable("NO");
		field.setDataType("timestamp");
//		field.setCharacterLength(50L);
//		field.setNumericPrecision(3L);
//		field.setNumericScale(0L);
		field.setColumnType("timestamp(3)");
		field.setExtra("");//
		fields.add(field);

		//更新时间
		field = new EntityField();
		field.setColumnName("update_time");
		field.setPosition(6);
		field.setComment("更新时间");
		field.setNullable("NO");
		field.setDataType("timestamp");
		//		field.setCharacterLength(50L);
		//		field.setNumericPrecision(3L);
		//		field.setNumericScale(0L);
		field.setColumnType("timestamp(3)");
		field.setExtra("");//
		fields.add(field);

		//创建人
		field = new EntityField();
		field.setColumnName("created_by");
		field.setPosition(7);
		field.setComment("创建人");
		field.setNullable("NO");
		field.setDataType("varchar");
		field.setCharacterLength(30L);
//		field.setNumericPrecision(3L);
//		field.setNumericScale(0L);
		field.setColumnType("varchar(30)");
		field.setExtra("");//
		fields.add(field);

		//更新人
		field = new EntityField();
		field.setColumnName("updated_by");
		field.setPosition(8);
		field.setComment("更新人");
		field.setNullable("NO");
		field.setDataType("varchar");
		field.setCharacterLength(30L);
//		field.setNumericPrecision(3L);
//		field.setNumericScale(0L);
		field.setColumnType("varchar(30)");
		field.setExtra("");//
		fields.add(field);

		return fields;
	}
}
