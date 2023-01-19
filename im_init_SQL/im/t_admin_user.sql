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

-- 傾印  資料表 im.t_admin_user 結構
CREATE TABLE IF NOT EXISTS `t_admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `_role_id` bigint NOT NULL COMMENT '角色ID',
  `_nick_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `_user_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `_member_id` bigint DEFAULT NULL COMMENT '聊天室身分',
  `_password` varchar(32) NOT NULL COMMENT '密码',
  `_status` tinyint DEFAULT '1' COMMENT '账户状态 0:禁用 1:启用',
  `_parent_id` bigint NOT NULL COMMENT '父账户ID',
  `_full_path` varchar(1000) DEFAULT NULL COMMENT '代理階層路徑，含自己，並且斜線結尾',
  `_brand_name` varchar(200) DEFAULT NULL COMMENT '所属品牌',
  `_memo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `_phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `_email` varchar(100) DEFAULT NULL COMMENT '信箱',
  `_google_secret` varchar(20) DEFAULT NULL COMMENT 'GA密钥',
  `_google_enable` tinyint(1) DEFAULT '0' COMMENT 'GA开关',
  `_login_error` int DEFAULT '0' COMMENT '连续登录失败次数',
  `_login_frozen` datetime DEFAULT NULL COMMENT '登录冻结时间',
  `_login_succeed` int DEFAULT '0' COMMENT '登录成功次数',
  `_login_fail` int DEFAULT '0' COMMENT '登录失败次数',
  `_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `_register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `_otp_key` varchar(100) DEFAULT NULL COMMENT 'OTP密钥',
  `_is_verify_otp_key` tinyint DEFAULT '0' COMMENT '是否通过OTP绑定验证 0: 未绑定 1: 绑定 ',
  `version` int unsigned DEFAULT '0' COMMENT '版本信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key` (`_user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COMMENT='管理者用户列表';

-- 正在傾印表格  im.t_admin_user 的資料：~11 rows (近似值)
/*!40000 ALTER TABLE `t_admin_user` DISABLE KEYS */;
INSERT INTO `t_admin_user` (`id`, `_role_id`, `_nick_name`, `_user_name`, `_member_id`, `_password`, `_status`, `_parent_id`, `_full_path`, `_brand_name`, `_memo`, `_phone`, `_email`, `_google_secret`, `_google_enable`, `_login_error`, `_login_frozen`, `_login_succeed`, `_login_fail`, `_login_time`, `_login_ip`, `_register_time`, `_create_time`, `_update_time`, `_otp_key`, `_is_verify_otp_key`, `version`) VALUES
	(1, 1, '系统管理者', 'admin', NULL, 'eddb8819648a5553d5bc7b808d260221', 1, 0, '/1/', 'SYSTEMADMIN', NULL, NULL, NULL, NULL, 0, 0, NULL, 0, 0, '2023-01-05 16:00:26', '192.168.0.165', '2023-01-05 15:57:28', '2023-01-05 15:57:28', '2023-01-05 16:00:25', NULL, 0, 0),
	(2, 1, '管理员kang', 'kang', 1, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/2/', 'SYSTEMADMIN', '', '18400000000', '', NULL, 0, 0, '2023-01-05 16:02:03', 0, 0, '2023-01-05 16:02:03', '', '2023-01-05 16:02:03', '2023-01-05 16:02:02', '2023-01-05 16:02:02', NULL, 0, 0),
	(3, 1, '管理员ron', 'ronron', 2, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/3/', 'SYSTEMADMIN', '', '18400000001', '', NULL, 0, 0, '2023-01-05 16:02:29', 0, 0, '2023-01-05 16:16:45', '192.168.0.165', '2023-01-05 16:02:29', '2023-01-05 16:02:29', '2023-01-05 16:16:45', NULL, 0, 0),
	(4, 1, '管理员paige', 'paige', 3, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/4/', 'SYSTEMADMIN', '', '18400000002', '', NULL, 0, 0, '2023-01-05 16:02:54', 0, 0, '2023-01-05 16:02:54', '', '2023-01-05 16:02:54', '2023-01-05 16:02:53', '2023-01-05 16:02:53', NULL, 0, 0),
	(5, 1, '管理员tim', 'timtim', 4, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/5/', 'SYSTEMADMIN', '', '18400000003', '', NULL, 0, 0, '2023-01-05 16:03:19', 0, 0, '2023-01-05 16:03:19', '', '2023-01-05 16:03:19', '2023-01-05 16:03:18', '2023-01-05 16:03:18', NULL, 0, 0),
	(6, 1, '管理员phil', 'phil', 5, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/6/', 'SYSTEMADMIN', '', '18400000004', '', NULL, 0, 0, '2023-01-05 16:03:46', 0, 0, '2023-01-05 16:03:46', '', '2023-01-05 16:03:46', '2023-01-05 16:03:45', '2023-01-05 16:03:45', NULL, 0, 0),
	(7, 1, '管理员alex', 'alex', 6, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/7/', 'SYSTEMADMIN', '', '18400000005', '', NULL, 0, 0, '2023-01-05 16:04:11', 0, 0, '2023-01-05 16:04:11', '', '2023-01-05 16:04:11', '2023-01-05 16:04:10', '2023-01-05 16:04:10', NULL, 0, 0),
	(8, 1, '管理员tony', 'tony', 7, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/8/', 'SYSTEMADMIN', '', '18400000006', '', NULL, 0, 0, '2023-01-05 16:04:38', 0, 0, '2023-01-05 16:04:38', '', '2023-01-05 16:04:38', '2023-01-05 16:04:38', '2023-01-05 16:04:38', NULL, 0, 0),
	(9, 1, '管理员sharon', 'sharon', 8, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/9/', 'SYSTEMADMIN', '', '18400000007', '', NULL, 0, 0, '2023-01-05 16:05:35', 0, 0, '2023-01-05 16:05:35', '', '2023-01-05 16:05:35', '2023-01-05 16:05:34', '2023-01-05 16:05:34', NULL, 0, 0),
	(10, 1, '管理员benny', 'benny', 9, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/10/', 'SYSTEMADMIN', '', '18400000008', '', NULL, 0, 0, '2023-01-05 16:05:59', 0, 0, '2023-01-05 16:05:59', '', '2023-01-05 16:05:59', '2023-01-05 16:05:59', '2023-01-05 16:05:59', NULL, 0, 0),
	(11, 1, '管理员aiden', 'aiden', 10, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/11/', 'SYSTEMADMIN', '', '18400000009', '', NULL, 0, 0, '2023-01-05 16:06:23', 0, 0, '2023-01-05 16:06:23', '', '2023-01-05 16:06:23', '2023-01-05 16:06:22', '2023-01-05 16:06:22', NULL, 0, 0),
	(12, 1, '管理员lance', 'lance', 11, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/12/', 'SYSTEMADMIN', '', '18400000010', '', NULL, 0, 0, '2023-01-05 16:06:48', 0, 0, '2023-01-05 16:06:48', '', '2023-01-05 16:06:48', '2023-01-05 16:06:47', '2023-01-05 16:06:47', NULL, 0, 0),
	(13, 1, '管理员ken', 'kenken', 12, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/13/', 'SYSTEMADMIN', '', '18400000011', '', NULL, 0, 0, '2023-01-05 16:07:14', 0, 0, '2023-01-05 16:07:14', '', '2023-01-05 16:07:14', '2023-01-05 16:07:13', '2023-01-05 16:07:13', NULL, 0, 0),
	(14, 1, '管理员willie', 'willie', 13, 'eddb8819648a5553d5bc7b808d260221', 1, 1, '/1/14/', 'SYSTEMADMIN', '', '18400000012', '', NULL, 0, 0, '2023-01-05 16:07:35', 0, 0, '2023-01-05 16:07:35', '', '2023-01-05 16:07:35', '2023-01-05 16:07:35', '2023-01-05 16:07:35', NULL, 0, 0),
	(15, 1, 'test123', 'test123', 16, 'eddb8819648a5553d5bc7b808d260221', 1, 9, '/1/9/15/', 'SYSTEMADMIN', '', '18400099890', '', NULL, 0, 0, '2023-01-05 16:13:58', 0, 0, '2023-01-05 16:13:58', '', '2023-01-05 16:13:58', '2023-01-05 16:13:58', '2023-01-05 16:13:58', NULL, 0, 0);
/*!40000 ALTER TABLE `t_admin_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
