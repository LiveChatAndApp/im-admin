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

-- 傾印  資料表 im.t_chatroom_blacklist 結構
CREATE TABLE IF NOT EXISTS `t_chatroom_blacklist` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_cid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '聊天室ID',
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IM会员ID',
  `_member_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会员ID',
  `_status` tinyint NOT NULL COMMENT '0，取消拉黑；1，禁言；2，禁止进入',
  `_expired_time` datetime DEFAULT NULL COMMENT '当拉黑或禁止进入时，生效的时间，0为不限时',
  `_create_time` datetime DEFAULT NULL COMMENT '黑名单创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`_cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在傾印表格  im.t_chatroom_blacklist 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_chatroom_blacklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_chatroom_blacklist` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
