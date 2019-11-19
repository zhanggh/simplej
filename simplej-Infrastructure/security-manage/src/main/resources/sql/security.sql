


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