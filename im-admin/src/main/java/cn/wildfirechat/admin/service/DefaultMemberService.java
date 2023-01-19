package cn.wildfirechat.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.DefaultMemberDTO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.DefaultMemberTypeEnum;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wildfirechat.admin.mapper.DefaultMemberMapper;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.DefaultMemberAddBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.enums.DefaultMemberDefaultTypeEnum;
import cn.wildfirechat.common.model.po.DefaultMemberPO;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.DefaultMemberQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.vo.DefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Assert;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultMemberService extends BaseService {

	@Autowired
	private SpringMessage message;

	@Resource
	private DefaultMemberMapper defaultMemberMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private InviteCodeService inviteCodeService;

	@Autowired
	private GroupService groupService;

	@Resource
	private UploadFileUtils uploadFileUtils;

	public Long insert(DefaultMemberAddBO defaultMemberAddBO) {
		Long inviteCodeId = defaultMemberAddBO.getInviteCodeId();
		if (inviteCodeId == null) {
			InviteCodePO inviteCodePO = inviteCodeService
					.list(InviteCodeQuery.builder().inviteCode(defaultMemberAddBO.getInviteCode()).build()).stream()
					.findFirst().orElse(null);
			Assert.notNull(inviteCodePO, message.getMessage(I18nAdmin.INVITE_CODE_NOT_EXIST));
			inviteCodeId = inviteCodePO.getId();
		}

		// 若Type为全部新加入会员, 则邀请码id设为0
		if(defaultMemberAddBO.getDefaultMemberTypeEnum().equals(DefaultMemberTypeEnum.ALL)){
			inviteCodeId = 0L;
		}

		DefaultMemberPO defaultMemberPO = DefaultMemberPO.builder().build();
		ReflectionUtils.copyFields(defaultMemberPO, defaultMemberAddBO, ReflectionUtils.STRING_TRIM_TO_NULL);
		defaultMemberPO.setDefaultType(defaultMemberAddBO.getDefaultMemberDefaultTypeEnum().getValue());
		defaultMemberPO.setType(defaultMemberAddBO.getDefaultMemberTypeEnum().getValue());
		defaultMemberPO.setInviteCodeId(inviteCodeId);

		if (defaultMemberAddBO.getDefaultMemberDefaultTypeEnum().equals(DefaultMemberDefaultTypeEnum.FRIEND)) {
			MemberDTO memberDTO = memberService
					.list(MemberQuery.builder().memberName(defaultMemberAddBO.getUsername()).build()).stream()
					.findFirst().orElse(null);

			Assert.notNull(memberDTO, message.getMessage(I18nAdmin.MEMBER_ACCOUNT_ERROR));
			defaultMemberPO.setMemberId(memberDTO.getId());
		} else if (defaultMemberAddBO.getDefaultMemberDefaultTypeEnum().equals(DefaultMemberDefaultTypeEnum.GROUP)) {
			GroupPO groupPO = groupService.list(GroupQuery.builder().gid(defaultMemberAddBO.getUsername()).build())
					.stream().findFirst().orElse(null);

			Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_ID_ERROR));
			defaultMemberPO.setGroupId(groupPO.getId());
		}

		Assert.isFalse(list(DefaultMemberQuery.builder().memberId(defaultMemberPO.getMemberId())
				.groupId(defaultMemberPO.getGroupId()).inviteCodeId(defaultMemberPO.getInviteCodeId()).build()).stream()
						.findFirst().isPresent(),
				message.get(I18nAdmin.DEFAULT_MEMBER_EXIST));

		defaultMemberMapper.insert(defaultMemberPO);

		// 默认好友-新增 log
		OperateLogList list = new OperateLogList();
		list.addLog("类型", defaultMemberAddBO.getDefaultMemberTypeEnum().getMessage(), false);
		list.addLog("邀请码", defaultMemberAddBO.getInviteCode(), false);
		list.addLog("预设类型", defaultMemberAddBO.getDefaultMemberDefaultTypeEnum().getMessage(), false);
		list.addLog("用户账号/群ID", defaultMemberAddBO.getUsername(), false);
		list.addLog("欢迎词", defaultMemberAddBO.getWelcomeText(), false);
		logService.addOperateLog("/admin/defaultMember/add", list);

		return defaultMemberPO.getId();
	}

	public PageVO<DefaultMemberPageVO> page(DefaultMemberQuery defaultMemberQuery, Page page) {
		page.startPageHelper();
		List<DefaultMemberPageVO> defaultMemberPageVOList = defaultMemberMapper
				.selectDefaultMemberPageVO(defaultMemberQuery);

		PageVO<DefaultMemberPageVO> defaultMemberPageVOPageVO = new PageInfo<>(defaultMemberPageVOList)
				.convertToPageVO();

		Set<Long> memberIdSet = defaultMemberPageVOList.stream().map(DefaultMemberPageVO::getMemberId)
				.filter(Objects::nonNull).collect(Collectors.toSet());

		Set<Long> groupIdSet = defaultMemberPageVOList.stream().map(DefaultMemberPageVO::getGroupId)
				.filter(Objects::nonNull).collect(Collectors.toSet());

		Map<Long, MemberPO> memberPOMap = memberService.selectMemberByIds(memberIdSet).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));

		Map<Long, GroupPO> groupPOMap = groupService.selectGroupByIds(groupIdSet).stream()
				.collect(Collectors.toMap(GroupPO::getId, groupPO -> groupPO));

		for (DefaultMemberPageVO defaultMemberPageVO : defaultMemberPageVOPageVO.getPage()) {
			if (defaultMemberPageVO.getMemberId() != null) {
				MemberPO memberPO = memberPOMap.get(defaultMemberPageVO.getMemberId());
				defaultMemberPageVO.setMemberName(memberPO.getMemberName());
				defaultMemberPageVO.setNickName(memberPO.getNickName());
				defaultMemberPageVO.setPhone(memberPO.getPhone());
				if (StringUtil.isNoneEmpty(memberPO.getAvatarUrl())) {
					defaultMemberPageVO.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(memberPO.getAvatarUrl()));
				}
			} else if (defaultMemberPageVO.getGroupId() != null) {
				GroupPO groupPO = groupPOMap.get(defaultMemberPageVO.getGroupId());
				if (groupPO != null) {
					defaultMemberPageVO.setGroupName(groupPO.getName());
					defaultMemberPageVO.setGid(groupPO.getGid());
					if (StringUtil.isNoneEmpty(groupPO.getGroupImage())) {
						defaultMemberPageVO.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(groupPO.getGroupImage()));
					}
				}
			}
		}
		return defaultMemberPageVOPageVO;
	}

	public List<DefaultMemberPO> list(DefaultMemberQuery defaultMemberQuery) {
		return defaultMemberMapper.list(defaultMemberQuery);
	}

	public Boolean update(DefaultMemberPO defaultMemberPO) {
		return defaultMemberMapper.update(defaultMemberPO) > 0;
	}

	public Boolean delete(Long id) {
		return defaultMemberMapper.delete(id) > 0;
	}

	public DefaultMemberDTO selectById(Long id) {
		return defaultMemberMapper.selectById(id);
	}
}
