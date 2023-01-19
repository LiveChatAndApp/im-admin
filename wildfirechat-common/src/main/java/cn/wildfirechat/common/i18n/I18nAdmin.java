package cn.wildfirechat.common.i18n;

/**
 * 管理者代码
 */
public interface I18nAdmin {

	// ------------------------------------------管理者认证------------------------------------------

	//数据库操作失败
	String DB_FAIL = "db.fail";
	// ------------------------------------------管理者认证------------------------------------------



	// ------------------------------------------管理者认证------------------------------------------

	// 管理者不存在
	String ADMIN_USER_NOT_EXIST = "admin.user.not.exist";
	// 用户已存在
	String ADMIN_USER_IS_EXIST = "admin.user.is.exist";

	// ------------------------------------------OTP认证------------------------------------------

	//此账号已绑定过OTP
	String ADMIN_USER_BINDING_OTP = "admin.user.binding.otp";
	//请先绑定OTP
	String ADMIN_USER_NOT_BINDING_OTP = "admin.user.not.binding.otp";
	//OTP验证码错误
	String ADMIN_USER_VERIFY_OTP_FAILURE = "admin.user.verify.otp.failure";
	//此账号已绑定OTP，请输入OTP验证码登录
	String ADMIN_USER_OTP_IS_REQUIRED = "admin.user.otp.is.required";

	// ------------------------------------------会员认证------------------------------------------

	// 用户不存在
	String MEMBER_NOT_EXIST = "admin.member.not.exist";
	// 用户已存在
	String MEMBER_IS_EXIST = "admin.member.is.exist";

	// 用户账号错误
	String MEMBER_ACCOUNT_ERROR = "admin.member.account.error";

	// 会员电话已存在
	String MEMBER_PHONE_IS_EXIST = "admin.member.phone.is.exist";

	// 会员帐号已存在
	String MEMBER_USERNAME_IS_EXIST = "admin.member.username.is.exist";
	// 会员电话已存在
	String MEMBER_PASSWORD_FORMAT_ERROR = "admin.member.password.format.error";
	// 会员电话已存在
	String MEMBER_TRADE_PASSWORD_FORMAT_ERROR = "admin.member.trade_password.format.error";
	// 头像只支持jpg, png
	String MEMBER_AVATAR_FILE_TYPE_ERROR = "admin.member.avatar.file.format.error";

	/** 暱稱*/
	// 不能为空白
	String MEMBER_NICKNAME_NOT_BE_BLANK = "admin.member.nickname.not.be.blank";
	// 头尾不能空白
	String MEMBER_NICKNAME_HEADER_AND_TAIL_NOT_BE_BLANK = "admin.member.nickname.header.and.tail.not.be.blank";
	// 不可连续输入空白键
	String MEMBER_NICKNAME_NOT_ALLOW_CONTINUE_BLANK = "admin.member.nickname.not.allow.continue.blank";
	// 不能包含表情符号
	String MEMBER_NICKNAME_NOT_CONTAIN_EMOJI = "admin.member.nickname.not.contain.emoji";
	// ---------------------------------------------群---------------------------------------------

	// 群不存在
	String GROUP_NOT_EXIST = "admin.group.not.exist";
	// 群ID错误
	String GROUP_ID_ERROR = "admin.group.id.error";
	// 群主新增大於一位
	String GROUP_MEMBER_Large_ONE = "admin.group.member.largeOne";
	// 群组成员错误
	String GROUP_MEMBER_ERROR = "admin.group.member.error";
	// 建群失敗
	String GROUP_CREATE_ERROR = "admin.group.create.error";

	// 群组成员不得删除群主
	String GROUP_MEMBER_MUST_NOT_BE_DELETED_REMOVE_MANAGE_ERROR = "admin.group.member.must.not.be.deleted.remove.manage.error";

	// 只有会员才能成为管理员
	String GROUP_MEMBER_TO_BE_AN_MANAGE_ERROR = "admin.group.member.to.be.an.manmage.error";
	// -------------------------------------------邀請碼-------------------------------------------
	// 邀请码不存在
	String INVITE_CODE_NOT_EXIST = "admin.invite_code.not.exist";
	// 邀请码已存在
	String INVITE_CODE_EXIST = "admin.invite_code.exist";

	// -------------------------------------------预设好友/群-------------------------------------------
	// 预设好友/群不存在
	String DEFAULT_MEMBER_NOT_EXIST = "admin.default_member.not.exist";

	// 预设好友/群已存在
	String DEFAULT_MEMBER_EXIST = "admin.default_member.exist";

	// ---------------------------------------------提现订单----------------------------------------

	// 提现订单不存在
	String WITHDRAW_ORDER_NOT_EXIST = "admin.withdraw.order.not.exist";

	// 提现订单状态不支持
	String WITHDRAW_ORDER_STATUS_NOT_SUPPORT = "admin.withdraw.order.status.not.support";

	// ---------------------------------------------充值订单----------------------------------------
	// 充值订单不存在
	String RECHARGE_ORDER_NOT_EXIST = "admin.recharge.order.not.exist";

	// 充值订单状态不支持
	String RECHARGE_ORDER_STATUS_NOT_SUPPORT = "admin.recharge.order.status.not.support";
	// ---------------------------------------------资产----------------------------------------

	// 用户资产不存在
	String MEMBER_ASSETS_NOT_EXIST = "admin.member.assets.not.exist";

	// 用户资产不足
	String MEMBER_ASSETS_INSUFFICIENT = "admin.member.assets.insufficient";

	// ---------------------------------------------消息----------------------------------------

	// 消息不存在
	String MESSAGE_NOT_EXIST = "admin.message.not.exist";

	// ---------------------------------------------敏感詞----------------------------------------

	// 敏感詞重複
	String SENSITIVE_WORD_REPEAT = "admin.sensitive.word.repeat";

	String SENSITIVE_WORD_EXIST = "admin.sensitive.word.exist";

	// ---------------------------------------------好友----------------------------------------

	// 好友已存在
	String FRIEND_EXIST = "admin.friend.exist";

	// 好友不存在
	String FRIEND_NOT_EXIST = "admin.friend.not.exist";

	// 好友待驗證
	String FRIEND_PENDING = "admin.friend.pending";

	// 好友对象不得为自己
	String FRIEND_TARGET_SELF = "admin.friend.target.self";

	// ---------------------------------------------角色----------------------------------------

	// 角色权限为空
	String ROLE_AUTH_LIST_IS_EMPTY = "admin.role.authList.is.empty";

	// ---------------------------------------------聊天室----------------------------------------

	// 创聊天室失败
	String CHATROOM_CREATE_ERROR = "admin.chatroom.create.error";

	// 聊天室排序重複
	String CHATROOM_SORT_REPEAT_ERROR = "admin.chatroom.sort.repeat.error";
}
