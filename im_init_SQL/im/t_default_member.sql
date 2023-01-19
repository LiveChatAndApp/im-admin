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

-- 傾印  資料表 im.t_default_member 結構
CREATE TABLE IF NOT EXISTS `t_default_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_default_type` tinyint NOT NULL DEFAULT '1' COMMENT '预设类型: 好友:1, 群:2',
  `_member_id` bigint DEFAULT NULL COMMENT '会员ID',
  `_group_id` bigint DEFAULT NULL COMMENT '群ID',
  `_welcome_text` varchar(1000) DEFAULT NULL COMMENT '欢迎词',
  `_type` tinyint NOT NULL DEFAULT '1' COMMENT '类型: 1:所有新注册用户, 2:使用邀请码注册用户',
  `_invite_code_id` bigint DEFAULT NULL COMMENT '邀请码ID 0为全部新加入者',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_creator` varchar(50) DEFAULT NULL COMMENT '创建者',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `_updater` varchar(50) DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`),
  KEY `FKi1im4aranshi95osw82k6xhf8` (`_member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='预设好友/群设置';

-- 正在傾印表格  im.t_default_member 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_default_member` DISABLE KEYS */;
INSERT INTO `t_default_member` (`id`, `_default_type`, `_member_id`, `_group_id`, `_welcome_text`, `_type`, `_invite_code_id`, `_create_time`, `_creator`, `_update_time`, `_updater`) VALUES
	(1, 1, 14, NULL, '您好，我是霓儿～欢迎您来到火热的聊天频道，喜欢妹妹任您挑选', 2, 1, '2023-01-05 16:11:40', 'admin', '2023-01-05 16:12:48', 'admin'),
	(2, 1, 15, NULL, '您好，我是卡拉～欢迎您来到火热的聊天频道，喜欢妹妹任您挑选', 2, 1, '2023-01-05 16:12:05', 'admin', '2023-01-05 16:12:40', 'admin'),
	(3, 2, NULL, 1, NULL, 1, 0, '2023-01-05 16:13:46', 'admin', '2023-01-05 16:13:46', 'admin');
/*!40000 ALTER TABLE `t_default_member` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
