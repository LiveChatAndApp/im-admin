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

-- 傾印  資料表 im.t_chatroom 結構
CREATE TABLE IF NOT EXISTS `t_chatroom` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_cid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '聊天室ID',
  `_name` varchar(100) NOT NULL COMMENT '名称',
  `_sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `_image` varchar(100) DEFAULT NULL COMMENT '图挡路径',
  `_status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0:停用,1:启用',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `_chat_status` tinyint NOT NULL DEFAULT '1' COMMENT '发言状态(目前無實作功能) 0:停用,1:启用',
  `_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '聊天室详情描述',
  `_extra` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '附加信息,建议用json',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sort` (`_sort`),
  UNIQUE KEY `unique` (`_cid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天室列表';

-- 正在傾印表格  im.t_chatroom 的資料：~3 rows (近似值)
/*!40000 ALTER TABLE `t_chatroom` DISABLE KEYS */;
INSERT INTO `t_chatroom` (`id`, `_cid`, `_name`, `_sort`, `_image`, `_status`, `_create_time`, `_update_time`, `_chat_status`, `_desc`, `_extra`) VALUES
	(1, 'chatroom1', '得意聊天室1', 20, '', 0, '2023-01-05 15:57:28', '2023-01-05 15:57:28', 1, NULL, NULL),
	(2, 'chatroom2', '得意聊天室2', 30, '', 1, '2023-01-05 15:57:28', '2023-01-05 15:57:28', 1, NULL, NULL),
	(3, 'chatroom3', '得意聊天室3', 3, '', 0, '2023-01-05 15:57:28', '2023-01-05 15:57:28', 1, NULL, NULL);
/*!40000 ALTER TABLE `t_chatroom` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
