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

-- 傾印  資料表 im.t_customer_question 結構
CREATE TABLE IF NOT EXISTS `t_customer_question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
  `_sender_id` bigint NOT NULL COMMENT '发送者id',
  `_operator_id` bigint DEFAULT NULL COMMENT '经办者id',
  `_question_type` tinyint NOT NULL COMMENT '问题类型 1:注册,2:忘记密码,3:聊天室,4:通讯录,5:安全,6:钱包,7:其它',
  `_status` tinyint NOT NULL COMMENT '状态 1:待谘询,2:谘询中,3:已解决,4:超时',
  `_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `_memo` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客服问题列表';

-- 正在傾印表格  im.t_customer_question 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `t_customer_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_customer_question` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
