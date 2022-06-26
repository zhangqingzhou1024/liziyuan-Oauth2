/*
Navicat MySQL Data Transfer

Source Server         : localhost-docker-mysql
Source Server Version : 50722
Source Host           : 127.0.0.1:3306
Source Database       : rbac_db

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2022-06-26 20:49:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `auth_access_token`;
CREATE TABLE `auth_access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `access_token` varchar(255) NOT NULL COMMENT 'Access Token',
  `user_id` int(11) NOT NULL COMMENT '关联的用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `client_id` int(11) NOT NULL COMMENT '接入的客户端ID',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `grant_type` varchar(50) DEFAULT NULL COMMENT '授权类型，比如：authorization_code',
  `scope` varchar(100) DEFAULT NULL COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int(11) DEFAULT NULL COMMENT '最后更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Access Token信息表';

-- ----------------------------
-- Records of auth_access_token
-- ----------------------------
INSERT INTO `auth_access_token` VALUES ('1', '1.8937ec3167e27c675b8c9450810dc666438d73aa.2592000.1658780004', '11', 'CSDN', '1', '1658780004', 'authorization_code', 'super', '11', '2022-06-25 13:47:11', '11', '2022-06-26 04:13:24');

-- ----------------------------
-- Table structure for auth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_details`;
CREATE TABLE `auth_client_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `client_id` varchar(100) NOT NULL COMMENT '接入的客户端ID',
  `client_name` varchar(100) DEFAULT NULL,
  `client_secret` varchar(255) NOT NULL COMMENT '接入的客户端的密钥',
  `redirect_uri` varchar(1000) NOT NULL COMMENT '回调地址',
  `description` varchar(1000) DEFAULT NULL COMMENT '描述信息',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int(11) DEFAULT NULL COMMENT '最后更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `status` int(2) DEFAULT '0' COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='接入的客户端信息表';

-- ----------------------------
-- Records of auth_client_details
-- ----------------------------
INSERT INTO `auth_client_details` VALUES ('1', '9gnffNcXT2hDUXf06w2z1y3e', 'CSDN-客户端', 'qRJJeRgJhypMXa0kwDGxg5coI4sAO1hV', 'http://127.0.0.1:7080/login', '这是一个测试客户端服务', '11', '2022-06-25 11:54:39', '11', '2022-06-25 11:54:39', '1');

-- ----------------------------
-- Table structure for auth_client_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_user`;
CREATE TABLE `auth_client_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_client_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `auth_scope_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户对某个接入客户端的授权信息';

-- ----------------------------
-- Records of auth_client_user
-- ----------------------------
INSERT INTO `auth_client_user` VALUES ('1', '1', '11', '2');
INSERT INTO `auth_client_user` VALUES ('2', '1', '11', '1');

-- ----------------------------
-- Table structure for auth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `auth_refresh_token`;
CREATE TABLE `auth_refresh_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `token_id` int(11) NOT NULL COMMENT '表auth_access_token对应的Access Token记录',
  `refresh_token` varchar(255) NOT NULL COMMENT 'Refresh Token',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int(11) DEFAULT NULL COMMENT '最后更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Refresh Token信息表';

-- ----------------------------
-- Records of auth_refresh_token
-- ----------------------------
INSERT INTO `auth_refresh_token` VALUES ('1', '1', '2.4d12a466d6fb1ef65e8148c93f490b47adcace98.31536000.1687724004', '1687724004', '11', '2022-06-25 13:48:18', '11', '2022-06-26 04:13:24');

-- ----------------------------
-- Table structure for auth_scope
-- ----------------------------
DROP TABLE IF EXISTS `auth_scope`;
CREATE TABLE `auth_scope` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scope_name` varchar(100) NOT NULL COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='可被访问的用户权限表';

-- ----------------------------
-- Records of auth_scope
-- ----------------------------
INSERT INTO `auth_scope` VALUES ('1', 'super', '用户所有信息');
INSERT INTO `auth_scope` VALUES ('2', 'basic', '用户基本信息');

-- ----------------------------
-- Table structure for func
-- ----------------------------
DROP TABLE IF EXISTS `func`;
CREATE TABLE `func` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `status` int(2) DEFAULT '1' COMMENT '1表示正常使用；2表示已被禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of func
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'manager', '管理员');
INSERT INTO `role` VALUES ('2', 'user', '普通用户');

-- ----------------------------
-- Table structure for role_func
-- ----------------------------
DROP TABLE IF EXISTS `role_func`;
CREATE TABLE `role_func` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `func_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `roleId` (`role_id`),
  CONSTRAINT `roleId` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_func
-- ----------------------------

-- ----------------------------
-- Table structure for sso_access_token
-- ----------------------------
DROP TABLE IF EXISTS `sso_access_token`;
CREATE TABLE `sso_access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `access_token` varchar(255) NOT NULL COMMENT 'Access Token',
  `user_id` int(11) NOT NULL COMMENT '关联的用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `ip` varchar(50) DEFAULT NULL COMMENT '用户来源IP',
  `client_id` int(11) DEFAULT NULL,
  `channel` varchar(50) DEFAULT '' COMMENT '表示这个Token用于什么渠道，比如：官网、APP1、APP2等等',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int(11) DEFAULT NULL COMMENT '最后更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单点登录的Access Token信息表';

-- ----------------------------
-- Records of sso_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for sso_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sso_client_details`;
CREATE TABLE `sso_client_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(100) DEFAULT NULL COMMENT '接入单点登录的系统名称',
  `description` varchar(1000) DEFAULT NULL,
  `redirect_url` varchar(255) NOT NULL COMMENT '请求Token的回调URL',
  `logout_url` varchar(255) NOT NULL COMMENT '注销URL',
  `status` int(2) DEFAULT NULL COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单点登录的回调域名的白名单';

-- ----------------------------
-- Records of sso_client_details
-- ----------------------------

-- ----------------------------
-- Table structure for sso_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `sso_refresh_token`;
CREATE TABLE `sso_refresh_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `token_id` int(11) NOT NULL COMMENT '表sso_access_token对应的Access Token记录',
  `refresh_token` varchar(255) NOT NULL COMMENT 'Refresh Token',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `create_user` int(11) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int(11) DEFAULT NULL COMMENT '最后更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单点登录的Refresh Token信息表';

-- ----------------------------
-- Records of sso_refresh_token
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `mobile` varchar(30) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('10', 'zqz', '$5$AFjMjbSS$AuXhIepUAGTgHwZFI8BIpgUs.NPQYXUeNldIaOy35S3', '12306', 'admin@zifangsky.cn', '2022-06-25 11:44:44', '2022-06-25 11:44:44', '1');
INSERT INTO `user` VALUES ('11', 'CSDN', '$5$S7wQ74.W$Uv5mECYGeW9.2AgV1fkRPnkc0qYNe1NsCAUPsXknnK1', '12306', 'admin@zifangsky.cn', '2022-06-25 11:45:02', '2022-06-25 11:45:02', '1');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`user_id`),
  CONSTRAINT `userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
