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

-- 傾印  資料表 wfchat.t_user_messages_0 結構
CREATE TABLE IF NOT EXISTS `t_user_messages_0` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_mid` bigint NOT NULL,
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_seq` bigint NOT NULL,
  `_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `_line` int NOT NULL DEFAULT '0',
  `_type` tinyint NOT NULL DEFAULT '0',
  `_target` varchar(129) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_directing` tinyint(1) NOT NULL DEFAULT '0',
  `_cont_type` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `message_seq_uid_index` (`_uid` DESC,`_seq` DESC),
  KEY `message_mid_line_uid_index` (`_uid` DESC,`_line` DESC,`_mid` DESC),
  KEY `user_messages0_mid_index` (`_mid`),
  KEY `user_messages_conv_index` (`_uid`,`_type`,`_target`,`_line`,`_mid` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_user_messages_0 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_user_messages_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_user_messages_0` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
