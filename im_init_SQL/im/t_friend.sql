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

-- 傾印  資料表 im.t_friend 結構
CREATE TABLE IF NOT EXISTS `t_friend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '朋友流水号',
  `_member_source_id` bigint NOT NULL COMMENT '邀请者ID',
  `_member_target_id` bigint NOT NULL COMMENT '接受者ID',
  `_verify` tinyint NOT NULL COMMENT '验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除\n',
  `_verify_text` varchar(50) DEFAULT NULL COMMENT '验证讯息',
  `_request_receiver` bigint DEFAULT NULL COMMENT '好友邀请接收者ID',
  `_hello_text` varchar(255) DEFAULT NULL COMMENT '打招呼讯息',
  `_source_blacked` tinyint DEFAULT '1' COMMENT '邀请者黑名单 0:正常,1:拉黑',
  `_target_blacked` tinyint DEFAULT '1' COMMENT '接受者黑名单 0:正常,1:拉黑',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `_add_time` datetime DEFAULT NULL COMMENT '成为好友时间',
  `_request_receuver` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`_member_source_id`,`_member_target_id`) USING BTREE,
  UNIQUE KEY `UKe67iea7jkcrsia2houfj9f4gt` (`_member_source_id`,`_member_target_id`),
  UNIQUE KEY `UKq7sgptj91vmsav8b6px3nojo3` (`_member_source_id`,`_member_target_id`),
  CONSTRAINT `FKgk51fn98hjsfnomqot3jk3qng` FOREIGN KEY (`_member_source_id`) REFERENCES `t_member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='朋友';

-- 正在傾印表格  im.t_friend 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_friend` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
