
CREATE TABLE `url_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `uri` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'web服务uri',
  `method` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'http method，如post,多个用英文逗号分隔',
  `header` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'http header，如：Content-Type=application/json，多个用于英文逗号分隔',
  `schema` VARCHAR(6) NOT NULL DEFAULT 'http' COMMENT 'web服务的schema，http/https',
  `namespace` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '命名空间，可以是唯一的域名',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_uri` (`uri`),
  KEY `idx_namespace` (`namespace`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='web服务url信息';




CREATE TABLE `config_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `namespace` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '所属的命名空间',
  `item_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '属性名',
  `item_value` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '属性值',
  `item_type` VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '1-普通属性项，2-配置文件',
  `file_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '命名空间，可以是唯一的域名',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  `item_version` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '属性版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_namespace_item_name` (`namespace`,`item_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='属性配置信息';


CREATE TABLE `sequence_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键,可用于某个业务的唯一编号',
  `namespace` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '所属的命名空间',
  `seq_key` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '序列号key',
  `seq_value` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '当前序列号',
  `step` INT(11) UNSIGNED NOT NULL DEFAULT '100' COMMENT '步长',
  `seq_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '序列命名，一般为中文名',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  `item_version` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '属性版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_namespace_seq_key` (`namespace`,`seq_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='序列号生成规则信息';

