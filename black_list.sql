/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : xzzj_bbs

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2019-11-22 17:29:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for black_list
-- ----------------------------
DROP TABLE IF EXISTS `black_list`;
CREATE TABLE `black_list` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL COMMENT '本用户ID',
  `other_id` int(11) DEFAULT NULL COMMENT '其他用户ID',
  `created_time` datetime DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `other_id` (`other_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
