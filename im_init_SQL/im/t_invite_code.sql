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

-- 傾印  資料表 im.t_invite_code 結構
CREATE TABLE IF NOT EXISTS `t_invite_code` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_invite_code` varchar(48) NOT NULL COMMENT '邀请码',
  `_friends_default_type` tinyint DEFAULT '1' COMMENT '预设好友模式 1: 所有, 2: 轮询',
  `_status` tinyint DEFAULT '1' COMMENT '状态 0: 停用, 1: 使用中\n',
  `_memo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '后台备注',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `_creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `_updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者',
  `_creator_role` int NOT NULL COMMENT '创建者角色 1: 系统管理者, 2: 会员',
  `_updater_role` int NOT NULL COMMENT '修改者角色 1: 系统管理者, 2: 会员',
  `_note` varchar(255) DEFAULT NULL,
  `_add_time` datetime(6) DEFAULT NULL,
  `_member_source_id` bigint DEFAULT NULL,
  `_member_target_id` bigint DEFAULT NULL,
  `_verify` int DEFAULT NULL,
  `_verify_text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`_invite_code` DESC) USING BTREE,
  KEY `ix_001` (`id`,`_invite_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='邀请码';

-- 正在傾印表格  im.t_invite_code 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_invite_code` DISABLE KEYS */;
INSERT INTO `t_invite_code` (`id`, `_invite_code`, `_friends_default_type`, `_status`, `_memo`, `_create_time`, `_update_time`, `_creator`, `_updater`, `_creator_role`, `_updater_role`, `_note`, `_add_time`, `_member_source_id`, `_member_target_id`, `_verify`, `_verify_text`) VALUES
	(1, '000000', 1, 1, NULL, '2023-01-05 16:08:43', '2023-01-05 16:08:43', 'admin', 'admin', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `t_invite_code` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
