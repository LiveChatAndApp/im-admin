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

-- 傾印  資料表 wfchat.t_group 結構
CREATE TABLE IF NOT EXISTS `t_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_gid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_portrait` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `_type` tinyint NOT NULL DEFAULT '0',
  `_extra` text COLLATE utf8mb4_unicode_ci,
  `_dt` bigint NOT NULL,
  `_member_count` int DEFAULT '0',
  `_member_dt` bigint NOT NULL,
  `_createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `_mute` tinyint NOT NULL DEFAULT '0',
  `_join_type` tinyint NOT NULL DEFAULT '0',
  `_private_chat` tinyint NOT NULL DEFAULT '0',
  `_searchable` int NOT NULL DEFAULT '0',
  `_history_message` tinyint NOT NULL DEFAULT '0',
  `_max_member_count` int NOT NULL DEFAULT '2000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_gid_index` (`_gid` DESC),
  KEY `group_name_index` (`_name` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='max group member count';

-- 正在傾印表格  wfchat.t_group 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_group` DISABLE KEYS */;
INSERT INTO `t_group` (`id`, `_gid`, `_name`, `_portrait`, `_owner`, `_type`, `_extra`, `_dt`, `_member_count`, `_member_dt`, `_createTime`, `_mute`, `_join_type`, `_private_chat`, `_searchable`, `_history_message`, `_max_member_count`) VALUES
	(1, 'qygqmws2k', '恋爱告白版', 'http://rmz8p2cii.sabkt.gdipper.com/group/avatar/GROUP_AVATAR_2023010516132627451798.jpg', 'gygqmws2k', 2, '{"creator":"admin","creatorRole":1,"groupType":2}', 1672906573394, 16, 1672906407077, '2023-01-05 16:13:27', 1, 0, 1, 0, 1, 2000);
/*!40000 ALTER TABLE `t_group` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
