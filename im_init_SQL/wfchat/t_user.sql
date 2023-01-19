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

-- 傾印  資料表 wfchat.t_user 結構
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_display_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_gender` int NOT NULL DEFAULT '0',
  `_portrait` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_mobile` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_address` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_company` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_social` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_passwd_md5` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_salt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_extra` text COLLATE utf8mb4_unicode_ci,
  `_type` tinyint DEFAULT '0',
  `_dt` bigint NOT NULL,
  `_createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `_deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_uid_index` (`_uid`),
  UNIQUE KEY `user_name_index` (`_name`),
  KEY `user_display_name_index` (`_display_name`),
  KEY `user_mobile_index` (`_mobile`),
  KEY `user_email_index` (`_email`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_user 的資料：~1,776 rows (近似值)
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `_uid`, `_name`, `_display_name`, `_gender`, `_portrait`, `_mobile`, `_email`, `_address`, `_company`, `_social`, `_passwd_md5`, `_salt`, `_extra`, `_type`, `_dt`, `_createTime`, `_deleted`) VALUES
	(1, 'PrettyRobot', 'PrettyRobot', '完美秘书', 0, 'http://cdn2.wildfirechat.cn/robot.png', '', '', '', '', '', '', '', NULL, 1, 1, '2023-01-05 15:57:51', 0),
	(2, 'admin', 'admin', '系統通知', 0, '{{domain}}/avatar/admin.png', '', '', '', '', '', '', '', NULL, 3, 1, '2023-01-05 15:57:51', 0),
	(3, 'wfc_file_transfer', 'wfc_file_transfer', '文件传输助手', 0, 'https://static.wildfirechat.cn/wfc_file_transfer.png', '', '', '', '', '', '', '', NULL, 1, 1, '2023-01-05 15:57:51', 0),
	(4, 'nygqmws2k', 'kang', '管理员kang', 0, '', '18400000000', '', '', '', '', 'PUfo8fcfxYXwOVQ/QojrEg==', '', '', 0, 1672905722420, '2023-01-05 16:02:02', 0),
	(5, 'gygqmws2k', 'ronron', '管理员ron', 1, '{{domain}}/avatar/AVATAR_2023010516083044859650.jpg', '18400000001', '', '', '', '', '3PpwcbF9YINEYUB1Z5guVg==', '', '', 0, 1672906618882, '2023-01-05 16:02:29', 0),
	(6, '9ygqmws2k', 'paige', '管理员paige', 0, '', '18400000002', '', '', '', '', 'sl2TLlBCFHQ0FDSgwfxhQA==', '', '', 0, 1672905773554, '2023-01-05 16:02:53', 0),
	(7, '2ygqmws2k', 'timtim', '管理员tim', 0, '', '18400000003', '', '', '', '', '3EHagOuiQqLA7GwD7auirg==', '', '', 0, 1672905798670, '2023-01-05 16:03:18', 0),
	(8, 'vygqmws2k', 'phil', '管理员phil', 0, '', '18400000004', '', '', '', '', 'ObDvC+JnYABvA4mcoIo7Cg==', '', '', 0, 1672905825940, '2023-01-05 16:03:45', 0),
	(9, 'oygqmws2k', 'alex', '管理员alex', 0, '', '18400000005', '', '', '', '', '4GuzhjDyuNkXU/xQL5ZZIw==', '', '', 0, 1672905850841, '2023-01-05 16:04:10', 0),
	(10, 'hygqmws2k', 'tony', '管理员tony', 0, '', '18400000006', '', '', '', '', '14mPVGguZ6BdKwjhqCkGZA==', '', '', 0, 1672905878215, '2023-01-05 16:04:38', 0),
	(11, 'aygqmws2k', 'sharon', '管理员sharon', 0, '', '18400000007', '', '', '', '', '7Pc6dAgv4mRXFrqtHtNxzQ==', '', '', 0, 1672905934891, '2023-01-05 16:05:34', 0),
	(12, '3ygqmws2k', 'benny', '管理员benny', 0, '', '18400000008', '', '', '', '', '0m+uLAby9EGJW70Q4Va+5A==', '', '', 0, 1672905959123, '2023-01-05 16:05:59', 0),
	(13, 'wygqmws2k', 'aiden', '管理员aiden', 0, '', '18400000009', '', '', '', '', 'Vq98ZZwl0iYSjrt+JrZQDQ==', '', '', 0, 1672905982510, '2023-01-05 16:06:22', 0),
	(14, 'pygqmws2k', 'lance', '管理员lance', 0, '', '18400000010', '', '', '', '', '02Muvsc3fnqKIBhYL42S4A==', '', '', 0, 1672906007526, '2023-01-05 16:06:47', 0),
	(15, 'iygqmws2k', 'kenken', '管理员ken', 0, '', '18400000011', '', '', '', '', 'o4z0iF3+8mf1Sa7pcPoabA==', '', '', 0, 1672906033948, '2023-01-05 16:07:13', 0),
	(16, 'bygqmws2k', 'willie', '管理员willie', 0, '', '18400000012', '', '', '', '', 'Ic2Hl0Ubucl/JcyBkOZXGg==', '', '', 0, 1672906055070, '2023-01-05 16:07:35', 0),
	(18, '4ygqmws2k', 'B3362346430363365', '恋爱狙击手-霓儿', 0, '{{domain}}/avatar/AVATAR_2023010516093551603867.jpg', '18410000000', '', '', '', '', '4s9px90GFi1SsEbfRC/1BA==', '', '', 0, 1672906176069, '2023-01-05 16:09:36', 0),
	(19, 'xygqmws2k', 'B3632303361336236', '恋爱狙击手-卡拉', 0, '{{domain}}/avatar/AVATAR_2023010516102965739110.jpg', '18410000001', '', '', '', '', 'EWKXtcTgGRWYx3eCA1cudQ==', '', '', 0, 1672906229744, '2023-01-05 16:10:29', 0),
	(20, 'jygqmws2k', 'test123', 'test123', 0, '', '18400099890', '', '', '', '', 'vzB8Zf7ezHHOVNvGxz0GFw==', '', '', 0, 1672906438024, '2023-01-05 16:13:58', 0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
