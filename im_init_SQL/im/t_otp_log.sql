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

-- 傾印  資料表 im.t_otp_log 結構
CREATE TABLE IF NOT EXISTS `t_otp_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '身份验证纪录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `secret_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '密钥',
  `totp_code` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '验证编码',
  `status` tinyint DEFAULT '1' COMMENT '状态 0:失败 1:成功',
  `action` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '行为',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新建时间',
  `creator` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '新建人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `t_otp_log_IDX1` (`user_id`,`secret_key`) USING BTREE,
  KEY `t_otp_log_IDX2` (`secret_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='身份验证纪录表';

-- 正在傾印表格  im.t_otp_log 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_otp_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_otp_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
