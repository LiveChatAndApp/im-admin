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

-- 傾印  資料表 im.t_member_password 結構
CREATE TABLE IF NOT EXISTS `t_member_password` (
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户UID',
  `last_try_time` bigint NOT NULL COMMENT '尝试登入时间',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `reset_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '重置码',
  `reset_code_time` bigint NOT NULL COMMENT '发送重置码时间',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '加盐',
  `try_count` int NOT NULL DEFAULT '0' COMMENT '登入(失败)次数',
  `is_register` tinyint NOT NULL DEFAULT '0' COMMENT '是否为注册用',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户密码';

-- 正在傾印表格  im.t_member_password 的資料：10 rows
/*!40000 ALTER TABLE `t_member_password` DISABLE KEYS */;
INSERT INTO `t_member_password` (`user_id`, `last_try_time`, `password`, `reset_code`, `reset_code_time`, `salt`, `try_count`, `is_register`) VALUES
	('nygqmws2k', 0, 'wQotAIWWvcPpXnrHf7tiYc0hEyo=', NULL, 0, 'c4444f54-3322-4189-b6fc-7fc97563d50d', 0, 0),
	('gygqmws2k', 0, 'IpmO62wiuEBbG8hUj4q5tAYvAUo=', NULL, 0, '22193734-b382-4eae-ba18-e905243b730e', 0, 0),
	('9ygqmws2k', 0, 'DAbRPCB7C9cEnh5TUKg0LA+lFqQ=', NULL, 0, 'fe36826d-119b-4609-a294-fba72232a6e2', 0, 0),
	('2ygqmws2k', 0, '3As8lfdqvDxsPLEGvortZdpksn0=', NULL, 0, 'ace69a33-590f-4583-b57e-b4c6acf4873d', 0, 0),
	('vygqmws2k', 0, 'w9fGTKOEVg+nrCIUt7u3iwJgcuM=', NULL, 0, 'b4b52343-2f19-491f-affc-8bcdd1022dca', 0, 0),
	('oygqmws2k', 0, '+S9CRAh7wGiYG0+o06S/9gAqDBk=', NULL, 0, '1dbc9f80-b3cc-41ba-b9f7-67096d9978ef', 0, 0),
	('hygqmws2k', 0, 'wslmGVbHI5Vbk2G6Whj730LoHGQ=', NULL, 0, '8aa1e6eb-d4d8-4e88-9a94-0506989fb595', 0, 0),
	('aygqmws2k', 0, 'OByEsHUVr0ua5nus40fHfZ0PiPo=', NULL, 0, '652afb9f-353a-4dec-9137-23f416cdef9a', 0, 0),
	('3ygqmws2k', 0, 'tQn137JdR7++H6+0QnGCcgnxHCo=', NULL, 0, '676e12ce-d584-495e-8d08-95c5456eaa6c', 0, 0),
	('wygqmws2k', 0, 'kImirXbfNQe9YbNNE3jz+SxJKBM=', NULL, 0, 'c98ba376-c769-4a54-8da0-4e844020f0b7', 0, 0),
	('pygqmws2k', 0, 'z1XN6Q4Lq2Oz0g3ebZiTPJ1+nP4=', NULL, 0, 'b4e8d73e-f78d-47b1-afc5-1880496234f5', 0, 0),
	('iygqmws2k', 0, '4fnEhzUqnhYuGN9e05SPtymrYdY=', NULL, 0, 'eeab179c-0a91-4271-93c0-4a071efe8302', 0, 0),
	('bygqmws2k', 0, 'O2ABMWo6lHt+aJL6AUq9/38r2vw=', NULL, 0, '5921e3f3-6bc2-40db-8e84-8b19ab029215', 0, 0),
	('4ygqmws2k', 0, '2JFn8MtHk1iHpdT6yOIfKW3/V7w=', NULL, 0, '6d39053e-2ad7-4025-a6a0-b8dfc4361cbe', 0, 0),
	('xygqmws2k', 0, 'z+LKKogOxF9rdvXwUWPNvEmAVA8=', NULL, 0, '30b58820-64f5-4a09-b825-cbd6b0df64c3', 0, 0),
	('jygqmws2k', 0, 'P89c8ZWXC/KobIaq9lANZ+vVbUY=', NULL, 0, '301b1e54-126c-4da5-a7e4-6d404a1af3ce', 0, 0);
/*!40000 ALTER TABLE `t_member_password` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
