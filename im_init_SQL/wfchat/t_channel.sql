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

-- 傾印  資料表 wfchat.t_channel 結構
CREATE TABLE IF NOT EXISTS `t_channel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_cid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_portrait` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_status` int NOT NULL DEFAULT '0',
  `_desc` text COLLATE utf8mb4_unicode_ci,
  `_secret` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_callback` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_extra` text COLLATE utf8mb4_unicode_ci,
  `_automatic` tinyint NOT NULL DEFAULT '0',
  `_dt` bigint NOT NULL,
  `_menu` blob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_cid_index` (`_cid` DESC),
  KEY `channel_name_index` (`_name` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_channel 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_channel` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
