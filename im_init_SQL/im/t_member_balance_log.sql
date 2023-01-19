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

-- 傾印  資料表 im.t_member_balance_log 結構
CREATE TABLE IF NOT EXISTS `t_member_balance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_user_id` bigint NOT NULL COMMENT '用户ID',
  `_currency` varchar(10) NOT NULL COMMENT '币种',
  `_type` tinyint NOT NULL COMMENT '类型 1:手動充值,2:手動提取, 3充值, 4:提現',
  `_amount` decimal(20,8) DEFAULT '0.00000000' COMMENT '交易金额',
  `_before_balance` decimal(20,8) DEFAULT '0.00000000' COMMENT '交易前馀额',
  `_after_balance` decimal(20,8) DEFAULT '0.00000000' COMMENT '交易后馀额',
  `_before_freeze` decimal(20,8) DEFAULT '0.00000000' COMMENT '交易前冻结金额',
  `_after_freeze` decimal(20,8) DEFAULT '0.00000000' COMMENT '交易后冻结金额',
  `_memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='会员馀额日志';

-- 正在傾印表格  im.t_member_balance_log 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_member_balance_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_member_balance_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
