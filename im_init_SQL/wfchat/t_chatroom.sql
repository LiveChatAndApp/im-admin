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

-- 傾印  資料表 wfchat.t_chatroom 結構
CREATE TABLE IF NOT EXISTS `t_chatroom` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_cid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_portrait` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `_state` tinyint NOT NULL DEFAULT '0',
  `_desc` text COLLATE utf8mb4_unicode_ci,
  `_extra` text COLLATE utf8mb4_unicode_ci,
  `_dt` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `chatroom_cid_index` (`_cid` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_chatroom 的資料：~3 rows (近似值)
/*!40000 ALTER TABLE `t_chatroom` DISABLE KEYS */;
INSERT INTO `t_chatroom` (`id`, `_cid`, `_title`, `_portrait`, `_state`, `_desc`, `_extra`, `_dt`) VALUES
	(1, 'chatroom1', '得意聊天室1', '', 0, '得意测试聊天室1，用来演示聊天室功能', NULL, 1),
	(2, 'chatroom2', '得意聊天室2', '', 0, '得意测试聊天室2，用来演示聊天室功能', NULL, 1),
	(3, 'chatroom3', '得意聊天室3', '', 0, '得意测试聊天室3，用来演示聊天室功能', NULL, 1);
/*!40000 ALTER TABLE `t_chatroom` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
