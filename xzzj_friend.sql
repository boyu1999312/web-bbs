/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : xzzj_bbs

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2019-11-22 17:29:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xzzj_friend
-- ----------------------------
DROP TABLE IF EXISTS `xzzj_friend`;
CREATE TABLE `xzzj_friend` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '本用户Id',
  `other_id` int(11) NOT NULL COMMENT '好友ID',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `created_time` datetime DEFAULT NULL COMMENT '成为好友时间',
  `updated_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `state` int(11) DEFAULT NULL COMMENT '好友状态 1-好友 2-待验证 3-拉黑 4-陌生人',
  `friendly_level` int(11) DEFAULT '0' COMMENT '友好关系',
  `jurisdiction` int(11) DEFAULT '1' COMMENT '权限 1-允许看卡片 2-不允许看卡片',
  `friends_group` int(11) DEFAULT '0' COMMENT '组',
  `version` int(11) DEFAULT '1' COMMENT '修改次数',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `other_id` (`other_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
