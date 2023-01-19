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

-- 傾印  資料表 im.t_group_member 結構
CREATE TABLE IF NOT EXISTS `t_group_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '群成员流水',
  `_member_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会员id',
  `_group_id` bigint NOT NULL COMMENT '群id',
  `_member_type` tinyint NOT NULL DEFAULT '1' COMMENT '成员类型 1: 成员, 2: 管理员, 3: 群主',
  `_verify` tinyint NOT NULL DEFAULT '0' COMMENT '验证 0: 待验证, 1: 成功, 2: 失败',
  `_verify_text` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '验证消息',
  `_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '建立时间',
  `_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `_gid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`_group_id`,`_member_id`) USING BTREE,
  KEY `ix_001` (`_group_id`,`_member_type`) USING BTREE,
  CONSTRAINT `FKgn4vsccsljpgyl7vdwvoj1uip` FOREIGN KEY (`_group_id`) REFERENCES `t_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='群成员';

-- 正在傾印表格  im.t_group_member 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_group_member` DISABLE KEYS */;
INSERT INTO `t_group_member` (`id`, `_member_id`, `_group_id`, `_member_type`, `_verify`, `_verify_text`, `_create_time`, `_update_time`, `_gid`) VALUES
	(1, '2', 1, 3, 2, NULL, '2023-01-05 16:13:27', NULL, NULL),
	(3, '16', 1, 1, 2, NULL, '2023-01-05 16:13:58', NULL, NULL),
	(4, '1', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(5, '3', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(6, '4', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(7, '5', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(8, '6', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(9, '7', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(10, '8', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(11, '9', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(12, '10', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(13, '11', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(14, '12', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(15, '13', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(16, '14', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL),
	(17, '15', 1, 1, 2, NULL, '2023-01-05 16:16:13', NULL, NULL);
/*!40000 ALTER TABLE `t_group_member` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
