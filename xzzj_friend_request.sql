/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : xzzj_bbs

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2019-11-25 17:34:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xzzj_friend_request
-- ----------------------------
DROP TABLE IF EXISTS `xzzj_friend_request`;
CREATE TABLE `xzzj_friend_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `other_id` int(11) DEFAULT NULL COMMENT '其他用户ID',
  `user_nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `other_nickname` varchar(255) DEFAULT NULL COMMENT '其他用户昵称',
  `message` varchar(255) DEFAULT NULL COMMENT '附加消息',
  `state` int(4) DEFAULT NULL COMMENT '1-进行中 2-同意 3-拒绝',
  `created_time` datetime DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `select` (`user_id`,`other_id`,`state`) COMMENT '根据用户id和状态查询'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
