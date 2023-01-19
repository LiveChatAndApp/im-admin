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

-- 傾印  資料表 im.t_sensitive_word_hit 結構
CREATE TABLE IF NOT EXISTS `t_sensitive_word_hit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `_sender_id` bigint DEFAULT NULL COMMENT '发送者id',
  `_sender` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '发送者帐号/GID',
  `_receiver_id` bigint DEFAULT NULL COMMENT '接收者id',
  `_receiver` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '接收者帐号/GID',
  `_chat_type` tinyint NOT NULL COMMENT '会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道',
  `_content` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '敏感词内容',
  `_creator` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '创建者帐号',
  `_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_sender_id` (`_sender_id`) USING BTREE,
  KEY `index_receiver_id` (`_receiver_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='敏感词命中';

-- 正在傾印表格  im.t_sensitive_word_hit 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_sensitive_word_hit` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_sensitive_word_hit` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
