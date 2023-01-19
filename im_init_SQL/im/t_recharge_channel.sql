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

-- 傾印  資料表 im.t_recharge_channel 結構
CREATE TABLE IF NOT EXISTS `t_recharge_channel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_name` varchar(50) NOT NULL COMMENT '银行卡名称',
  `_payment_method` tinyint NOT NULL COMMENT '收款方式 1:银行卡,2:微信,3:支付宝',
  `_info` varchar(1000) NOT NULL COMMENT '收款方式资讯',
  `_qrcode_image` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'QRCode路径',
  `_status` tinyint NOT NULL COMMENT '状态 0:停用,1:启用',
  `_memo` varchar(100) DEFAULT NULL COMMENT '备注',
  `_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='充值渠道';

-- 正在傾印表格  im.t_recharge_channel 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_recharge_channel` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_recharge_channel` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
