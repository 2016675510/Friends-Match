/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 80024
Source Host           : 8.138.93.93:3306
Source Database       : user

Target Server Type    : MYSQL
Target Server Version : 80024
File Encoding         : 65001

Date: 2024-03-28 23:57:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '队伍名称',
  `description` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `maxNum` int NOT NULL DEFAULT '1' COMMENT '最大人数',
  `expireTime` datetime DEFAULT NULL COMMENT '过期时间',
  `userId` bigint DEFAULT NULL COMMENT '用户id',
  `status` int NOT NULL DEFAULT '0' COMMENT '0 - 公开，1 - 私有，2 - 加密',
  `password` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='队伍';

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES ('1', 'test', 'test', '3', '2024-04-24 00:00:00', '8030', '0', '', '2024-03-24 17:03:24', '2024-03-24 18:23:00', '1');
INSERT INTO `team` VALUES ('2', '321', '12321321321321', '3', '2025-03-24 00:00:00', '8031', '0', '', '2024-03-24 17:04:45', '2024-03-24 17:04:45', '0');
INSERT INTO `team` VALUES ('3', '532313', '532313创建', '3', '2024-04-24 00:00:00', '8032', '0', '', '2024-03-24 17:06:47', '2024-03-24 17:06:47', '0');
INSERT INTO `team` VALUES ('4', '加密房', '1234', '3', '2024-04-24 00:00:00', '8030', '2', '1234', '2024-03-24 18:24:52', '2024-03-24 18:24:52', '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
  `userAccount` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号',
  `avatarUrl` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
  `gender` bigint DEFAULT NULL COMMENT '性别',
  `profile` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户简介',
  `userPassword` varchar(512) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话',
  `tags` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签列表',
  `email` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `userStatus` int DEFAULT '1' COMMENT '状态',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDelete` bigint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `role` int NOT NULL DEFAULT '0',
  `planetCode` int DEFAULT NULL COMMENT '认证ID\r\n',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8035 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('8029', null, '432323', 'https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', null, null, 'e1ee942297e97f24afcf88c6c41818f8', null, '[\"女\",\"大二\",\"java\"]', null, '1', '2024-03-19 10:29:04', '2024-03-24 17:52:46', '0', '0', '324334');
INSERT INTO `user` VALUES ('8030', null, '1234567', 'https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', null, null, 'afb117a14b21a3f9bdb95917033dcf95', '15176451852', '[\"Java\",\"男\",\"Python\"]', null, '1', '2024-03-24 16:57:53', '2024-03-24 18:49:55', '0', '0', '1234567');
INSERT INTO `user` VALUES ('8031', '123321', '123321', 'https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', null, null, 'de47182995da3eae880efee9ef4b56da', '3213213', '[\"321321\"]', '3213213', '1', '2024-03-24 17:03:39', '2024-03-24 17:52:45', '0', '0', '123321');
INSERT INTO `user` VALUES ('8032', null, '532313', 'https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', null, null, 'cf1757756da352d8e2cbefd20eb88704', null, '[\"c\",\"女\",\"Java\"]', null, '1', '2024-03-24 17:04:25', '2024-03-24 17:52:45', '0', '0', '532313');
INSERT INTO `user` VALUES ('8033', null, 'Hecq', null, null, null, 'ae8176b4caaf5f39283361ae3eacc71f', null, null, null, '1', '2024-03-24 18:20:07', '2024-03-24 18:20:07', '0', '0', '20230201');
INSERT INTO `user` VALUES ('8034', null, '432313', null, null, null, '3b4a4c92fd415ff01100a8cec8c00cf9', null, '[\"大一\",\"Java\",\"Python\",\"go\"]', null, '1', '2024-03-24 19:21:31', '2024-03-24 19:22:05', '0', '0', '432313');

-- ----------------------------
-- Table structure for user_team
-- ----------------------------
DROP TABLE IF EXISTS `user_team`;
CREATE TABLE `user_team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint DEFAULT NULL COMMENT '用户id',
  `teamId` bigint DEFAULT NULL COMMENT '队伍id',
  `joinTime` datetime DEFAULT NULL COMMENT '加入时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户队伍关系';

-- ----------------------------
-- Records of user_team
-- ----------------------------
INSERT INTO `user_team` VALUES ('1', '8030', '1', '2024-03-24 09:03:24', '2024-03-24 17:03:24', '2024-03-24 18:23:00', '1');
INSERT INTO `user_team` VALUES ('2', '8031', '1', '2024-03-24 09:03:53', '2024-03-24 17:03:52', '2024-03-24 17:04:28', '1');
INSERT INTO `user_team` VALUES ('3', '8031', '2', '2024-03-24 09:04:45', '2024-03-24 17:04:45', '2024-03-24 17:04:45', '0');
INSERT INTO `user_team` VALUES ('4', '8032', '1', '2024-03-24 09:06:05', '2024-03-24 17:06:04', '2024-03-24 18:23:00', '1');
INSERT INTO `user_team` VALUES ('5', '8032', '2', '2024-03-24 09:06:14', '2024-03-24 17:06:14', '2024-03-24 17:06:14', '0');
INSERT INTO `user_team` VALUES ('6', '8032', '3', '2024-03-24 09:06:48', '2024-03-24 17:06:47', '2024-03-24 17:06:47', '0');
INSERT INTO `user_team` VALUES ('7', '8030', '2', '2024-03-24 10:22:52', '2024-03-24 18:22:51', '2024-03-24 18:23:19', '1');
INSERT INTO `user_team` VALUES ('8', '8030', '3', '2024-03-24 10:22:54', '2024-03-24 18:22:53', '2024-03-24 18:23:14', '1');
INSERT INTO `user_team` VALUES ('9', '8030', '4', '2024-03-24 10:24:52', '2024-03-24 18:24:52', '2024-03-24 18:24:52', '0');
INSERT INTO `user_team` VALUES ('10', '8030', '2', '2024-03-24 10:49:39', '2024-03-24 18:49:38', '2024-03-24 18:49:38', '0');
INSERT INTO `user_team` VALUES ('11', '8030', '3', '2024-03-24 11:15:02', '2024-03-24 19:15:01', '2024-03-24 19:15:09', '1');
