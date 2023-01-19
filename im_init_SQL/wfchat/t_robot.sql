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

-- 傾印  資料表 wfchat.t_robot 結構
CREATE TABLE IF NOT EXISTS `t_robot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `_secret` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_callback` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_state` tinyint DEFAULT '0',
  `_extra` text COLLATE utf8mb4_unicode_ci,
  `_dt` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `robot_uid_index` (`_uid`),
  KEY `robot_owner_index` (`_owner`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_robot 的資料：~2 rows (近似值)
/*!40000 ALTER TABLE `t_robot` DISABLE KEYS */;
INSERT INTO `t_robot` (`id`, `_uid`, `_owner`, `_secret`, `_callback`, `_state`, `_extra`, `_dt`) VALUES
	(1, 'PrettyRobot', 'PrettyRobot', '123456', 'http://127.0.0.1:8883/robot/recvmsg', 0, NULL, 1),
	(2, 'wfc_file_transfer', 'wfc_file_transfer', '', '', 0, NULL, 1);
/*!40000 ALTER TABLE `t_robot` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
