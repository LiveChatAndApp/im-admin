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

-- 傾印  資料表 im.t_system_operate_log 結構
CREATE TABLE IF NOT EXISTS `t_system_operate_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `_admin_id` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '管理员编号(t_admin_user.uid)',
  `_memo` text CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '备注内容(Json格式)',
  `_auth_id` bigint NOT NULL COMMENT '请求方法(t_admin_auth.id)',
  `_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `_creator_level` int NOT NULL DEFAULT '0' COMMENT '创建者Level',
  `_creator` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '创建者帐号',
  `_creator_ip` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '创建者IP',
  `_creator_location` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '创建者位置',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_admin_id` (`_admin_id`) USING BTREE,
  KEY `index_auth_id` (`_auth_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 COMMENT='系统操作日誌';

-- 正在傾印表格  im.t_system_operate_log 的資料：~11 rows (近似值)
/*!40000 ALTER TABLE `t_system_operate_log` DISABLE KEYS */;
INSERT INTO `t_system_operate_log` (`id`, `_admin_id`, `_memo`, `_auth_id`, `_create_time`, `_creator_level`, `_creator`, `_creator_ip`, `_creator_location`) VALUES
	(1, '1', '{"before":[{"key":"用户账号","val":"admin"}],"after":[]}', -100, '2023-01-05 16:00:25', 1, 'admin', '192.168.0.165', '内网IP'),
	(2, '1', '{"before":[{"key":"账号","val":"kang"},{"key":"手机号","val":"18400000000"},{"key":"昵称","val":"管理员kang"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:02:02', 1, 'admin', '192.168.0.165', '内网IP'),
	(3, '1', '{"before":[{"key":"账号","val":"ronron"},{"key":"手机号","val":"18400000001"},{"key":"昵称","val":"管理员ron"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:02:29', 1, 'admin', '192.168.0.165', '内网IP'),
	(4, '1', '{"before":[{"key":"账号","val":"paige"},{"key":"手机号","val":"18400000002"},{"key":"昵称","val":"管理员paige"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:02:53', 1, 'admin', '192.168.0.165', '内网IP'),
	(5, '1', '{"before":[{"key":"账号","val":"timtim"},{"key":"手机号","val":"18400000003"},{"key":"昵称","val":"管理员tim"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:03:18', 1, 'admin', '192.168.0.165', '内网IP'),
	(6, '1', '{"before":[{"key":"账号","val":"phil"},{"key":"手机号","val":"18400000004"},{"key":"昵称","val":"管理员phil"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:03:45', 1, 'admin', '192.168.0.165', '内网IP'),
	(7, '1', '{"before":[{"key":"账号","val":"alex"},{"key":"手机号","val":"18400000005"},{"key":"昵称","val":"管理员alex"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:04:10', 1, 'admin', '192.168.0.165', '内网IP'),
	(8, '1', '{"before":[{"key":"账号","val":"tony"},{"key":"手机号","val":"18400000006"},{"key":"昵称","val":"管理员tony"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:04:38', 1, 'admin', '192.168.0.165', '内网IP'),
	(9, '1', '{"before":[{"key":"账号","val":"sharon"},{"key":"手机号","val":"18400000007"},{"key":"昵称","val":"管理员sharon"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:05:34', 1, 'admin', '192.168.0.165', '内网IP'),
	(10, '1', '{"before":[{"key":"账号","val":"benny"},{"key":"手机号","val":"18400000008"},{"key":"昵称","val":"管理员benny"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:05:59', 1, 'admin', '192.168.0.165', '内网IP'),
	(11, '1', '{"before":[{"key":"账号","val":"aiden"},{"key":"手机号","val":"18400000009"},{"key":"昵称","val":"管理员aiden"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:06:22', 1, 'admin', '192.168.0.165', '内网IP'),
	(12, '1', '{"before":[{"key":"账号","val":"lance"},{"key":"手机号","val":"18400000010"},{"key":"昵称","val":"管理员lance"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:06:47', 1, 'admin', '192.168.0.165', '内网IP'),
	(13, '1', '{"before":[{"key":"账号","val":"kenken"},{"key":"手机号","val":"18400000011"},{"key":"昵称","val":"管理员ken"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:07:13', 1, 'admin', '192.168.0.165', '内网IP'),
	(14, '1', '{"before":[{"key":"账号","val":"willie"},{"key":"手机号","val":"18400000012"},{"key":"昵称","val":"管理员willie"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:07:35', 1, 'admin', '192.168.0.165', '内网IP'),
	(15, '1', '{"before":[{"key":"账号","val":"ronron"},{"key":"手机号","val":"18400000001"},{"key":"头像","val":"原头像"}],"after":[{"key":"头像","val":"新头像"}]}', 8, '2023-01-05 16:08:30', 1, 'admin', '192.168.0.165', '内网IP'),
	(16, '1', '{"before":[{"key":"邀请码","val":"000000"},{"key":"预设好友模式","val":"添加所有预设好友"},{"key":"状态","val":"使用中"}],"after":[]}', 21, '2023-01-05 16:08:43', 1, 'admin', '192.168.0.165', '内网IP'),
	(17, '1', '{"before":[{"key":"账号","val":"B3362346430363365"},{"key":"手机号","val":"18410000000"},{"key":"昵称","val":"恋爱狙击手-霓儿"},{"key":"性别","val":"女"}],"after":[]}', 5, '2023-01-05 16:09:36', 1, 'admin', '192.168.0.165', '内网IP'),
	(18, '1', '{"before":[{"key":"账号","val":"B3632303361336236"},{"key":"手机号","val":"18410000001"},{"key":"昵称","val":"恋爱狙击手-卡拉"},{"key":"性别","val":"女"}],"after":[]}', 5, '2023-01-05 16:10:29', 1, 'admin', '192.168.0.165', '内网IP'),
	(19, '1', '{"before":[{"key":"类型","val":"使用邀请码注册用户"},{"key":"邀请码","val":"000000"},{"key":"预设类型","val":"好友"},{"key":"用户账号/群ID","val":"B3362346430363365"},{"key":"欢迎词","val":"您好，霓儿 欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}],"after":[]}', 27, '2023-01-05 16:11:40', 1, 'admin', '192.168.0.165', '内网IP'),
	(20, '1', '{"before":[{"key":"类型","val":"使用邀请码注册用户"},{"key":"邀请码","val":"000000"},{"key":"预设类型","val":"好友"},{"key":"用户账号/群ID","val":"B3632303361336236"},{"key":"欢迎词","val":"您好，卡拉欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}],"after":[]}', 27, '2023-01-05 16:12:05', 1, 'admin', '192.168.0.165', '内网IP'),
	(21, '1', '{"before":[{"key":"邀请码","val":"000000"},{"key":"账号","val":"B3632303361336236"},{"key":"欢迎词","val":"您好，卡拉欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}],"after":[{"key":"欢迎词","val":"您好，我是卡拉～欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}]}', 28, '2023-01-05 16:12:40', 1, 'admin', '192.168.0.165', '内网IP'),
	(22, '1', '{"before":[{"key":"邀请码","val":"000000"},{"key":"账号","val":"B3362346430363365"},{"key":"欢迎词","val":"您好，霓儿 欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}],"after":[{"key":"欢迎词","val":"您好，我是霓儿～欢迎您来到火热的聊天频道，喜欢妹妹任您挑选"}]}', 28, '2023-01-05 16:12:48', 1, 'admin', '192.168.0.165', '内网IP'),
	(23, '1', '{"before":[{"key":"群组名称","val":"恋爱告白版"},{"key":"群组类型","val":"2"},{"key":"群主账号","val":"ronron"}],"after":[]}', 46, '2023-01-05 16:13:27', 1, 'admin', '192.168.0.165', '内网IP'),
	(24, '1', '{"before":[{"key":"类型","val":"所有新注册用户"},{"key":"预设类型","val":"群"},{"key":"用户账号/群ID","val":"qygqmws2k"}],"after":[]}', 27, '2023-01-05 16:13:46', 1, 'admin', '192.168.0.165', '内网IP'),
	(25, '9', '{"before":[{"key":"账号","val":"test123"},{"key":"手机号","val":"18400099890"},{"key":"昵称","val":"test123"},{"key":"性别","val":"保密"}],"after":[]}', 5, '2023-01-05 16:13:58', 1, 'sharon', '192.168.0.122', '内网IP'),
	(26, '1', '{"before":[{"key":"敏感词内容","val":"fuck"}],"after":[]}', 37, '2023-01-05 16:13:58', 1, 'admin', '192.168.0.165', '内网IP'),
	(27, '1', '{"before":[{"key":"群ID","val":"qygqmws2k"},{"key":"新增成员","val":"[paige, sharon, benny, lance, willie, B3632303361336236, timtim, aiden, phil, tony, B3362346430363365, alex, kenken, kang]"},{"key":"群组人数","val":"2"}],"after":[{"key":"群组人数","val":"16"}]}', 51, '2023-01-05 16:16:13', 1, 'admin', '192.168.0.165', '内网IP'),
	(28, '1', '{"before":[{"key":"用户账号","val":"admin"}],"after":[]}', -101, '2023-01-05 16:16:38', 1, 'admin', '192.168.0.165', '内网IP'),
	(29, '3', '{"before":[{"key":"用户账号","val":"ronron"}],"after":[]}', -100, '2023-01-05 16:16:45', 1, 'ronron', '192.168.0.165', '内网IP'),
	(30, '3', '{"before":[{"key":"账号","val":"ronron"},{"key":"手机号","val":"18400000001"},{"key":"密码","val":"********"}],"after":[{"key":"密码","val":"*********"}]}', 8, '2023-01-05 16:16:58', 1, 'ronron', '192.168.0.165', '内网IP');
/*!40000 ALTER TABLE `t_system_operate_log` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
