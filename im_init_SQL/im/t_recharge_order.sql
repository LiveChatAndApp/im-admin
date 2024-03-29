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

-- 傾印  資料表 im.t_recharge_order 結構
CREATE TABLE IF NOT EXISTS `t_recharge_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_order_code` varchar(50) NOT NULL COMMENT '订单编号',
  `_user_id` bigint NOT NULL COMMENT '用户ID',
  `_method` tinyint NOT NULL COMMENT '充值方式 1:银行卡,2:微信,3:支付宝',
  `_amount` decimal(20,8) NOT NULL COMMENT '充值金额',
  `_currency` varchar(10) NOT NULL COMMENT '币种',
  `_channel_id` bigint NOT NULL DEFAULT '0' COMMENT '充值渠道ID',
  `_pay_image` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '付款截图',
  `_status` tinyint NOT NULL COMMENT '状态 0:訂單成立, 1:待审查,2:已完成,3:已拒绝',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `_updater_id` bigint DEFAULT NULL COMMENT '修改者ID',
  `_updater_role` tinyint DEFAULT NULL COMMENT '修改者角色 1: 系统管理者,2: 会员',
  `_update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key` (`_order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='充值订单';

-- 正在傾印表格  im.t_recharge_order 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_recharge_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_recharge_order` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
