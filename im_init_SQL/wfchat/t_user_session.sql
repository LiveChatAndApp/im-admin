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

-- 傾印  資料表 wfchat.t_user_session 結構
CREATE TABLE IF NOT EXISTS `t_user_session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_cid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_token` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `_voip_token` varchar(240) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_secret` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `_db_secret` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `_platform` tinyint DEFAULT '0',
  `_push_type` tinyint DEFAULT '0',
  `_package_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_device_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_device_version` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_phone_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_language` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_carrier_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_dt` bigint NOT NULL,
  `_deleted` tinyint DEFAULT '0',
  `_user_type` tinyint DEFAULT '0' COMMENT '0, normal user; 1, robot; 2, device;',
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_uid_cid_index` (`_cid`,`_uid`),
  KEY `session_token_index` (`_token`),
  KEY `session_uid_index` (`_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_user_session 的資料：~1,757 rows (近似值)
/*!40000 ALTER TABLE `t_user_session` DISABLE KEYS */;
INSERT INTO `t_user_session` (`id`, `_uid`, `_cid`, `_token`, `_voip_token`, `_secret`, `_db_secret`, `_platform`, `_push_type`, `_package_name`, `_device_name`, `_device_version`, `_phone_name`, `_language`, `_carrier_name`, `_dt`, `_deleted`, `_user_type`) VALUES
	(1, '4ygqmws2k', 'f8b37378-1ad6-40a5-b7ee-567c1eb427841669344187038', NULL, '', 'b3b942c3-2f91-446e-8335-8dcbdcaf1b4e', '5b341c10-4b10-433c-a92c-fc9a45b104aa', 2, 0, 'cn.jyintgroup.chat', 'Xiaomi', '9', 'MIX 2', 'zh', '', 1672906508483, 0, 0);
/*!40000 ALTER TABLE `t_user_session` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
