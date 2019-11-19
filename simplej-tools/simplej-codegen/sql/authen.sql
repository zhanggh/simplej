CREATE TABLE `department_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '部门编号',
  `name` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '部门名称',
  `level` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '部门级别，1：一级部门，2：二级部门',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '备注',
  `description` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '部门描述',
  `manager_user_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '部门负责人用户编号',
  `telephone` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '部门固话',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `parent_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '上级部门编号',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `department_idx_code` (`code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='部门信息';


CREATE TABLE `group_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `group_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '小组编号',
  `group_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '小组名',
  `level` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '小组级别',
  `group_manager_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '小组负责人用户编号',
  `description` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '小组负责内容描述',
  `department_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '所属部门编号',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '备注',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `parent_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '上级小组编号',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `group_idx_code` (`group_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='小组信息，一个部门下面对应多个小组';


CREATE TABLE `menu_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `menu_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `menu_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '菜单名',
  `menu_level` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '菜单级别，1：一级菜单，2：二级菜单，3：三级菜单，4：四级菜单',
  `parent_menu_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '父级菜单编号',
  `menu_uri` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '菜单uri',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '菜单描述',
  `menu_status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '菜单状态，1：启用，0禁用',
  `order_num` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '菜单顺序，数值越小，排序越前面',
  `platform` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '所属平台',
  `menu_type` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '菜单类型：0：平台菜单 1：外部地址',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `icon` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '图标地址',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `menu_idx_menu_code` (`menu_code`),
  KEY `menu_idx_platform_level` (`menu_level`,`platform`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='菜单信息表';


CREATE TABLE `role_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '角色编号',
  `role_name` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '角色名称',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '角色备注',
  `platform` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '所属平台',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `create_time` TIMESTAMP(3) NULL DEFAULT '1970-01-02 00:00:00.000',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

CREATE TABLE `role_menu_ref` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '角色编号',
  `menu_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '备注',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`Id`),
  KEY `role_menu_idx_role_code_menu_code` (`role_code`,`menu_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='角色与菜单关联表';


CREATE TABLE `sequence_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `sequence_name` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '序列名',
  `sequence_key` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '序列key',
  `current_value` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '当前值',
  `step` INT(11) UNSIGNED NOT NULL DEFAULT '10' COMMENT '步长',
  `is_cycle` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '当序列满了之后是否归零开始，0否，1是',
  `data_version` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '数据版本号，用于乐观锁',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `sequence_idx_key` (`sequence_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='序列号生成器表';


CREATE TABLE `user_info` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_code` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '用户登录号',
  `user_name` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '用户名称',
  `user_passwd` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '用户密码，经过加密后存储',
  `weixin_no` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '微信号',
  `mobile` VARCHAR(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `qq_no` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'qq号码',
  `telephone` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '固话号码',
  `email` VARCHAR(40) NOT NULL DEFAULT '' COMMENT '邮件地址',
  `sex` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '性别：0男性，1女生',
  `age` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '年龄',
  `province` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '所在省份',
  `city` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '所在城市',
  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '详细地址',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '备注信息',
  `education` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0:小学 1：初中  2：高中  3：本科 4：硕士 5博士 6：其他',
  `height` DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT '0.00' COMMENT '身高',
  `weight` DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT '0.00' COMMENT '体重',
  `Interest` VARCHAR(255) DEFAULT '' COMMENT '兴趣爱好',
  `marital_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '婚姻状态：0未婚，1已婚',
  `occupation` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '职业',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `user_info_idx_user_code` (`user_code`),
  KEY `user_info_idx_user_name` (`user_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户信息表';


CREATE TABLE `user_role_ref` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_code` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '用户号',
  `role_code` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '角色号',
  `platform` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '所属平台标志',
  `remark` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '备注',
  `is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '软删除标志，0正常，1删除',
  `create_time` TIMESTAMP(3) NOT NULL DEFAULT '1970-01-02 00:00:00.000' COMMENT '创建时间',
  `created_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  KEY `user_role_idx_user_code_role_code_platform` (`user_code`,`role_code`,`platform`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';



CREATE TABLE `opt_log` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `biz_type` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '业务类型编码',
  `dest_table` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '目标操作表',
  `operator` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '操作人',
  `op_type` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '操作类型：query/update/insert/delete',
  `input` TEXT NOT NULL COMMENT '入参',
  `user_role` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '用户角色',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '创建时间（操作时间）',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标志：0-正常，1-删除',
  `reserved` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '保留字段，一般不建议使用',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='操作日志表';




CREATE TABLE `key_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `key_type` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '秘钥类型：aes/des/desede/r4/r2/rsa/dsa/ec/other',
  `iv` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '偏移量',
  `mode` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '模式，如：AES/ECB/PKCS5Padding',
  `version` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '秘钥加密版本号',
  `key_name` VARCHAR(32) NOT NULL DEFAULT ''  COMMENT '秘钥名称',
  `key` VARCHAR(512) NOT NULL DEFAULT ''  COMMENT '秘钥内容，经过了主密钥加密并且base64后的字符串保存于此',
  `business` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '业务描述',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '创建时间（操作时间）',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标志：0-正常，1-删除',
  `reserved` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '保留字段，一般不建议使用',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秘钥信息表';



CREATE TABLE `app_key_ref` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '接入方id',
  `key_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '秘钥id',
  `business` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '业务描述',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '创建时间（操作时间）',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标志：0-正常，1-删除',
  `reserved` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '保留字段，一般不建议使用',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_key_id` (`key_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='接入方对应的key关联管理表';



CREATE TABLE `certificate_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `cert_sequence` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '证书序列号',
  `key_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '秘钥id，所有证书保存的时候，会把秘钥存储在key_info表',
  `cert_type` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '证书类型：1-公钥证书，2-私钥证书',
  `format` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '证书格式：der/cer/crt/pem/jks/pfx/p12',
  `cert_context` TEXT  COMMENT '证书文件内容',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '创建时间（操作时间）',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标志：0-正常，1-删除',
  `reserved` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '保留字段，一般不建议使用',
  PRIMARY KEY (`id`),
  KEY `idx_cert_sequence` (`cert_sequence`),
  KEY `idx_key_id` (`key_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='证书管理信息表';

CREATE TABLE `app_cert_ref` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '接入方id',
  `cert_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '证书记录id',
  `business` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '业务描述',
  `create_time` TIMESTAMP NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '创建时间（操作时间）',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` VARCHAR(24) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_deleted` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '软删除标志：0-正常，1-删除',
  `reserved` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '保留字段，一般不建议使用',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_cert_id` (`cert_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='接入方对应的证书关联管理表';