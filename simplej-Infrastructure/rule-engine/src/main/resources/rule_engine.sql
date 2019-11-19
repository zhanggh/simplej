CREATE TABLE `expression` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `expr_type` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '表达式类型：if/then/else/normal_command',
  `expr_params` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '操作参数表达式',
  `operator` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '操作符',
  `operator_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '操作符id，关联操作符信息',
  `order_num` INT(11) NOT NULL DEFAULT '1' COMMENT '序号',
  `rule_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '规则id',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_rule_id` (`rule_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='规则表达式，一个规则对应多个表达式';


CREATE TABLE `rule_operator` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '操作符名称',
  `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '描述',
  `data_type` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '适用的数据类型 1:字符串; 2:数字; 3:日期; 4:布尔; 5:字典; 0:其他',
  `return_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '操作符结果返回类型，INI/LONG/STRING.....',
  `op_type` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '操作符类型：1-算术运算符，2-逻辑运算符，3-比较运算符，4-移位运算符，5-括号,6-赋值运算符，7-插件运算符',
  `param_count` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '操作符后面的参数数量',
  `param_type` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '参数类型的配置, 如：[INI,LONG,STRING]',
  `label` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '比较符的中文描述',
  `parser_type` VARCHAR(15) NOT NULL DEFAULT 'el' COMMENT '解析引擎：el/groovy',
  `selected_label` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '选中后的显示的描述',
  `code` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '比较符生成的可执行代码, 包含占位符用于填充对象和参数',
  `plugin_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '执行所需的插件ID, 如使用自定义方法则关联插件表',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态: 0:创建;1:删除',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `name_op_type` (`name`,`op_type`)
) ENGINE=INNODB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COMMENT='规则操作符配置表'


CREATE TABLE `rule_item` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `rule_type` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '规则类型',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '规则名称',
  `en_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '英文名称',
  `params` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '规则参数，json格式',
  `script_code` Text    COMMENT '脚本代码',
  `return_type` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '返回类型',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name_rule_type` (`name`,`rule_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='规则配置信息';



CREATE TABLE `model_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '模型名称，中文',
  `en_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '英文名称',
  `code` Text    COMMENT 'model groovy 代码',
  `biz_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '模型所属的业务场景的id',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_en_name` (`en_name`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='模型信息';



CREATE TABLE `model_field` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '模型名称，中文',
  `en_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '英文名称',
  `field_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '数据类型，如：java.lang.int',
  `model_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '所属model的id',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_en_name` (`en_name`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='模型字段信息，与model_info表是多对一关系';



CREATE TABLE `rule_group` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `biz_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '所属业务场景id',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '规则组名称',
  `type` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '规则组执行类型：single_match-命中退出,full_match-全量匹配',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态: 1:有效，0：无效',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='规则包信息';



CREATE TABLE `app_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '接入方名称',
  `description` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '接入方描述',
  `ips` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '接入方ip地址，多个以英文逗号分割',
  `access_key` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '签名密码',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态: 1:有效，0：无效',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='接入方信息';



CREATE TABLE `business` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '业务所属接入方',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '接入方名称',
  `description` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '接入方描述',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态: 1:有效，0：无效',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='接入方信息';


CREATE TABLE `plugin_item` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '插件名称',
  `description` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '插件描述',
  `code` Text    COMMENT 'plugin groovy 代码',
  `input_params` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '入参',
  `return_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '返回值类型',
  `reserved1` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '保留字段',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态: 1:有效，0：无效',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志: 0:正常，1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='插件信息，注意，一个插件仅提供一个方法';