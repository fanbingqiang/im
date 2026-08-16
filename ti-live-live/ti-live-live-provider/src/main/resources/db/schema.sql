CREATE DATABASE IF NOT EXISTS `ti_live` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `ti_live`;

CREATE TABLE IF NOT EXISTS `live_stream` (
  `stream_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '直播流ID',
  `anchor_id` BIGINT NOT NULL COMMENT '主播ID',
  `anchor_name` VARCHAR(100) NOT NULL COMMENT '主播名称',
  `room_id` BIGINT NOT NULL COMMENT '直播间ID',
  `room_name` VARCHAR(100) NOT NULL COMMENT '直播间名称',
  `title` VARCHAR(200) NOT NULL COMMENT '直播标题',
  `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '直播封面',
  `push_url` VARCHAR(500) NOT NULL COMMENT '推流地址',
  `pull_url` VARCHAR(500) NOT NULL COMMENT '拉流地址',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-准备中 1-直播中 2-已结束',
  `viewer_count` INT NOT NULL DEFAULT 0 COMMENT '观看人数',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`stream_id`),
  KEY `idx_anchor_id` (`anchor_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播流表';
