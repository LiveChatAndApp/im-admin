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

-- 傾印  資料表 im.t_group 結構
CREATE TABLE IF NOT EXISTS `t_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_gid` varchar(50) NOT NULL COMMENT '群组id',
  `_name` varchar(100) NOT NULL COMMENT '群名称(群信息)',
  `_manager_id` bigint NOT NULL DEFAULT '0' COMMENT '群主',
  `_member_count_limit` int NOT NULL DEFAULT '200' COMMENT '群人数上限',
  `_group_image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '群头像图片',
  `_mute_type` tinyint NOT NULL DEFAULT '0' COMMENT '禁言 0:正常 1:禁言普通成员 2:禁言整个群',
  `_enter_auth_type` tinyint NOT NULL DEFAULT '1' COMMENT '入群验证类型 1:无需验证 2:管理员验证 3:不允许入群验证',
  `_invite_permission` tinyint NOT NULL DEFAULT '1' COMMENT '邀请权限(谁可以邀请他人入群) 1:管理员 2:所有人',
  `_invite_auth` tinyint NOT NULL DEFAULT '1' COMMENT '被邀请人身份验证 0:不需要同意 1:需要同意',
  `_modify_permission` tinyint NOT NULL DEFAULT '1' COMMENT '群资料修改权限 1:管理员 2:所有人',
  `_private_chat` tinyint NOT NULL COMMENT '私聊 0: 禁止, 1: 正常',
  `_bulletin_title` varchar(100) DEFAULT NULL COMMENT '群公告标题',
  `_bulletin_content` varchar(500) DEFAULT NULL COMMENT '群公告内容',
  `_status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 1:正常,2:解散',
  `_group_type` tinyint NOT NULL COMMENT '群组类型 1:一般,2:广播',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `_creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者uid',
  `_updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改者uid',
  `_creator_role` int NOT NULL COMMENT '创建者角色 1: 系统管理者, 2: 会员',
  `_updater_role` int NOT NULL COMMENT '修改者角色 1: 系统管理者, 2: 会员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique` (`_gid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='群列表';

-- 正在傾印表格  im.t_group 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_group` DISABLE KEYS */;
INSERT INTO `t_group` (`id`, `_gid`, `_name`, `_manager_id`, `_member_count_limit`, `_group_image`, `_mute_type`, `_enter_auth_type`, `_invite_permission`, `_invite_auth`, `_modify_permission`, `_private_chat`, `_bulletin_title`, `_bulletin_content`, `_status`, `_group_type`, `_create_time`, `_update_time`, `_creator`, `_updater`, `_creator_role`, `_updater_role`) VALUES
	(1, 'qygqmws2k', '恋爱告白版', 2, 2000, '{{domain}}/group/avatar/GROUP_AVATAR_2023010516132627451798.jpg', 2, 1, 0, 0, 2, 0, NULL, NULL, 1, 2, '2023-01-05 16:13:27', '2023-01-05 16:13:27', 'admin', 'admin', 1, 1);
/*!40000 ALTER TABLE `t_group` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
