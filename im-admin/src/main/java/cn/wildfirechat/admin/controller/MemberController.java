package cn.wildfirechat.admin.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import cn.wildfirechat.admin.common.utils.UserAgentUtils;
import cn.wildfirechat.admin.utils.EmojiUtils;
import cn.wildfirechat.admin.utils.TextUtils;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.po.MessagePO;
import cn.wildfirechat.common.utils.FileUtils;
import cn.wildfirechat.sdk.MessageAdmin;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.common.net.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.wildfirechat.admin.common.annotation.SwaggerSpec;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.FriendService;
import cn.wildfirechat.admin.service.MemberService;
import cn.wildfirechat.admin.service.MessageService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.i18n.I18nBase;
import cn.wildfirechat.common.model.bo.*;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.po.FriendPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.query.MessagePageQuery;
import cn.wildfirechat.common.model.vo.*;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.FileNameUtils;
import cn.wildfirechat.sdk.RelationAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/member")
@Api(tags = "会员管理")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private FriendService friendService;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	@GetMapping(value = "/page")
	@ApiOperation(value = "搜寻用户列表")
	public ResponseVO<PageVO<MemberVO>> page(MemberForm form, Page page) {
		try {
			log.info("page form: {}, page: {}", form, page);
			MemberQuery query = MemberQuery.builder().build();
			ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
			return ResponseVO.success(memberService.page(query, page));
		} catch (IllegalArgumentException e) {
			log.error("page form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("page form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping(value = "/add")
	@ApiOperation(value = "新增用户")
	public ResponseVO<?> add(HttpServletRequest request, MemberAddForm form) {
		try {
			log.info("add form: {}", form);
			checkNickNameFormat(form.getNickName());
			MemberAddBO bo = MemberAddBO.builder().build();
			ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
			bo.setAccountType(MemberAccountTypeEnum.ORDINARY.getValue());

			// 更新头像
			MultipartFile avatar = form.getAvatarFile();
			String urlPath = uploadFileUtils.uploadFile(avatar, FileUtils.AVATAR_PATH, FileNameUtils.AVATAR_PREFIX, Collections.singletonList(MediaType.ANY_IMAGE_TYPE));
			if (urlPath != null) {
				bo.setAvatarUrl(urlPath);
			}

			bo.setChannel(UserAgentUtils.getOs(request));

			memberService.add(bo);
			return ResponseVO.success();
		} catch (IllegalArgumentException e) {
			log.error("add form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("add form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping(value = "/addBatch")
	@ApiOperation(value = "批量新增用户")
	@SwaggerSpec("Deprecated")
	public ResponseVO<?> addBatch(HttpServletRequest request, MemberAddBatchForm form) {
		try {
			MemberBatchAddBO bo = MemberBatchAddBO.builder().build();
			ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
			bo.setAccountType(MemberAccountTypeEnum.ORDINARY);
			bo.setNickNameType(MemberNickNameTypeEnum.parse(form.getNickNameType()));
			bo.setAvatarType(MemberAvatarTypeEnum.parse(form.getAvatarType()));
			bo.setChannel(UserAgentUtils.getOs(request));
			memberService.addBatch(bo);
			return ResponseVO.success();
		} catch (IllegalArgumentException e) {
			log.error("add form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("add form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping(value = "/registerIMMember")
	@ApiOperation(value = "向IM服务注册用户")
	public ResponseVO<?> registerIMMember(@RequestParam("id") Long id, @RequestParam("loginPwd") String loginPwd) {
		try {
			MemberDTO memberDTO = checkMemberExist(id);
			Assert.isNull(memberDTO.getUid(), "IM服务已注册用户");
			boolean success = memberService.registerIMMember(id, loginPwd, memberDTO.getMemberName(),
					memberDTO.getNickName(), memberDTO.getPhone(), null);
			return ResponseVO.success(success);
		} catch (IllegalArgumentException e) {
			log.error("registerIMMember id: {}", id, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("registerIMMember id: {}", id, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@GetMapping(value = "/{id}")
	@ApiOperation(value = "用户明细")
	public ResponseVO<MemberDetailVO> detail(@PathVariable Long id) {
		MemberDTO memberDTO = checkMemberExist(id);
		MemberDetailVO memberDetailVO = new MemberDetailVO();
		ReflectionUtils.copyFields(memberDetailVO, memberDTO, ReflectionUtils.STRING_TRIM_TO_NULL);
		return ResponseVO.success(memberDetailVO);
	}

	@PostMapping(value = "/edit")
	@ApiOperation(value = "编辑用户")
	public ResponseVO<?> edit(MemberEditForm memberEditForm) {
		try {
			log.info("edit form: {}", memberEditForm);
			checkMemberExist(memberEditForm.getId());
			checkNickNameFormat(memberEditForm.getNickName());
			MemberPO memberPO = new MemberPO();
			ReflectionUtils.copyFields(memberPO, memberEditForm, ReflectionUtils.STRING_TRIM_TO_NULL);

			// 更新头像
			MultipartFile avatar = memberEditForm.getAvatarFile();
			String urlPath = uploadFileUtils.uploadFile(avatar, FileUtils.AVATAR_PATH, FileNameUtils.AVATAR_PREFIX, Collections.singletonList(MediaType.ANY_IMAGE_TYPE));
			if (urlPath != null) {
				memberPO.setAvatarUrl(urlPath);
			}

			memberService.update(memberPO, memberEditForm.getPassword());
			return ResponseVO.success();
		} catch (IllegalArgumentException e) {
			log.error("edit form: {}", memberEditForm, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("edit form: {}", memberEditForm, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping("/cleanLoginError/{id}")
	@ApiOperation(value = "清除登陆错误次数")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class)})
	public ResponseVO<?> cleanLoginError(@PathVariable Long id) {
		MemberDTO memberDTO = checkMemberExist(id);
		memberService.cleanLoginError(memberDTO);

		// 用户列表-清除登录错误次数 log'
		OperateLogList list = new OperateLogList();
		list.addLog("账号", memberDTO.getMemberName(), false);
		list.addDiffLog("清除登录错误次数", memberDTO.getLoginErrorCount(),
				checkMemberExist(id).getLoginErrorCount(), false);
		logService.addOperateLog("/admin/member/cleanLoginError/{id}", list);
		return ResponseVO.success();
	}

	@PostMapping("/blockManagerLogin")
	@ApiOperation(value = "批量管理允许管理号登入")
	@ApiImplicitParams({@ApiImplicitParam(name = "isBlock", value = "是否可以登入", dataTypeClass = Boolean.class)})
	public ResponseVO<?> manageManagerLogin(Boolean isBlock) {
		MemberPO memberPO = MemberPO.builder().accountType(MemberAccountTypeEnum.MANAGE.getValue()).loginEnable(isBlock)
				.build();
		memberService.batchUpdateLoginAndCreateGroup(memberPO);

		// 用户列表-批量管理允許管理员登录 log'
		OperateLogList list = new OperateLogList();
		boolean success = (Boolean.TRUE.equals(isBlock)) ?
				list.addLog("批量允许管理员登录", "操作成功", false) :
				list.addLog("批量禁止管理员登录", "操作成功", false);
		logService.addOperateLog("/admin/member/blockManagerLogin", list);
		return ResponseVO.success();
	}

	@PostMapping("/blockCreateGroup")
	@ApiOperation(value = "批量管理允许建群")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "accountType", value = "帐号类型 1:普通帐号, 2:管理号", dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "isBlock", value = "是否可以建群", dataTypeClass = Boolean.class)})
	public ResponseVO<?> manageCreateGroup(Integer accountType, Boolean isBlock) {
		MemberPO memberPO = MemberPO.builder().accountType(accountType).createGroupEnable(isBlock).build();
		memberService.batchUpdateLoginAndCreateGroup(memberPO);

		// 用户列表-批量管理允许建群 log'
		OperateLogList list = new OperateLogList();
		String operate = (Boolean.TRUE.equals(isBlock)) ? "允许" : "禁止";
		boolean success = (accountType.equals(MemberAccountTypeEnum.MANAGE.getValue())) ?
				list.addLog("批量" + operate + "管理员建群", "操作成功", false) :
				list.addLog("批量" + operate + "所有人建群", "操作成功", false);
		logService.addOperateLog("/admin/member/blockCreateGroup", list);
		return ResponseVO.success();
	}

	@GetMapping("/chat/{id}")
	@ApiOperation(value = "聊天对象列表")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class)})
	public ResponseVO<PageVO<MemberFriendVO>> chatList(@PathVariable("id") Long id, MemberFriendQueryForm form,
			Page page) {
		log.info("聊天对象列表 id: {}, form:{}, page:{}", id, form, page);
		checkMemberExist(id);
		MemberFriendQueryBO memberFriendQueryBO = new MemberFriendQueryBO();
		ReflectionUtils.copyFields(memberFriendQueryBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		memberFriendQueryBO.setMemberId(id);

		PageVO<MemberFriendVO> memberFriendVOPageVO = friendService.selectMemberFriend(memberFriendQueryBO, page);

		log.info("聊天对象列表 成功");
		return ResponseVO.success(memberFriendVOPageVO);
	}

	@GetMapping("/chat/{id}/{friendId}")
	@ApiOperation(value = "查看聊天讯息")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class),
			@ApiImplicitParam(name = "friendId", value = "好友用户流水号", dataTypeClass = Long.class)})
	public ResponseVO<PageVO<MessagePageVO>> chatMessage(@PathVariable("id") Long id,
			@PathVariable("friendId") Long friendId, Page page) {
		log.info("查看聊天讯息 id:{}, friendId:{}", id, friendId);
		Map<Long, MemberPO> memberPOMap = memberService.selectMemberByIds(Arrays.asList(id, friendId)).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));

		Assert.isTrue(memberPOMap.keySet().size() == 2, message.get(I18nAdmin.FRIEND_NOT_EXIST));

		List<FriendPO> friendPOList = friendService.relateExist(null, id, friendId);
		Assert.isTrue(friendPOList.size() > 0, message.get(I18nAdmin.FRIEND_NOT_EXIST));

		PageVO<MessagePageVO> messagePageVOPageVO = messageService
				.page(MessagePageQuery.builder().account1(memberPOMap.get(id).getMemberName())
						.account2(memberPOMap.get(friendId).getMemberName()).build(), page);

		log.info("查看聊天讯息 成功");
		return ResponseVO.success(messagePageVOPageVO);
	}

	@PostMapping("/chat/clean/{id}/{friendId}")
	@ApiOperation(value = "清空聊天记录")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class),
			@ApiImplicitParam(name = "friendId", value = "好友用户流水号", dataTypeClass = Long.class),
			@ApiImplicitParam(name = "count", value = "回撤条数(-1为全部，正整数为回撤条数)", dataTypeClass = Integer.class)})
	public ResponseVO<?> cleanChat(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId,
			Integer count) {
		log.info("清空聊天记录 id:{}, friendId: {}, count:{}", id, friendId, count);
		String username = SpringSecurityUtil.getSuffixUsername();

		Map<Long, MemberPO> memberPOMap = memberService.selectMemberByIds(Arrays.asList(id, friendId)).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));

		log.info("清空聊天记录 {}条", messageService.delete(id, friendId, username, count));
		

		// 用户列表-清空聊天记录 log
		OperateLogList list = new OperateLogList();
		list.addLog("账号", memberPOMap.get(id).getMemberName(), false);
		list.addLog("好友账号", memberPOMap.get(friendId).getMemberName(), false);
		switch (count) {
			case -1:
				list.addLog("清空数量", "清空所有", false);
				break;
			default:
				list.addLog("清空数量", count, false);
				if (count != 100) list.addLog("自定义撤回消息数量", count, false);
				break;
		}
		logService.addOperateLog("/admin/member/chat/clean/{id}/{friendId}", list);
		return ResponseVO.success();
	}

	@PostMapping("/sent/{id}")
	@ApiOperation("发送消息")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class),
			@ApiImplicitParam(name = "content", value = "文字内容", dataTypeClass = String.class)})
	public ResponseVO<?> sentMessage(@PathVariable("id") Long id, String content) {
		log.info("发送消息 id:{}, content:{}", id, content);
		MemberDTO memberDTO = checkMemberExist(id);
		Long adminId = SpringSecurityUtil.getUserId();
		String adminAccount = SpringSecurityUtil.getPrincipal();

		MessageCreateBO messageCreateBO = new MessageCreateBO();
		messageCreateBO.setSenderId(adminId);
		messageCreateBO.setSenderAccountName(adminAccount);
		messageCreateBO.setSenderRole(MessageSenderRoleEnum.ADMIN.getValue());
		messageCreateBO.setReceiverId(memberDTO.getId());
		messageCreateBO.setReceiverAccountName(memberDTO.getMemberName());
		messageCreateBO.setMessageType(MessageTypeEnum.TEXT.getValue());
		messageCreateBO.setChatType(MessageChatEnum.PRIVATE.getValue());
		messageCreateBO.setContent(content);

		boolean isSuccess = messageService.create(messageCreateBO);
		log.info("发送消息 id:{}, content:{}, Success:{}", id, content, isSuccess);
		return isSuccess ? ResponseVO.success() : ResponseVO.error(message.getMessage(I18nBase.SYSTEM_ERROR));
	}

	@PostMapping("/friend/add/{id}")
	@ApiOperation("添加好友")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "用户流水号", dataTypeClass = Long.class)})
	public ResponseVO<?> addFriend(HttpServletRequest request, @PathVariable("id") Long id, MemberAddFriendForm form) {
		log.info("添加好友 id:{}, form:{}", id, form);
		MemberPO targetMember = memberService.selectMemberByMemberNameOrPhone(form.getMember()).stream().findFirst()
				.orElse(null);
		Assert.notNull(targetMember, message.getMessage(I18nAdmin.MEMBER_NOT_EXIST));

		FriendPO friendPO = friendService.relateExist(null, id, targetMember.getId()).stream().findFirst().orElse(null);
		if (friendPO != null) {
			cn.wildfirechat.common.support.Assert.isFalse(friendPO.getVerify() == RelateVerifyEnum.PENDING.getValue(),
					message.get(I18nAdmin.FRIEND_PENDING));
			cn.wildfirechat.common.support.Assert.isFalse(friendPO.getVerify() == RelateVerifyEnum.VERIFY.getValue(),
					message.get(I18nAdmin.FRIEND_PENDING));
			cn.wildfirechat.common.support.Assert.isFalse(friendPO.getVerify() == RelateVerifyEnum.SUCCESS.getValue(),
					message.get(I18nAdmin.FRIEND_EXIST));
		}
		List<MemberPO> memberPOList = memberService.selectMemberByIds(Collections.singletonList(id));

		MemberPO selfMember = memberPOList.stream().findFirst().orElse(null);
		Assert.notNull(selfMember, message.get(I18nAdmin.MEMBER_NOT_EXIST));
		try {
			IMResult<Void> voidIMResult = null;
			// 若需要验证, 先送出好友邀请并存入后台DB。不需要验证, 则直接送入IM新增好友
			// 两者皆需要等待RabbitMQ Sync[UserFriendSync]资料
			if (form.getVerify() == RelateVerifyEnum.VERIFY.getValue()) {
				voidIMResult = RelationAdmin.sendFriendRequest(selfMember.getUid(), targetMember.getUid(), "", true);
				MemberJoinFriendBO memberJoinFriendBO = new MemberJoinFriendBO();
				ReflectionUtils.copyFields(memberJoinFriendBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
				memberJoinFriendBO.setSelfMemberId(selfMember.getId());
				memberJoinFriendBO.setMemberId(targetMember.getId());
				friendService.insert(memberJoinFriendBO);
			} else if (form.getVerify() == RelateVerifyEnum.SUCCESS.getValue()) {
				voidIMResult = RelationAdmin.setUserFriend(selfMember.getUid(), targetMember.getUid(), true, "");
			}
			// if (voidIMResult != null) {
			// if (voidIMResult.code == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			// return ResponseVO.success();
			// }
			// }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		log.info("[IM][新增好友]已送出至IM Server");
		// 用户列表-添加好友 log
		OperateLogList list = new OperateLogList();
		list.addLog("账号", selfMember.getMemberName(), false);
		list.addLog("好友手机号/用户账号", form.getMember(), false);
		if (form.getVerify() == RelateVerifyEnum.VERIFY.getValue()) {
			list.addLog("免验证通过", "需要对方验证通过", false);
			list.addLog("验证消息", form.getVerifyText(), false);
		} else {
			list.addLog("免验证通过", "免验证直接通过", false);
		}

		logService.addOperateLog("/admin/member/friend/add/{id}", list);
		return ResponseVO.success();
	}

	@PostMapping("/friend/delete/{id}")
	@ApiOperation("删除好友")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "好友流水号", dataTypeClass = Long.class)})
	public ResponseVO<?> deleteFriend(@PathVariable("id") Long id) {
		log.info("删除好友 id:{}", id);
		friendService.delete(id);
		log.info("删除好友 完成");
		return ResponseVO.success();
	}

	private MemberDTO checkMemberExist(Long id) {
		MemberQuery memberQuery = MemberQuery.builder().id(id).build();
		MemberDTO memberDTO = memberService.list(memberQuery).stream().findFirst().orElse(null);
		Assert.notNull(memberDTO, message.get(I18nAdmin.MEMBER_NOT_EXIST));
		return memberDTO;
	}

	public void checkNickNameFormat(String nickName) {
		if (StringUtils.isNotEmpty(nickName)) {
			Assert.isTrue(!StringUtils.isWhitespace(nickName), message.get(I18nAdmin.MEMBER_NICKNAME_NOT_BE_BLANK));
			Assert.isTrue(!(nickName.startsWith(" ") || nickName.endsWith(" ")), message.get(I18nAdmin.MEMBER_NICKNAME_HEADER_AND_TAIL_NOT_BE_BLANK));
			Assert.isTrue(!(nickName.contains("  ")), message.get(I18nAdmin.MEMBER_NICKNAME_NOT_ALLOW_CONTINUE_BLANK));
			Assert.isTrue(!EmojiUtils.containsEmoji(nickName), message.get(I18nAdmin.MEMBER_NICKNAME_NOT_CONTAIN_EMOJI));
		}
	}
}
