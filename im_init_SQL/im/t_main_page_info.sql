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

-- 傾印  資料表 im.t_main_page_info 結構
CREATE TABLE IF NOT EXISTS `t_main_page_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_active_member_count` int NOT NULL DEFAULT '0' COMMENT '活跃用户数',
  `_add_member_count` int NOT NULL DEFAULT '0' COMMENT '新增用户数',
  `_add_group_count` int NOT NULL DEFAULT '0' COMMENT '新建群个数',
  `_message_count` bigint NOT NULL DEFAULT '0' COMMENT '发送消息数',
  `_top10_active_member` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Top10活跃用户数',
  `_top10_active_group` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Top10活跃群组',
  `_member_grand_total_count` bigint NOT NULL COMMENT '当前系统总用户数',
  `_group_grand_total_count` bigint NOT NULL COMMENT '当前系统群总数',
  `_create_time` date NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_001` (`_create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='首页资讯';

-- 正在傾印表格  im.t_main_page_info 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_main_page_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_main_page_info` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
