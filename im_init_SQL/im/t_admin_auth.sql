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

-- 傾印  資料表 im.t_admin_auth 結構
CREATE TABLE IF NOT EXISTS `t_admin_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `_parent_id` bigint DEFAULT NULL COMMENT '父層ID',
  `_code` varchar(255) NOT NULL DEFAULT '' COMMENT '权限代码',
  `_name` varchar(255) NOT NULL DEFAULT '' COMMENT '权限名称',
  `_type` int NOT NULL DEFAULT '1000' COMMENT '类型',
  `_api` varchar(100) DEFAULT NULL COMMENT 'api Url',
  `_isLog` tinyint DEFAULT '0' COMMENT '是否寫log,0-不寫入,1-寫入',
  `_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `_version` int NOT NULL DEFAULT '1' COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key` (`_parent_id`,`_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb3 COMMENT='管理者权限列表';

-- 正在傾印表格  im.t_admin_auth 的資料：~105 rows (近似值)
/*!40000 ALTER TABLE `t_admin_auth` DISABLE KEYS */;
INSERT INTO `t_admin_auth` (`id`, `_parent_id`, `_code`, `_name`, `_type`, `_api`, `_isLog`, `_order`, `_version`) VALUES
	(1, -1, 'MAIN_PAGE', '首页', 1000, '/admin/mainPage/info', 0, 0, 1),
	(2, -1, 'MEMBER_MANAGEMENT', '用户管理', 1000, NULL, 0, 0, 1),
	(3, 2, 'MEMBER_LIST', '用户列表', 1000, NULL, 0, 0, 1),
	(4, 3, 'MEMBER_PAGE', '用户列表#查询', 1000, '/admin/member/page', 0, 0, 1),
	(5, 3, 'MEMBER_INSERT', '用户列表#新增', 1000, '/admin/member/add', 1, 0, 1),
	(6, 3, 'MEMBER_INSERT_BATCH', '用户列表#批量新增', 1000, '/admin/member/addBatch', 0, 0, 1),
	(7, 3, 'MEMBER_DETAIL', '用户列表#用户明细', 1000, '/admin/member/{id}', 0, 0, 1),
	(8, 3, 'MEMBER_EDIT', '用户列表#编辑', 1000, '/admin/member/edit', 1, 0, 1),
	(9, 3, 'MEMBER_CLEAN_LOGIN_ERROR', '用户列表#清除登陆错误次数', 1000, '/admin/member/cleanLoginError/{id}', 1, 0, 1),
	(10, 3, 'MEMBER_MANAGER_LOGIN_BATCH', '用户列表#批量管理允许管理号登入', 1000, '/admin/member/blockManagerLogin', 1, 0, 1),
	(11, 3, 'MEMBER_MANAGER_ALLOW_GROUP_BATCH', '用户列表#批量管理允许建群', 1000, '/admin/member/blockCreateGroup', 1, 0, 1),
	(12, 3, 'MEMBER_MANUAL_RECHARGE_WITHDRAW', '用户列表#存提', 1000, '/admin/manual/rechargeWithdraw', 1, 0, 1),
	(13, 3, 'MEMBER_CHAT_MEMBER', '用户列表#聊天对象', 1000, '/admin/member/chat/{id}', 0, 0, 1),
	(14, 3, 'MEMBER_SENT_MESSAGE', '用户列表#发送消息', 1000, '/admin/member/sent/{id}', 1, 0, 1),
	(15, 3, 'MEMBER_INSERT_FRIEND', '用户列表#添加好友', 1000, '/admin/member/friend/add/{id}', 1, 0, 1),
	(16, 3, 'MEMBER_REGISTER_IM_MEMBER', '用户列表#向IM服务注册用户', 1000, '/admin/member/registerIMMember', 0, 0, 1),
	(17, 3, 'MEMBER_CLEAN_CHAT', '用户列表#清空聊天记录', 1000, '/admin/member/chat/clean/{id}/{friendId}', 1, 0, 1),
	(18, 3, 'MEMBER_DELETE_FRIEND', '用户列表#删除好友', 1000, '/admin/member/friend/delete/{id}', 1, 0, 1),
	(19, 2, 'INVITE_CODE_LIST', '邀请码', 1000, NULL, 0, 0, 1),
	(20, 19, 'INVITE_CODE_PAGE', '邀请码#查询', 1000, '/admin/inviteCode/page', 0, 0, 1),
	(21, 19, 'INVITE_CODE_INSERT', '邀请码#新增', 1000, '/admin/inviteCode/add', 1, 0, 1),
	(22, 19, 'INVITE_CODE_EDIT', '邀请码#编辑', 1000, '/admin/inviteCode/edit', 1, 0, 1),
	(23, 19, 'INVITE_CODE_DEFAULT_MEMBER', '邀请码#查询预设好友', 1000, '/admin/inviteCode/defaultMember/{id}', 0, 0, 1),
	(24, 19, 'INVITE_CODE_DEFAULT_GROUP', '邀请码#查询预设群', 1000, '/admin/inviteCode/defaultGroup/{id}', 0, 0, 1),
	(25, 2, 'DEFAULT_MEMBER', '默认好友', 1000, NULL, 0, 0, 1),
	(26, 25, 'DEFAULT_MEMBER_PAGE', '默认好友#查询', 1000, '/admin/defaultMember/page', 0, 0, 1),
	(27, 25, 'DEFAULT_MEMBER_INSERT', '默认好友#新增', 1000, '/admin/defaultMember/add', 1, 0, 1),
	(28, 25, 'DEFAULT_MEMBER_EDIT', '默认好友#编辑', 1000, '/admin/defaultMember/edit', 1, 0, 1),
	(29, 25, 'DEFAULT_MEMBER_ALL_MODE', '默认好友#全部模式', 1000, '/admin/defaultMember/type', 1, 0, 1),
	(30, 25, 'DEFAULT_MEMBER_DELETE', '默认好友#删除预设好友_群', 1000, '/admin/defaultMember/delete/{id}', 1, 0, 1),
	(31, 2, 'MEMBER_OPERATE_LOG', '用户操作日誌', 1000, NULL, 0, 0, 1),
	(32, 31, 'MEMBER_OPERATE_LOG_PAGE', '用户操作日誌#查询', 1000, '/admin/member/operateLog/page', 0, 0, 1),
	(33, 31, 'MEMBER_OPERATE_LOG_CSV', '用户操作日誌#报表', 1000, '/admin/member/operateLog/list/csv', 1, 0, 1),
	(34, -1, 'SENSITIVE_WORD_MANAGEMENT', '敏感词管理', 1000, NULL, 0, 0, 1),
	(35, 34, 'SENSITIVE_WORD_LIST', '敏感词列表', 1000, NULL, 0, 0, 1),
	(36, 35, 'SENSITIVE_WORD_PAGE', '敏感词#查询', 1000, '/admin/sensitiveWord/swPage', 0, 0, 1),
	(37, 35, 'SENSITIVE_WORD_CREATE', '敏感词#新增', 1000, '/admin/sensitiveWord/swCreate', 1, 0, 1),
	(38, 35, 'SENSITIVE_WORD_DELETE', '敏感词#删除', 1000, '/admin/sensitiveWord/swDelete', 1, 0, 1),
	(39, 34, 'SENSITIVE_WORD_HIT', '敏感词命中', 1000, NULL, 0, 0, 1),
	(40, 39, 'SENSITIVE_WORD_HIT_PAGE', '敏感词命中#查询', 1000, '/admin/sensitiveWord/swHitPage', 0, 0, 1),
	(41, 39, 'SENSITIVE_WORD_HIT_DELETE', '敏感词命中#删除', 1000, '/admin/sensitiveWord/swHitDelete', 1, 0, 1),
	(42, 39, 'SENSITIVE_WORD_HIT_DELETE_ALL', '敏感词命中#清空', 1000, '/admin/sensitiveWord/swHitDeleteAll', 1, 0, 1),
	(43, -1, 'GROUP_MANAGEMENT', '群组管理', 1000, NULL, 0, 0, 1),
	(44, 43, 'GROUP_LIST', '群组管理', 1000, NULL, 0, 0, 1),
	(45, 44, 'GROUP_PAGE', '群组管理#查询', 1000, '/admin/group/page', 0, 0, 1),
	(46, 44, 'GROUP_INSERT', '群组管理#新增', 1000, '/admin/group/add', 1, 0, 1),
	(47, 44, 'GROUP_EDIT', '群组管理#编辑', 1000, '/admin/group/edit', 1, 0, 1),
	(48, 44, 'GROUP_DISSOLVE', '群组管理#解散', 1000, '/admin/group/delete/{groupId}', 1, 0, 1),
	(49, 44, 'GROUP_MEMBER', '群组管理#查询成员', 1000, '/admin/group/member', 0, 0, 1),
	(50, 44, 'GROUP_OUTSIDE_MEMBER', '群组管理#查询可添加成员', 1000, '/admin/group/member/without', 0, 0, 1),
	(51, 44, 'GROUP_MEMBER_INSERT', '群组管理#添加成员', 1000, '/admin/group/member/add', 1, 0, 1),
	(52, 44, 'GROUP_MEMBER_EDIT', '群组管理#编辑成员身份', 1000, '/admin/group/member/edit', 1, 0, 1),
	(53, 44, 'GROUP_MEMBER_DELETE', '群组管理#删除成员', 1000, '/admin/group/member/delete', 1, 0, 1),
	(54, 44, 'GROUP_MUTE', '群组管理#禁言群组', 1000, '/admin/group/mute', 1, 0, 1),
	(55, 44, 'GROUP_PRIVATRE_CHAT', '群组管理#私聊群组', 1000, '/admin/group/privateChat', 1, 0, 1),
	(56, 44, 'GROUP_ADD_MANAGER', '群组管理#添加群管理身分', 1000, '/admin/group/member/manager/add', 0, 0, 1),
	(57, -1, 'MESSAGE_MANAGEMENT', '消息管理', 1000, NULL, 0, 0, 1),
	(58, 57, 'MESSAGE_LIST', '消息列表', 1000, NULL, 0, 0, 1),
	(59, 58, 'MESSAGE_PAGE', '消息列表#查询', 1000, '/admin/message/page', 0, 0, 1),
	(60, 58, 'MESSAGE_REVERT', '消息列表#撤回消息', 1000, '/admin/message/revert/{messageId}', 1, 0, 1),
	(61, 57, 'BROADCAST_MESSAGE', '广播消息', 1000, NULL, 0, 0, 1),
	(62, 57, 'GROUP_MESSAGE', '群发消息', 1000, NULL, 0, 0, 1),
	(63, -1, 'CHATROOM_MANAGEMENT', '聊天室管理', 1000, NULL, 0, 0, 1),
	(64, 63, 'CHATROOM_LIST', '聊天室列表', 1000, NULL, 0, 0, 1),
	(65, 64, 'CHATROOM_PAGE', '聊天室#查询', 1000, '/admin/chatroom/page', 0, 0, 1),
	(66, 64, 'CHATROOM_CREATE', '聊天室#创建', 1000, '/admin/chatroom/create', 1, 0, 1),
	(67, 64, 'CHATROOM_DESTROY', '聊天室#解散', 1000, '/admin/chatroom/destroy', 1, 0, 1),
	(68, 64, 'CHATROOM_ONLINE_MEMBER', '聊天室#在线用户', 1000, '/admin/chatroom/onlineMember', 0, 0, 1),
	(69, 64, 'CHATROOM_UPDATE_SORT', '聊天室#排序', 1000, '/admin/chatroom/updateSort', 1, 0, 1),
	(70, -1, 'REPORT_MANAGEMENT', '报表管理', 1000, NULL, 0, 0, 1),
	(71, 70, 'REPORT_LIST', '经营报表', 1000, NULL, 0, 0, 1),
	(72, 71, 'REPORT_OPERATE', '报表管理#经营报告', 1000, '/admin/report/operate', 0, 0, 1),
	(73, 71, 'REPORT_OPERATE_EXCEL', '报表管理#经营报告Excel', 1000, '/admin/report/operate/csv', 1, 0, 1),
	(74, -1, 'FINANCIAL_MANAGEMENT', '财务管理', 1000, NULL, 0, 0, 1),
	(75, 74, 'MEMBER_BALANCE_LOG', '资金明细', 1000, NULL, 0, 0, 1),
	(76, 75, 'MEMBER_BALANCE_LOG_PAGE', '资金明细#查询', 1000, '/admin/memberBalanceLog/page', 0, 0, 1),
	(77, 74, 'RECHARGE_ORDER', '充值订单', 1000, NULL, 0, 0, 1),
	(78, 77, 'RECHARGE_ORDER_PAGE', '充值订单#查询', 1000, '/admin/rechargeOrder/page', 0, 0, 1),
	(79, 77, 'RECHARGE_ORDER_AUDIT', '充值订单#审核', 1000, '/admin/rechargeOrder/audit', 0, 0, 1),
	(80, 74, 'WITHDRAW_ORDER', '提现订单', 1000, NULL, 0, 0, 1),
	(81, 80, 'WITHDRAW_ORDER_PAGE', '提现订单#查询', 1000, '/admin/withdrawOrder/page', 0, 0, 1),
	(82, 80, 'WITHDRAW_ORDER_AUDIT', '提现订单#审核', 1000, '/admin/withdrawOrder/audit', 0, 0, 1),
	(83, 74, 'RECHARGE_CHANNEL', '收款渠道', 1000, NULL, 0, 0, 1),
	(84, 83, 'RECHARGE_CHANNEL_PAGE', '收款渠道#查询', 1000, '/admin/rechargeChannel/page', 0, 0, 1),
	(85, 83, 'RECHARGE_CHANNEL_ADD', '收款渠道#添加', 1000, '/admin/rechargeChannel/add', 0, 0, 1),
	(86, 83, 'RECHARGE_CHANNEL_UPDATE', '收款渠道#編輯', 1000, '/admin/rechargeChannel/update', 0, 0, 1),
	(87, 83, 'RECHARGE_CHANNEL_DELETE', '收款渠道#刪除', 1000, '/admin/rechargeChannel/delete', 0, 0, 1),
	(88, -1, 'CMS_MANAGEMENT', '后台管理', 1000, NULL, 0, 0, 1),
	(89, 88, 'AUTH_LIST', '菜单列表', 1000, NULL, 0, 0, 1),
	(90, 89, 'AUTH_LIST', '菜单#权限查询', 1000, '/admin/auth/list', 0, 0, 1),
	(91, 88, 'ROLE_LIST', '角色列表', 1000, NULL, 0, 0, 1),
	(92, 91, 'ROLE_PAGE', '角色#查询', 1000, '/admin/role/page', 0, 0, 1),
	(93, 91, 'ROLE_ADD', '角色#新增', 1000, '/admin/role/add', 1, 0, 1),
	(94, 91, 'ROLE_UPDATE', '角色#更新', 1000, '/admin/role/update', 1, 0, 1),
	(95, 88, 'SYSTEM_OPERATE_LOG', '系统操作日誌', 1000, NULL, 0, 0, 1),
	(96, 95, 'SYSTEM_OPERATE_LOG_PAGE', '系统操作日誌#查询', 1000, '/admin/system/operateLog/page', 0, 0, 1),
	(97, 95, 'SYSTEM_OPERATE_LOG_CSV', '系统操作日誌#报表', 1000, '/admin/system/operateLog/list/csv', 1, 0, 1),
	(98, 88, 'ADMIN_LIST', '账号列表', 1000, NULL, 0, 0, 1),
	(99, 98, 'ADMIN_PAGE', '账号#查询', 1000, '/admin/user/page', 0, 0, 1),
	(100, 98, 'ADMIN_INSERT', '账号#新增', 1000, '/admin/user/insert', 1, 0, 1),
	(101, 98, 'ADMIN_UPDATE', '账号#更新', 1000, '/admin/user/update', 1, 0, 1),
	(102, 98, 'ADMIN_RESETPWD', '账号#重置密码', 1000, '/admin/user/resetPwd', 1, 0, 1),
	(103, 98, 'ADMIN_KICKOUT', '账号#踢出下线', 1000, '/admin/user/kickOut', 1, 0, 1),
	(104, 88, 'ADMIN_INFO', '账号#资讯', 1000, '/admin/info', 1, 0, 1),
	(105, -1, 'VERSION_GET', '资讯', 1000, '/version/get', 0, 0, 1);
/*!40000 ALTER TABLE `t_admin_auth` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
