package com.haven.simplej.codegen.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityIndex implements Serializable {
	private static final long serialVersionUID = 1165008662319727436L;

	private long nonUnique;// NON_UNIQUE NOT NULL

	private String indexName;// INDEX_NAME NOT NULL

	private String seqInIndex;// SEQ_IN_INDEX NOT NULL

	private String columnName;// COLUMN_NAME NOT NULL

	private List<String> columnNames;

	public String getColumnNameStr() {
		return StringUtils.join(columnNames, ", ");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nEntityIndex [nonUnique=");
		builder.append(nonUnique);
		builder.append(", indexName=");
		builder.append(indexName);
		builder.append(", seqInIndex=");
		builder.append(seqInIndex);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append(", columnNames=");
		builder.append(columnNames);
		builder.append("]");
		return builder.toString();
	}

}
