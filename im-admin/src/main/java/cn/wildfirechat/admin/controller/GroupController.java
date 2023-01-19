package cn.wildfirechat.admin.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.bo.*;
import cn.wildfirechat.common.utils.FileUtils;
import cn.wildfirechat.sdk.model.IMResult;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.common.net.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.GroupMemberService;
import cn.wildfirechat.admin.service.GroupService;
import cn.wildfirechat.admin.service.MemberService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.enums.GroupMemberTypeEnum;
import cn.wildfirechat.common.model.enums.RelateVerifyEnum;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.GroupMemberQuery;
import cn.wildfirechat.common.model.query.GroupPageQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.vo.GroupMemberPageVO;
import cn.wildfirechat.common.model.vo.GroupPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.FileNameUtils;
import cn.wildfirechat.common.utils.StringUtil;
import cn.wildfirechat.sdk.GroupAdmin;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/group")
@Api(tags = "群组管理")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupMemberService groupMemberService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	@GetMapping("/page")
	@ApiOperation(value = "群列表")
	public ResponseVO<PageVO<GroupPageVO>> page(GroupQueryForm groupQueryForm, Page page) {
		log.info("page form: {}, page: {}", groupQueryForm, page);
		GroupPageQuery groupPageQuery = new GroupPageQuery();
		ReflectionUtils.copyFields(groupPageQuery, groupQueryForm, ReflectionUtils.STRING_TRIM_TO_NULL);
		if (StringUtil.isNotEmpty(groupQueryForm.getGroupName())) {
			groupPageQuery.setName(groupQueryForm.getGroupName());
		}
		PageVO<GroupPageVO> groupPageVOPageVO = groupService.page(groupPageQuery, page);
		log.info("群列表查询成功");

		return ResponseVO.success(groupPageVOPageVO);
	}

	@PostMapping("/add")
	@ApiOperation("创建群组")
	public ResponseVO<?> create(GroupCreateForm form) throws Exception {
		log.info("创建群组: {}", form);
		String username = SpringSecurityUtil.getPrincipal();

		GroupAddBO groupAddBO = new GroupAddBO();
		ReflectionUtils.copyFields(groupAddBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		groupAddBO.setCreator(username);

		// 更新头像
		MultipartFile avatar = form.getAvatarFile();
		String urlPath = uploadFileUtils.uploadFile(avatar, FileUtils.GROUP_AVATAR_PATH, FileNameUtils.GROUP_AVATAR_PREFIX, Arrays.asList(MediaType.ANY_IMAGE_TYPE));
		if (urlPath != null) {
			groupAddBO.setGroupImage(urlPath);
		}

		Assert.isTrue(groupService.insert(groupAddBO), message.get(I18nAdmin.GROUP_CREATE_ERROR));
		return ResponseVO.success();
	}

	@PostMapping("/edit")
	@ApiOperation("编辑群组")
	public ResponseVO<?> edit(GroupEditForm form) {
		log.info("编辑群组: {}", form);
		String username = SpringSecurityUtil.getPrincipal();

		GroupUpdateBO groupUpdateBO = GroupUpdateBO.builder().build();
		ReflectionUtils.copyFields(groupUpdateBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		if (StringUtil.isNotEmpty(form.getGroupName())) {
			groupUpdateBO.setName(form.getGroupName());
		}

		// 更新头像
		MultipartFile avatar = form.getAvatarFile();
		String urlPath = uploadFileUtils.uploadFile(avatar, FileUtils.GROUP_AVATAR_PATH, FileNameUtils.GROUP_AVATAR_PREFIX, Arrays.asList(MediaType.ANY_IMAGE_TYPE));
		if (urlPath != null) {
			groupUpdateBO.setGroupImage(urlPath);
		}

		groupService.update(groupUpdateBO);
		return ResponseVO.success();
	}

	@PostMapping("/delete/{groupId}")
	@ApiOperation("解散群组")
	public ResponseVO<?> delete(@ApiParam(value = "群組ID", required = true) @PathVariable("groupId") Long groupId) {
		groupService.delete(groupId);
		return ResponseVO.success();
	}

	@PostMapping("/mute")
	@ApiOperation("禁言群组")
	public ResponseVO<?> mute(@RequestBody GroupMuteForm form) throws Exception {
		log.info("禁言群组: {}", form);
		if (groupService.mute(form.getGroupId(), form.getMuteType())) {
			return ResponseVO.success();
		} else {
			return ResponseVO.error("[禁言群组]异常");
		}
	}

	@PostMapping("/privateChat")
	@ApiOperation("私聊群组")
	public ResponseVO<?> privateChat(@RequestBody GroupPrivateChatForm form) throws Exception {
		log.info("私聊群组: {}", form);
		if (groupService.privateChat(form.getGroupId(), form.getPrivateChat())) {
			return ResponseVO.success();
		} else {
			return ResponseVO.error("[私聊群组]异常");
		}
	}

	@GetMapping("/member")
	@ApiOperation("查看群成员")
	public ResponseVO<PageVO<GroupMemberPageVO>> member(GroupMemberQueryForm groupMemberQueryForm, Page page) {
		log.info("查看群成员: {}", groupMemberQueryForm);
		GroupMemberQuery groupMemberQuery = GroupMemberQuery.builder().build();
		ReflectionUtils.copyFields(groupMemberQuery, groupMemberQueryForm, ReflectionUtils.STRING_TRIM_TO_NULL);
		if (StringUtil.isNotEmpty(groupMemberQueryForm.getMember())) {
			groupMemberQuery.setMember(groupMemberQueryForm.getMember());
		}
		PageVO<GroupMemberPageVO> groupMemberPageVOPageVO = groupMemberService.page(groupMemberQuery, page);
		return ResponseVO.success(groupMemberPageVOPageVO);
	}

	@GetMapping("/member/without")
	@ApiOperation("查看群可添加成员")
	public ResponseVO<PageVO<GroupMemberPageVO>> withoutGroupMember(GroupMemberQueryForm groupMemberQueryForm,
			Page page) {
		log.info("查看群可添加成员: {}", groupMemberQueryForm);
		GroupMemberQuery groupMemberQuery = GroupMemberQuery.builder().build();
		ReflectionUtils.copyFields(groupMemberQuery, groupMemberQueryForm, ReflectionUtils.STRING_TRIM_TO_NULL);
		PageVO<GroupMemberPageVO> groupMemberPageVOPageVO = groupMemberService.withoutGroupPage(groupMemberQuery, page);
		return ResponseVO.success(groupMemberPageVOPageVO);
	}

	@PostMapping("/member/add")
	@ApiOperation("添加成员")
	public ResponseVO<?> addMember(@RequestBody GroupMemberAddForm form) throws Exception {
		log.info("添加成员: {}", form);
		GroupMemberAddBO groupMemberAddBO = GroupMemberAddBO.builder().build();
		ReflectionUtils.copyFields(groupMemberAddBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		groupMemberAddBO.setVerify(RelateVerifyEnum.SUCCESS.getValue());
		groupMemberService.inserts(groupMemberAddBO);
		return ResponseVO.success();
	}

	@PostMapping("/member/edit")
	@ApiOperation("編輯群成員")
	public ResponseVO<?> editMember(@RequestBody GroupMemberEditForm form) {
		log.info("編輯群成員: {}", form);
		GroupMemberEditBO groupMemberEditBO = GroupMemberEditBO.builder()
				.groupMemberTypeEnum(GroupMemberTypeEnum.parse(form.getMemberType())).build();
		ReflectionUtils.copyFields(groupMemberEditBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);

		groupMemberService.update(groupMemberEditBO);
		return ResponseVO.success();
	}

	@PostMapping("/member/delete")
	@ApiOperation("刪除群成員")
	public ResponseVO<?> deleteMember(@RequestBody GroupMemberDeleteForm form) throws Exception {
		log.info("刪除群成員: {}", form);
		GroupMemberDeleteBO groupMemberDeleteBO = GroupMemberDeleteBO.builder().build();
		ReflectionUtils.copyFields(groupMemberDeleteBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		groupMemberService.delete(groupMemberDeleteBO);

		return ResponseVO.success();
	}

	@PostMapping("/member/manager/edit")
	@ApiOperation("变更群管理身分")
	public ResponseVO<?> addManagerMember(@RequestBody GroupMemberManageEditForm form) throws Exception {
		log.info("变更群管理身分: {}", form);

		GroupMemberDeleteBO bo = GroupMemberDeleteBO.builder().groupId(form.getGroupId())
				.memberIdList(Collections.singletonList(form.getMemberId())).isManager(form.getIsManager()).build();
		groupMemberService.addManager(bo);
		return ResponseVO.success();
	}

	@PostMapping("/member/owner/change")
	@ApiOperation("变更群主")
	public ResponseVO<?> changeOwner(@RequestBody GroupMemberOwnerEditForm form) throws Exception {
		log.info("变更群主: {}", form);
		GroupPO groupPO = groupService.list(GroupQuery.builder().id(form.getGroupId()).build()).stream().findFirst()
				.orElse(null);
		Map<Long, MemberPO> memberPOMap = memberService
				.selectMemberByIds(Arrays.asList(form.getMemberId(), groupPO.getManagerId())).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));
		IMResult<Void> voidIMResult = GroupAdmin.transferGroup(memberPOMap.get(groupPO.getManagerId()).getUid(),
				groupPO.getGid(), memberPOMap.get(form.getMemberId()).getUid(), null, null);
		return ResponseVO.success();

	}
}
