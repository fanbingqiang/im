CREATE DATABASE IF NOT EXISTS `id_generator` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `id_generator`;

CREATE TABLE IF NOT EXISTS `cosid_machine` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `namespace` VARCHAR(50) NOT NULL DEFAULT 'default' COMMENT '命名空间',
    `biz_tag` VARCHAR(50) NOT NULL COMMENT '业务标识',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    `last_timestamp` BIGINT NOT NULL DEFAULT 0 COMMENT '上次分配时间戳',
    `worker_id` BIGINT NOT NULL DEFAULT 0 COMMENT '机器ID',
    `worker_id_instance` VARCHAR(50) DEFAULT NULL COMMENT '机器ID实例标识',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_namespace_biz_tag` (`namespace`, `biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CosId机器号分配表';

CREATE TABLE IF NOT EXISTS `cosid_segment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `namespace` VARCHAR(50) NOT NULL DEFAULT 'default' COMMENT '命名空间',
    `biz_tag` VARCHAR(50) NOT NULL COMMENT '业务标识',
    `max_id` BIGINT NOT NULL DEFAULT 1 COMMENT '最大ID',
    `step` INT NOT NULL DEFAULT 1000 COMMENT '步长',
    `delta` INT NOT NULL DEFAULT 1 COMMENT '增量',
    `last_update_time` BIGINT NOT NULL DEFAULT 0 COMMENT '最后更新时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_namespace_biz_tag` (`namespace`, `biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CosId号段表';
