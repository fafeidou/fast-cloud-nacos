/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 16/01/2020 10:21:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------
BEGIN;
INSERT INTO `demo` VALUES (1, '!23');
COMMIT;

-- ----------------------------
-- Table structure for emp
-- ----------------------------
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sal` float(255,0) DEFAULT NULL,
  `deptno` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emp
-- ----------------------------
BEGIN;
INSERT INTO `emp` VALUES (8, 'emp1001', 100, 10);
INSERT INTO `emp` VALUES (9, 'emp1002', 200, 10);
INSERT INTO `emp` VALUES (10, 'emp1003', 300, 20);
INSERT INTO `emp` VALUES (11, 'emp1004', 400, 20);
INSERT INTO `emp` VALUES (12, 'emp1005', 500, 30);
INSERT INTO `emp` VALUES (13, 'emp1006', 600, 30);
COMMIT;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `last_modify_time` varchar(255) DEFAULT NULL,
  `rule_key` varchar(255) NOT NULL,
  `version` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9yepjak9olg92holwkr8p3l0f` (`rule_key`),
  UNIQUE KEY `UK_ilmbp99kyt6gy10224pc9bl6n` (`version`),
  UNIQUE KEY `UK_ei48upwykmhx9r5p7p4ndxvgn` (`last_modify_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rule
-- ----------------------------
BEGIN;
INSERT INTO `rule` VALUES (1, 'package plausibcheck.adress\n\nimport com.neo.drools.model.Address;\nimport com.neo.drools.model.fact.AddressCheckResult;\n\nrule \"9Postcode should be filled with exactly 5 numbers\"\n    when\n        address : Address(postcode != null, postcode contains 1)\n        checkResult : AddressCheckResult();\n    then\n        checkResult.setPostCodeResult(true);\n		System.out.println(\"规则中打印日志：校验通过!\");\nend', NULL, NULL, '9Postcode should be filled with exactly 5 numbers', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
