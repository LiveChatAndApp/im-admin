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

-- 傾印  資料表 im.t_member_operate_log 結構
CREATE TABLE IF NOT EXISTS `t_member_operate_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `_uid` varchar(12) NOT NULL COMMENT '用戶编号(t_member._uid)',
  `_memo` text NOT NULL COMMENT '备注内容(Json格式)',
  `_auth_id` bigint NOT NULL COMMENT '请求方法(AppOperateLogEnum)',
  `_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_creator_level` int NOT NULL DEFAULT '0' COMMENT '创建者Level',
  `_creator` varchar(30) NOT NULL COMMENT '创建者帐号',
  `_creator_ip` varchar(15) NOT NULL COMMENT '创建者IP',
  `_creator_location` varchar(15) NOT NULL COMMENT '创建者位置',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_uid` (`_uid`) USING BTREE,
  KEY `index_auth_id` (`_auth_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='用户操作日誌';

-- 正在傾印表格  im.t_member_operate_log 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_member_operate_log` DISABLE KEYS */;
INSERT INTO `t_member_operate_log` (`id`, `_uid`, `_memo`, `_auth_id`, `_create_time`, `_creator_level`, `_creator`, `_creator_ip`, `_creator_location`) VALUES
	(1, '4ygqmws2k', '{"before":[{"key":"用户账号","val":"B3362346430363365"},{"key":"操作设备","val":"MOBILE"}],"after":[]}', 1000, '2023-01-05 16:15:08', 0, 'B3362346430363365', '192.168.0.162', '内网IP');
/*!40000 ALTER TABLE `t_member_operate_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
