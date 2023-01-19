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

-- 傾印  資料表 im.t_admin_role 結構
CREATE TABLE IF NOT EXISTS `t_admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_name` varchar(30) NOT NULL COMMENT '角色名称',
  `_level` int NOT NULL DEFAULT '2' COMMENT '级别 1:超级管理员, 2:普通管理员',
  `_brand_name` varchar(500) DEFAULT NULL COMMENT '品牌',
  `_memo` varchar(50) DEFAULT NULL COMMENT '备注',
  `_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COMMENT='管理者角色列表';

-- 正在傾印表格  im.t_admin_role 的資料：~3 rows (近似值)
/*!40000 ALTER TABLE `t_admin_role` DISABLE KEYS */;
INSERT INTO `t_admin_role` (`id`, `_name`, `_level`, `_brand_name`, `_memo`, `_create_time`, `_update_time`) VALUES
	(1, '管理者_技術', 1, 'SYSTEMADMIN', '系统预设', '2022-10-21 15:01:51', '2022-10-21 15:01:51');
/*!40000 ALTER TABLE `t_admin_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
