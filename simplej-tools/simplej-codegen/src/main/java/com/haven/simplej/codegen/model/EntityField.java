package com.haven.simplej.codegen.model;

import java.io.Serializable;

import com.haven.simplej.codegen.kit.ComKit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityField implements Serializable {
	private static final long serialVersionUID = 1165008662319727436L;

	/** 实体成员名称 */
	private String name;

	/** 实体成员类型 */
	private String type;

	/** 表字段名称 */
	private String columnName;// COLUMN_NAME NOT NULL

	private long position;// ORDINAL_POSITION NOT NULL

	private String columnDefault;// COLUMN_DEFAULT

	private String nullable;// IS_NULLABLE NOT NULL

	private String dataType;// DATA_TYPE NOT NULL

	private Long characterLength;// CHARACTER_MAXIMUM_LENGTH

	private Long numericPrecision;// NUMERIC_PRECISION

	private Long numericScale;// NUMERIC_SCALE

	private String columnType;// COLUMN_TYPE NOT NULL

	private String comment;// COLUMN_COMMENT NOT NULL

	private String extra;// EXTRA NOT NULL

	private String columnKey;// COLUMN_KEY NOT NULL

	private String upName;

	public String getName() {
		if (this.name == null) {
			this.name = ComKit.underlineToLowerCamel(this.columnName);
		}
		return this.name;
	}

	public String getUpName() {
		if (this.upName == null) {
			this.upName = ComKit.underlineToUpperCamel(this.columnName);
		}
		return this.upName;
	}

	public String getType() {
		if (this.type == null) {
			switch (this.dataType.toLowerCase()) {
			case "bool":
			case "boolean":
				this.type = "Boolean";
				break;
			case "tinyint":
				this.type = "Byte";
				break;
			case "smallint":
				this.type = "Short";
				break;
			case "int":
			case "integer":
				this.type = "Integer";
				break;
			case "bigint":
				this.type = "Long";
				break;
			case "float":
				this.type = "Float";
				break;
			case "real":
			case "double":
				this.type = "Double";
				break;
			case "dec":
			case "fixed":
			case "numeric":
			case "decimal":
				this.type = "BigDecimal";
				break;
			case "date":
				this.type = "Date";// SQL Date
				break;
			case "datetime":
				this.type = "java.util.Date";
				break;
			case "timestamp":
				this.type = "Timestamp";
				break;
			case "time":
				this.type = "Time";
				break;
			case "char":
			case "varchar":
			case "binary":// like CHAR
			case "varbinary":// like VARCHAR
			case "enum":
			case "set":
			case "tinytext":// max 255 characters text
			case "text":// max 65,535 64KiB characters text
			case "mediumtext": // max 16,777,215 16MiB characters text
			case "longtext": // max 4,294,967,295 or 4GiB characters text
				this.type = "String";
				break;
			case "blob":
			case "mediumblob":
			case "longblob":
				// this.type = "Blob";// MySQL 内部是直接使用 byte[] 的
				this.type = "byte[]";
				break;
			default:
				throw new IllegalArgumentException("未知的数据类型：" + this.dataType);
			}
		}
		return this.type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nEntityField [name=");
		builder.append(name);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append(", position=");
		builder.append(position);
		builder.append(", columnDefault=");
		builder.append(columnDefault);
		builder.append(", nullable=");
		builder.append(nullable);
		builder.append(", dataType=");
		builder.append(dataType);
		builder.append(", characterLength=");
		builder.append(characterLength);
		builder.append(", numericPrecision=");
		builder.append(numericPrecision);
		builder.append(", numericScale=");
		builder.append(numericScale);
		builder.append(", columnType=");
		builder.append(columnType);
		builder.append(", comment=");
		builder.append(comment);
		builder.append("]");
		return builder.toString();
	}

}
