-- 建库
create database tllive character set utf8mb4 collate utf8mb4_general_ci;

-- 切换库
use tllive;

-- 用户基础信息表
CREATE TABLE `t_user` (
    `user_id` bigint NOT NULL DEFAULT '1' COMMENT '用户id',
    `nick_name` varchar(35) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '头像',
    `true_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '真实姓名',
    `sex` tinyint(1) DEFAULT NULL COMMENT '性别 0男, 1女',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB COMMENT='用户基础信息表';

-- 用户手机绑定表
CREATE TABLE `t_user_phone` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `phone` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT '手机号',
    `user_id` bigint DEFAULT '1' COMMENT '用户id',
    `status` tinyint DEFAULT '1' COMMENT '状态(0无效, 1有效)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone` (`phone`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户手机表';

-- 短信发送记录表
CREATE TABLE `t_sms` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `code` int unsigned DEFAULT '0' COMMENT '验证码',
    `phone` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机号',
    `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='短信发送记录';