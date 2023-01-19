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

-- 傾印  資料表 wfchat.t_user_setting 結構
CREATE TABLE IF NOT EXISTS `t_user_setting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_scope` int NOT NULL,
  `_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `_value` varchar(4096) COLLATE utf8mb4_unicode_ci NOT NULL,
  `_dt` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_setting_index` (`_uid`,`_scope`,`_key`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在傾印表格  wfchat.t_user_setting 的資料：~3 rows (近似值)
/*!40000 ALTER TABLE `t_user_setting` DISABLE KEYS */;
INSERT INTO `t_user_setting` (`id`, `_uid`, `_scope`, `_key`, `_value`, `_dt`) VALUES
	(1, 'gygqmws2k', 6, 'qygqmws2k', '1', 1672906407269),
	(3, 'jygqmws2k', 6, 'qygqmws2k', '1', 1672906438054),
	(4, 'nygqmws2k', 6, 'qygqmws2k', '1', 1672906573404),
	(5, '9ygqmws2k', 6, 'qygqmws2k', '1', 1672906573407),
	(6, '2ygqmws2k', 6, 'qygqmws2k', '1', 1672906573410),
	(7, 'vygqmws2k', 6, 'qygqmws2k', '1', 1672906573414),
	(8, 'oygqmws2k', 6, 'qygqmws2k', '1', 1672906573416),
	(9, 'hygqmws2k', 6, 'qygqmws2k', '1', 1672906573418),
	(10, 'aygqmws2k', 6, 'qygqmws2k', '1', 1672906573422),
	(11, '3ygqmws2k', 6, 'qygqmws2k', '1', 1672906573425),
	(12, 'wygqmws2k', 6, 'qygqmws2k', '1', 1672906573427),
	(13, 'pygqmws2k', 6, 'qygqmws2k', '1', 1672906573429),
	(14, 'iygqmws2k', 6, 'qygqmws2k', '1', 1672906573431),
	(15, 'bygqmws2k', 6, 'qygqmws2k', '1', 1672906573433),
	(16, '4ygqmws2k', 6, 'qygqmws2k', '1', 1672906573434),
	(17, 'xygqmws2k', 6, 'qygqmws2k', '1', 1672906573436);
/*!40000 ALTER TABLE `t_user_setting` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
