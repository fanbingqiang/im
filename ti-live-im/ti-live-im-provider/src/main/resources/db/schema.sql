CREATE DATABASE IF NOT EXISTS `im_live` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `im_live`;

CREATE TABLE IF NOT EXISTS `im_message` (
  `msg_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
  `sender_name` VARCHAR(100) NOT NULL COMMENT '发送者名称',
  `room_id` BIGINT NOT NULL COMMENT '房间ID',
  `msg_type` TINYINT NOT NULL COMMENT '消息类型：1-文本 2-图片 3-礼物 4-系统 5-进入房间 6-离开房间',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `gift_info` TEXT DEFAULT NULL COMMENT '礼物信息（JSON格式）',
  `is_barrage` TINYINT NOT NULL DEFAULT 0 COMMENT '是否弹幕消息：0-否 1-是',
  `send_time` BIGINT NOT NULL COMMENT '发送时间戳',
  `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`msg_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IM消息表';
