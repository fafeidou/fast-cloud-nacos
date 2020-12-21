CREATE DATABASE `bank1` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'

DROP TABLE
IF
	EXISTS `account_info`;
CREATE TABLE `account_info` (
	`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
	`account_name` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '户 主姓名',
	`account_no` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '银行 卡号',
	`account_password` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '帐户密码',
	`account_balance` DOUBLE NULL DEFAULT NULL COMMENT '帐户余额',
	PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 5 CHARACTER
SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

INSERT INTO `account_info`
VALUES
	( 2, '张三的账户', '1', '', 10000 );


CREATE DATABASE `bank2` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

CREATE TABLE `account_info` (
	`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
	`account_name` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '户 主姓名',
	`account_no` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '银行 卡号',
	`account_password` VARCHAR ( 100 ) CHARACTER
	SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '帐户密码',
	`account_balance` DOUBLE NULL DEFAULT NULL COMMENT '帐户余额',
	PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 5 CHARACTER
SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;
INSERT INTO `account_info`
VALUES
	( 3, '李四的账户', '2', NULL, 0 )

----------bank1、bank2都需要执行
DROP TABLE
IF
	EXISTS `de_duplication`;
CREATE TABLE `de_duplication` (
	`tx_no` VARCHAR ( 64 ) COLLATE utf8_bin NOT NULL,
	`create_time` datetime ( 0 ) NULL DEFAULT NULL,
	PRIMARY KEY ( `tx_no` ) USING BTREE
) ENGINE = INNODB CHARACTER
SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;