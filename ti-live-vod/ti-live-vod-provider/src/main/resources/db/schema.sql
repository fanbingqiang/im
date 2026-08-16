CREATE DATABASE IF NOT EXISTS `ti_live` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `ti_live`;

CREATE TABLE IF NOT EXISTS `vod_video` (
  `video_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '视频ID',
  `user_id` BIGINT NOT NULL COMMENT '上传用户ID',
  `user_name` VARCHAR(100) NOT NULL COMMENT '用户名',
  `title` VARCHAR(200) NOT NULL COMMENT '视频标题',
  `description` TEXT COMMENT '视频描述',
  `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '视频封面',
  `video_url` VARCHAR(500) NOT NULL COMMENT '视频地址',
  `duration` INT NOT NULL DEFAULT 0 COMMENT '视频时长（秒）',
  `play_count` INT NOT NULL DEFAULT 0 COMMENT '播放次数',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核 1-已发布 2-审核失败',
  `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`video_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点播视频表';
