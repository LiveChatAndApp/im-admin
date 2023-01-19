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

-- 傾印  資料表 wfchat.t_group_member 結構
CREATE TABLE IF NOT EXISTS `t_group_member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_gid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_mid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_alias` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_type` tinyint DEFAULT '0' COMMENT '0普通成员；1，管理员；2，群主，与Owner相同',
  `_dt` bigint NOT NULL,
  `_create_dt` bigint DEFAULT '0',
  `_extra` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_gid_mid_index` (`_gid`,`_mid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='group member create time';

-- 正在傾印表格  wfchat.t_group_member 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_group_member` DISABLE KEYS */;
INSERT INTO `t_group_member` (`id`, `_gid`, `_mid`, `_alias`, `_type`, `_dt`, `_create_dt`, `_extra`) VALUES
	(1, 'qygqmws2k', 'gygqmws2k', '', 2, 1672906407262, 1672906407262, ''),
	(3, 'qygqmws2k', 'jygqmws2k', '', 0, 1672906438045, 1672906438045, ''),
	(4, 'qygqmws2k', 'nygqmws2k', '管理员kang', 0, 1672906573384, 1672906573384, ''),
	(5, 'qygqmws2k', '9ygqmws2k', '管理员paige', 0, 1672906573385, 1672906573385, ''),
	(6, 'qygqmws2k', '2ygqmws2k', '管理员tim', 0, 1672906573386, 1672906573386, ''),
	(7, 'qygqmws2k', 'vygqmws2k', '管理员phil', 0, 1672906573387, 1672906573387, ''),
	(8, 'qygqmws2k', 'oygqmws2k', '管理员alex', 0, 1672906573388, 1672906573388, ''),
	(9, 'qygqmws2k', 'hygqmws2k', '管理员tony', 0, 1672906573389, 1672906573389, ''),
	(10, 'qygqmws2k', 'aygqmws2k', '管理员sharon', 0, 1672906573390, 1672906573390, ''),
	(11, 'qygqmws2k', '3ygqmws2k', '管理员benny', 0, 1672906573391, 1672906573391, ''),
	(12, 'qygqmws2k', 'wygqmws2k', '管理员aiden', 0, 1672906573392, 1672906573392, ''),
	(13, 'qygqmws2k', 'pygqmws2k', '管理员lance', 0, 1672906573393, 1672906573393, ''),
	(14, 'qygqmws2k', 'iygqmws2k', '管理员ken', 0, 1672906573394, 1672906573394, ''),
	(15, 'qygqmws2k', 'bygqmws2k', '管理员willie', 0, 1672906573395, 1672906573395, ''),
	(16, 'qygqmws2k', '4ygqmws2k', '恋爱狙击手-霓儿', 0, 1672906573396, 1672906573396, ''),
	(17, 'qygqmws2k', 'xygqmws2k', '恋爱狙击手-卡拉', 0, 1672906573397, 1672906573397, '');
/*!40000 ALTER TABLE `t_group_member` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
