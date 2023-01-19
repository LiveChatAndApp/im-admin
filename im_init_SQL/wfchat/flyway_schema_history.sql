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

-- 傾印  資料表 wfchat.flyway_schema_history 結構
CREATE TABLE IF NOT EXISTS `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 正在傾印表格  wfchat.flyway_schema_history 的資料：~52 rows (近似值)
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
	(1, '1', 'baseline', 'SQL', 'V1__baseline.sql', 0, 'root', '2023-01-05 15:56:36', 2, 1),
	(2, '2', 'create table', 'SQL', 'V2__create_table.sql', -1748263735, 'root', '2023-01-05 15:56:36', 338, 1),
	(3, '3', 'create sharding table', 'SQL', 'V3__create_sharding_table.sql', 478454046, 'root', '2023-01-05 15:56:39', 3000, 1),
	(4, '4', 'create default chatroom', 'SQL', 'V4__create_default_chatroom.sql', 1427169464, 'root', '2023-01-05 15:56:39', 5, 1),
	(5, '5', 'create default robot', 'SQL', 'V5__create_default_robot.sql', -230242622, 'root', '2023-01-05 15:56:39', 5, 1),
	(6, '6', 'add friend alias', 'SQL', 'V6__add_friend_alias.sql', 277839155, 'root', '2023-01-05 15:56:39', 11, 1),
	(7, '7', 'add createtime user group table', 'SQL', 'V7__add_createtime_user_group_table.sql', 1270184638, 'root', '2023-01-05 15:56:39', 27, 1),
	(8, '8', 'add content type in messages', 'SQL', 'V8__add_content_type_in_messages.sql', -1266320159, 'root', '2023-01-05 15:56:40', 413, 1),
	(9, '9', 'add sensitive messages', 'SQL', 'V9__add_sensitive_messages.sql', 166085793, 'root', '2023-01-05 15:56:40', 20, 1),
	(10, '10', 'create default admin', 'SQL', 'V10__create_default_admin.sql', -69343883, 'root', '2023-01-05 15:56:40', 2, 1),
	(11, '11', 'alter device token', 'SQL', 'V11__alter_device_token.sql', -1407485274, 'root', '2023-01-05 15:56:40', 11, 1),
	(12, '12', 'add group control columns', 'SQL', 'V12__add_group_control_columns.sql', -656226254, 'root', '2023-01-05 15:56:40', 45, 1),
	(13, '13', 'create session table', 'SQL', 'V13__create_session_table.sql', 1406726373, 'root', '2023-01-05 15:56:40', 16, 1),
	(14, '14', 'alter createtime user group table', 'SQL', 'V14__alter_createtime_user_group_table.sql', 435637097, 'root', '2023-01-05 15:56:40', 60, 1),
	(15, '15', 'alter session table time', 'SQL', 'V15__alter_session_table_time.sql', 1873904193, 'root', '2023-01-05 15:56:40', 59, 1),
	(16, '16', 'update message dt gmt8', 'SQL', 'V16__update_message_dt_gmt8.sql', 389210932, 'root', '2023-01-05 15:56:40', 25, 1),
	(17, '17', 'add default sensitive word', 'SQL', 'V17__add_default_sensitive_word.sql', 0, 'root', '2023-01-05 15:56:40', 1, 1),
	(18, '18', 'create settings table', 'SQL', 'V18__create_settings_table.sql', -1760241630, 'root', '2023-01-05 15:56:40', 14, 1),
	(19, '19', 'add id for sensitive word', 'SQL', 'V19__add_id_for_sensitive_word.sql', -357586, 'root', '2023-01-05 15:56:40', 13, 1),
	(20, '20', 'alter voip token length', 'SQL', 'V20__alter_voip_token_length.sql', -159336300, 'root', '2023-01-05 15:56:40', 9, 1),
	(21, '21', 'add friend blocked', 'SQL', 'V21__add_friend_blocked.sql', 1548092851, 'root', '2023-01-05 15:56:40', 10, 1),
	(22, '22', 'add user session token index', 'SQL', 'V22__add_user_session_token_index.sql', 1511335132, 'root', '2023-01-05 15:56:40', 14, 1),
	(23, '23', 'add friend request index', 'SQL', 'V23__add_friend_request_index.sql', -2114419374, 'root', '2023-01-05 15:56:40', 12, 1),
	(24, '24', 'add user session uid index', 'SQL', 'V24__add_user_session_uid_index.sql', 518259660, 'root', '2023-01-05 15:56:40', 13, 1),
	(25, '25', 'user session add deleted', 'SQL', 'V25__user_session_add_deleted.sql', 1991490712, 'root', '2023-01-05 15:56:40', 14, 1),
	(26, '26', 'user add deleted', 'SQL', 'V26__user_add_deleted.sql', 1479561521, 'root', '2023-01-05 15:56:40', 15, 1),
	(27, '27', 'refactor channel status', 'SQL', 'V27__refactor_channel_status.sql', -158214832, 'root', '2023-01-05 15:56:40', 3, 1),
	(28, '28', 'alter message add to column', 'SQL', 'V28__alter_message_add_to_column.sql', 627099953, 'root', '2023-01-05 15:56:41', 360, 1),
	(29, '29', 'add friend extra', 'SQL', 'V29__add_friend_extra.sql', 536611746, 'root', '2023-01-05 15:56:41', 10, 1),
	(30, '30', 'create chatroom blacklist manager', 'SQL', 'V30__create_chatroom_blacklist_manager.sql', -410666459, 'root', '2023-01-05 15:56:41', 30, 1),
	(31, '31', 'add user messages line', 'SQL', 'V31__add_user_messages_line.sql', 988840057, 'root', '2023-01-05 15:56:43', 2494, 1),
	(32, '32', 'create device table', 'SQL', 'V32__create_device_table.sql', 312090620, 'root', '2023-01-05 15:56:43', 36, 1),
	(33, '33', 'add session user type', 'SQL', 'V33__add_session_user_type.sql', 1247467376, 'root', '2023-01-05 15:56:43', 18, 1),
	(34, '34', 'create receipt table', 'SQL', 'V34__create_receipt_table.sql', 1434736584, 'root', '2023-01-05 15:56:43', 60, 1),
	(35, '35', 'add group member create dt', 'SQL', 'V35__add_group_member_create_dt.sql', 1563271901, 'root', '2023-01-05 15:56:43', 22, 1),
	(36, '36', 'add group member count history message', 'SQL', 'V36__add_group_member_count_history_message.sql', 256396909, 'root', '2023-01-05 15:56:44', 50, 1),
	(37, '37', 'alter setting column name', 'SQL', 'V37__alter_setting_column_name.sql', -1482729867, 'root', '2023-01-05 15:56:44', 29, 1),
	(38, '38', 'alter group searchable column', 'SQL', 'V38__alter_group_searchable_column.sql', -524664704, 'root', '2023-01-05 15:56:44', 25, 1),
	(39, '39', 'create files table', 'SQL', 'V39__create_files_table.sql', 1144437159, 'root', '2023-01-05 15:56:44', 22, 1),
	(40, '40', 'create file transfer user', 'SQL', 'V40__create_file_transfer_user.sql', -274219840, 'root', '2023-01-05 15:56:44', 2, 1),
	(41, '41', 'alter channel status column', 'SQL', 'V41__alter_channel_status_column.sql', -1588313643, 'root', '2023-01-05 15:56:44', 26, 1),
	(42, '42', 'add user messages mid index', 'SQL', 'V42__add_user_messages_mid_index.sql', 538501302, 'root', '2023-01-05 15:56:45', 1328, 1),
	(43, '43', 'add user messages conv info', 'SQL', 'V43__add_user_messages_conv_info.sql', -1504280825, 'root', '2023-01-05 15:56:51', 5825, 1),
	(44, '44', 'add group member friend request extra', 'SQL', 'V44__add_group_member_friend_request_extra.sql', 1581231416, 'root', '2023-01-05 15:56:51', 21, 1),
	(45, '45', 'add user messages cont type', 'SQL', 'V45__add_user_messages_cont_type.sql', 810327912, 'root', '2023-01-05 15:56:52', 1587, 1),
	(46, '46', 'utf8mb4 unicode ci to utf8mb4 bin', 'SQL', 'V46__utf8mb4_unicode_ci_to_utf8mb4_bin.sql', 627308598, 'root', '2023-01-05 15:57:02', 9140, 1),
	(47, '47', 'fix read report error', 'SQL', 'V47__fix_read_report_error.sql', 1370732853, 'root', '2023-01-05 15:57:02', 34, 1),
	(48, '48', 'alter user setting key column', 'SQL', 'V48__alter_user_setting_key_column.sql', 2042462886, 'root', '2023-01-05 15:57:02', 8, 1),
	(49, '49', 'create secret chat table', 'SQL', 'V49__create_secret_chat_table.sql', -924343879, 'root', '2023-01-05 15:57:02', 16, 1),
	(50, '50', 'add message table conversation index', 'SQL', 'V50__add_message_table_conversation_index.sql', -250787270, 'root', '2023-01-05 15:57:02', 400, 1),
	(51, '51', 'add channel listener table member index', 'SQL', 'V51__add_channel_listener_table_member_index.sql', -1291014306, 'root', '2023-01-05 15:57:02', 18, 1),
	(52, '52', 'add channel menu column', 'SQL', 'V52__add_channel_menu_column.sql', -536830492, 'root', '2023-01-05 15:57:02', 12, 1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
