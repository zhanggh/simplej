package com.haven.simplej.codegen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.haven.simplej.codegen.kit.ComKit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityInfo implements Serializable {
	private static final long serialVersionUID = 1165008662319727436L;

	/** 表目录，修改为分表数 */
	private String catalog;// TABLE_CATALOG NOT NULL

	/** 表概要 */
	private String schema;// TABLE_SCHEMA NOT NULL

	/** 表名 */
	private String tableName;// TABLE_NAME NOT NULL

	/** 表注释 */
	private String comment;// TABLE_COMMENT NOT NULL

	/** 实体名称 */
	private String name;

	/** 表的列信息 */
	private List<EntityField> fields;

	/** 表的索引信息 */
	private List<EntityIndex> indexes;

	public void init(String catalog) {
		this.catalog = catalog;
		String name = ComKit.underlineToUpperCamel(this.tableName);
		if (!"1".equals(catalog)) {
			name = name.substring(0, name.length() - catalog.length());
		}
		this.name = name;
	}

	public void setIndexes(List<EntityIndex> indexes) {
		List<EntityIndex> list = Collections.emptyList();
		if (CollectionUtils.isNotEmpty(indexes)) {
			list = new ArrayList<>(indexes.size());
			String indexName = null;
			EntityIndex idx = null;
			for (EntityIndex index : indexes) {
				if (index.getIndexName().equals(indexName)) {
					idx.getColumnNames().add(index.getColumnName());
				} else {
					if (idx != null) {
						list.add(idx);
					}
					idx = new EntityIndex();
					idx.setIndexName(indexName = index.getIndexName());
					idx.setNonUnique(index.getNonUnique());
					List<String> columnNames = new ArrayList<>(4);
					columnNames.add(index.getColumnName());
					idx.setColumnNames(columnNames);
				}
			}
			if (idx != null) {
				list.add(idx);
			}
		}
		this.indexes = list;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nEntityInfo [catalog=");
		builder.append(catalog);
		builder.append(", schema=");
		builder.append(schema);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", name=");
		builder.append(name);
		builder.append(",\nfields=");
		builder.append(fields);
		builder.append(",\nindexes=");
		builder.append(indexes);
		builder.append("]");
		return builder.toString();
	}

}
