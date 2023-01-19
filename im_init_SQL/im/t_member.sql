-- --------------------------------------------------------
-- 主機:                           192.168.1.32
-- 伺服器版本:                        8.0.29 - MySQL Community Server - GPL
-- 伺服器作業系統:                      Linux
-- HeidiSQL 版本:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 傾印  資料表 im.t_member 結構
CREATE TABLE IF NOT EXISTS `t_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_uid` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'UID',
  `_nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `_member_name` varchar(50) NOT NULL COMMENT '帐号',
  `_password_nouse` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码(弃用,以t_member_password替代)',
  `_trade_pwd` varchar(100) DEFAULT NULL COMMENT '交易密码',
  `_account_type` tinyint DEFAULT '1' COMMENT '帐号类型 1:普通帐号, 2:管理号',
  `_phone` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `_invite_code` varchar(10) DEFAULT NULL COMMENT '邀请码',
  `_invite_member_id` bigint DEFAULT NULL COMMENT '邀请会员',
  `_avatar_url` varchar(100) DEFAULT NULL COMMENT '头像',
  `_email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `_signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `_gender` tinyint DEFAULT '1' COMMENT '性别 1: 保密, 2: 男, 3: 女',
  `_login_status` tinyint DEFAULT NULL COMMENT '登陆状态 0: 禁止登陆, 1: 正常登入',
  `_last_active_time_nouse` datetime DEFAULT NULL COMMENT '最后活跃时间(弃用,以t_member_password替代)',
  `_login_error_count_nouse` int NOT NULL DEFAULT '0' COMMENT '登陆错误次数(弃用,以t_member_password替代)',
  `_register_ip` varchar(30) NOT NULL COMMENT '注册IP',
  `_register_area` varchar(30) NOT NULL COMMENT '注册地区',
  `_balance` decimal(20,8) DEFAULT '0.00000000' COMMENT '馀额',
  `_login_enable` tinyint DEFAULT '1' COMMENT '登陆开关',
  `_add_friend_enable` tinyint DEFAULT '1' COMMENT '添加好友开关',
  `_create_group_enable` tinyint DEFAULT '1' COMMENT '建群开关',
  `_admin_enable` tinyint DEFAULT '0' COMMENT '管理号开关',
  `_qr_code_token` varchar(36) DEFAULT NULL COMMENT 'QRCode验证',
  `_channel` varchar(255) DEFAULT NULL COMMENT '渠道',
  `_memo` varchar(300) DEFAULT NULL COMMENT '备注',
  `_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key_member_name` (`_member_name`) USING BTREE,
  UNIQUE KEY `unique_key_phone` (`_phone`) USING BTREE,
  UNIQUE KEY `unique_key_uid` (`_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 COMMENT='会员列表';

-- 正在傾印表格  im.t_member 的資料：~10 rows (近似值)
/*!40000 ALTER TABLE `t_member` DISABLE KEYS */;
INSERT INTO `t_member` (`id`, `_uid`, `_nick_name`, `_member_name`, `_password_nouse`, `_trade_pwd`, `_account_type`, `_phone`, `_invite_code`, `_invite_member_id`, `_avatar_url`, `_email`, `_signature`, `_gender`, `_login_status`, `_last_active_time_nouse`, `_login_error_count_nouse`, `_register_ip`, `_register_area`, `_balance`, `_login_enable`, `_add_friend_enable`, `_create_group_enable`, `_admin_enable`, `_qr_code_token`, `_channel`, `_memo`, `_create_time`, `_update_time`) VALUES
	(1, 'nygqmws2k', '管理员kang', 'kang', NULL, NULL, 2, '18400000000', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:02:02', '2023-01-05 16:02:02'),
	(2, 'gygqmws2k', '管理员ron', 'ronron', NULL, NULL, 2, '18400000001', NULL, NULL, '{{domain}}/avatar/AVATAR_2023010516083044859650.jpg', '', NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:02:29', '2023-01-05 16:16:58'),
	(3, '9ygqmws2k', '管理员paige', 'paige', NULL, NULL, 2, '18400000002', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:02:53', '2023-01-05 16:02:53'),
	(4, '2ygqmws2k', '管理员tim', 'timtim', NULL, NULL, 2, '18400000003', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:03:18', '2023-01-05 16:03:18'),
	(5, 'vygqmws2k', '管理员phil', 'phil', NULL, NULL, 2, '18400000004', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:03:45', '2023-01-05 16:03:45'),
	(6, 'oygqmws2k', '管理员alex', 'alex', NULL, NULL, 2, '18400000005', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:04:10', '2023-01-05 16:04:10'),
	(7, 'hygqmws2k', '管理员tony', 'tony', NULL, NULL, 2, '18400000006', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:04:38', '2023-01-05 16:04:38'),
	(8, 'aygqmws2k', '管理员sharon', 'sharon', NULL, NULL, 2, '18400000007', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:05:34', '2023-01-05 16:05:34'),
	(9, '3ygqmws2k', '管理员benny', 'benny', NULL, NULL, 2, '18400000008', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:05:59', '2023-01-05 16:05:59'),
	(10, 'wygqmws2k', '管理员aiden', 'aiden', NULL, NULL, 2, '18400000009', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:06:22', '2023-01-05 16:06:22'),
	(11, 'pygqmws2k', '管理员lance', 'lance', NULL, NULL, 2, '18400000010', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:06:47', '2023-01-05 16:06:47'),
	(12, 'iygqmws2k', '管理员ken', 'kenken', NULL, NULL, 2, '18400000011', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:07:13', '2023-01-05 16:07:13'),
	(13, 'bygqmws2k', '管理员willie', 'willie', NULL, NULL, 2, '18400000012', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:07:35', '2023-01-05 16:07:35'),
	(14, '4ygqmws2k', '恋爱狙击手-霓儿', 'B3362346430363365', NULL, NULL, 1, '18410000000', NULL, NULL, '{{domain}}/avatar/AVATAR_2023010516093551603867.jpg', NULL, NULL, 3, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:09:36', '2023-01-05 16:09:36'),
	(15, 'xygqmws2k', '恋爱狙击手-卡拉', 'B3632303361336236', NULL, NULL, 1, '18410000001', NULL, NULL, '{{domain}}/avatar/AVATAR_2023010516102965739110.jpg', NULL, NULL, 3, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Mac OS X', NULL, '2023-01-05 16:10:29', '2023-01-05 16:10:29'),
	(16, 'jygqmws2k', 'test123', 'test123', NULL, NULL, 2, '18400099890', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, '127.0.0.1', '本地', 0.00000000, 1, 1, 1, 0, NULL, 'Windows', NULL, '2023-01-05 16:13:58', '2023-01-05 16:13:58');
/*!40000 ALTER TABLE `t_member` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
