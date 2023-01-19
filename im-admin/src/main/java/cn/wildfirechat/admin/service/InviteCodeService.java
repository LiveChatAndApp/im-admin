package cn.wildfirechat.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.wildfirechat.admin.mapper.InviteCodeMapper;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.DefaultMemberAddBO;
import cn.wildfirechat.common.model.bo.InviteCodeAddBO;
import cn.wildfirechat.common.model.po.DefaultMemberPO;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.DefaultMemberQuery;
import cn.wildfirechat.common.model.query.InviteCodePageQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.vo.InviteCodeDefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.InviteCodePageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InviteCodeService extends BaseService {
	@Resource
	private InviteCodeMapper inviteCodeMapper;

	@Autowired
	private DefaultMemberService defaultMemberService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private GroupService groupService;

	@Resource
	private UploadFileUtils uploadFileUtils;

	public PageVO<InviteCodePageVO> page(InviteCodePageQuery query, Page page) {
		page.startPageHelper();
		List<InviteCodePageVO> list = inviteCodeMapper.selectInviteCodePageVO(query);

		return new PageInfo<>(list).convertToPageVO();
	}

	public List<InviteCodePO> list(InviteCodeQuery query) {
		return inviteCodeMapper.list(query);
	}

	@Transactional
	public Boolean update(InviteCodePO inviteCodePO) {
		return inviteCodeMapper.update(inviteCodePO) > 0;
	}

	@Transactional
	public Long insert(InviteCodeAddBO inviteCodeAddBO) {
		InviteCodeQuery inviteCodeQuery = new InviteCodeQuery();
		inviteCodeQuery.setInviteCode(inviteCodeAddBO.getInviteCode());

		InviteCodePO inviteCodePO = list(inviteCodeQuery).stream().findFirst().orElse(null);
		Assert.isNull(inviteCodePO, message.getMessage(I18nAdmin.INVITE_CODE_EXIST));

		inviteCodePO = new InviteCodePO();
		ReflectionUtils.copyFields(inviteCodePO, inviteCodeAddBO, ReflectionUtils.STRING_TRIM_TO_NULL);
		inviteCodePO.setCreatorRole(EditorRoleEnum.ADMIN.getValue());
		inviteCodePO.setUpdaterRole(EditorRoleEnum.ADMIN.getValue());

		inviteCodeMapper.insert(inviteCodePO);
		if (inviteCodeAddBO.getUsername() != null) {
			DefaultMemberAddBO defaultMemberAddBO = DefaultMemberAddBO.builder()
					.defaultMemberDefaultTypeEnum(DefaultMemberDefaultTypeEnum.FRIEND)
					.defaultMemberTypeEnum(DefaultMemberTypeEnum.INVITE_CODE_ONLY)
					.username(inviteCodeAddBO.getUsername()).inviteCodeId(inviteCodePO.getId())
					.creator(inviteCodeAddBO.getCreator()).welcomeText(inviteCodeAddBO.getWelcomeText())
					.inviteCode(inviteCodeAddBO.getInviteCode()).build();
			defaultMemberService.insert(defaultMemberAddBO);
		}

		// 邀请码-新增 log
		OperateLogList list = new OperateLogList();
		list.addLog("邀请码", inviteCodeAddBO.getInviteCode(), false);
		list.addLog("备注", inviteCodeAddBO.getMemo(), false);
		list.addLog("预设好友(用户账号)", inviteCodeAddBO.getUsername(), false);
		list.addLog("预设好友欢迎词", inviteCodeAddBO.getWelcomeText(), false);
		list.addLog("预设好友模式", InviteCodeDefaultFriendTypeEnum.parse(inviteCodeAddBO.getFriendsDefaultType()).getMessage(), false);
		list.addLog("状态", InviteCodeStatusEnum.parse(inviteCodeAddBO.getStatus()).getMessage(), false);
		logService.addOperateLog("/admin/inviteCode/add", list);

		return inviteCodePO.getId();
	}

	public void updateFriendDefaultType(InviteCodeDefaultFriendTypeEnum inviteCodeDefaultFriendTypeEnum,
			String updater) {
		int current = InviteCodeDefaultFriendTypeEnum.ALL == inviteCodeDefaultFriendTypeEnum
				? InviteCodeDefaultFriendTypeEnum.LOOP.getValue()
				: InviteCodeDefaultFriendTypeEnum.ALL.getValue();
		int target = inviteCodeDefaultFriendTypeEnum.getValue();

		inviteCodeMapper.updateFriendDefaultType(current, target, updater, EditorRoleEnum.ADMIN.getValue());

		// 默认好友-全局默认好友设置
		OperateLogList list = new OperateLogList();
		list.addLog("预设好友模式", inviteCodeDefaultFriendTypeEnum.getMessage(), false);
		logService.addOperateLog("/admin/defaultMember/type", list);
	}

	public PageVO<InviteCodeDefaultMemberPageVO> selectInviteCodeDefaultMemberPageVO(Long id, String inviteCode,
			DefaultMemberDefaultTypeEnum defaultTypeEnum, Page page) {
		page.startPageHelper();
		List<DefaultMemberPO> defaultMemberPOList = defaultMemberService
				.list(DefaultMemberQuery.builder().inviteCodeId(id).defaultType(defaultTypeEnum.getValue()).build());

		Set<Long> memberIdList = defaultMemberPOList.stream()
				.filter(defaultMemberPO -> defaultMemberPO.getDefaultType()
						.equals(DefaultMemberDefaultTypeEnum.FRIEND.getValue()))
				.map(DefaultMemberPO::getMemberId).collect(Collectors.toSet());
		Set<Long> groupIdList = defaultMemberPOList.stream()
				.filter(defaultMemberPO -> defaultMemberPO.getDefaultType()
						.equals(DefaultMemberDefaultTypeEnum.GROUP.getValue()))
				.map(DefaultMemberPO::getGroupId).collect(Collectors.toSet());

		List<MemberPO> memberPOList = memberService.selectMemberByIds(memberIdList);
		List<GroupPO> groupPOList = groupService.selectGroupByIds(groupIdList);

		List<InviteCodeDefaultMemberPageVO> inviteCodeDefaultMemberPageVOList = new ArrayList<>();

		defaultMemberPOList.forEach(defaultMemberPO -> {
			InviteCodeDefaultMemberPageVO pageVO = new InviteCodeDefaultMemberPageVO();

			MemberPO memberPO = memberPOList.stream().filter(po -> po.getId().equals(defaultMemberPO.getMemberId()))
					.findFirst().orElse(null);
			GroupPO groupPO = groupPOList.stream().filter(po -> po.getId().equals(defaultMemberPO.getGroupId()))
					.findFirst().orElse(null);
			String account = null;
			String nickName = null;
			String groupName = null;
			String phone = null;
			String avatarUrl = null;
			if (memberPO != null) {
				nickName = memberPO.getNickName();
				account = memberPO.getMemberName();
				phone = memberPO.getPhone();
				if(StringUtil.isNotEmpty(memberPO.getAvatarUrl())){
					avatarUrl = uploadFileUtils.parseFilePathToUrl(memberPO.getAvatarUrl());
				}
			} else if (groupPO != null) {
				account = groupPO.getGid();
				groupName = groupPO.getName();
				if(StringUtil.isNotEmpty(groupPO.getGroupImage())){
					avatarUrl = uploadFileUtils.parseFilePathToUrl(groupPO.getGroupImage());
				}
			}

			pageVO.setId(defaultMemberPO.getId());
			pageVO.setInviteCode(inviteCode);
			pageVO.setAccount(account);
			pageVO.setNickName(nickName);
			pageVO.setPhone(phone);
			pageVO.setGroupName(groupName);
			pageVO.setAvatarUrl(avatarUrl);
			pageVO.setWelcomeText(defaultMemberPO.getWelcomeText());
			pageVO.setCreateTime(defaultMemberPO.getCreateTime());

			inviteCodeDefaultMemberPageVOList.add(pageVO);
		});

		return new PageInfo<>(inviteCodeDefaultMemberPageVOList).convertToPageVO();
	}
}
