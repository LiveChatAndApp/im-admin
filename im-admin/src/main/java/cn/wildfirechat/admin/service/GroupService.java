package cn.wildfirechat.admin.service;

import java.util.*;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.mapper.AdminUserMapper;
import cn.wildfirechat.admin.mapper.GroupMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.GroupAddBO;
import cn.wildfirechat.common.model.bo.GroupDissolveBO;
import cn.wildfirechat.common.model.bo.GroupSyncBO;
import cn.wildfirechat.common.model.bo.GroupUpdateBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.im.dto.GroupAddDTO;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.GroupPageQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.vo.GroupPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.common.utils.StringUtil;
import cn.wildfirechat.pojos.OutputCreateGroupResult;
import cn.wildfirechat.pojos.PojoGroupInfo;
import cn.wildfirechat.pojos.PojoGroupMember;
import cn.wildfirechat.proto.ProtoConstants;
import cn.wildfirechat.sdk.GroupAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroupService extends BaseService {

	@Resource
	private GroupMapper groupMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private GroupMemberService groupMemberService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private SpringMessage message;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private AdminUserMapper adminUserMapper;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	public interface ModifyGroupInfoType {
		int Modify_Group_Name = 0;
		int Modify_Group_Portrait = 1;
		int Modify_Group_Extra = 2;
		int Modify_Group_Mute = 3;
		int Modify_Group_JoinType = 4;
		int Modify_Group_PrivateChat = 5;
		int Modify_Group_Searchable = 6;
		// 仅专业版支持
		int Modify_Group_History_Message = 7;
		// 仅专业版的server api支持
		int Modify_Group_Max_Member_Count = 8;
	}

	public List<GroupPO> list(GroupQuery query) {
		return groupMapper.list(query);
	}

	public PageVO<GroupPageVO> page(GroupPageQuery groupPageQuery, Page page) {
		page.startPageHelper();
		List<GroupPageVO> list = groupMapper.selectGroupPageVO(groupPageQuery);
		for (GroupPageVO groupPageVO : list) {
			if (StringUtil.isNotEmpty(groupPageVO.getGroupImage())) {
				groupPageVO.setGroupImage(uploadFileUtils.parseFilePathToUrl(groupPageVO.getGroupImage()));
			}
		}

		return new PageInfo<>(list).convertToPageVO();
	}

	@Transactional
	public boolean insert(GroupAddBO bo) throws Exception {
		boolean isSuccess = false;
		ObjectMapper objMapper = new ObjectMapper();
		MemberQuery memberQuery = MemberQuery.builder().memberName(bo.getMemberName()).build();
		MemberDTO memberDTO = memberService.list(memberQuery).stream().findFirst().orElse(null);
		Assert.notNull(memberDTO, message.getMessage(I18nAdmin.MEMBER_NOT_EXIST));
		Assert.isTrue(memberDTO.getCreateGroupEnable(), message.get(I18nAdmin.GROUP_CREATE_ERROR));

		PojoGroupInfo pojoGroupInfo = new PojoGroupInfo();
		pojoGroupInfo.setName(bo.getGroupName());
		if (StringUtil.isNotEmpty(bo.getGroupImage())) {
			pojoGroupInfo.setPortrait(uploadFileUtils.parseFilePathToUrl(bo.getGroupImage()));
		}
		pojoGroupInfo.setOwner(memberDTO.getUid());
		pojoGroupInfo.setType(2);
		pojoGroupInfo.setHistory_message(1);
		if (bo.getGroupType().equals(GroupTypeEnum.BROADCAST.getValue())) {
			pojoGroupInfo.setMute(1);
			pojoGroupInfo.setPrivate_chat(1);
		}

		GroupAddDTO dto = GroupAddDTO.builder().creator(bo.getCreator()).creatorRole(EditorRoleEnum.ADMIN.getValue())
				.groupType(GroupTypeEnum.parse(bo.getGroupType()).getValue()).build();
		pojoGroupInfo.setExtra(objMapper.writeValueAsString(dto));

		IMResult<OutputCreateGroupResult> imResult = GroupAdmin.createGroup(memberDTO.getUid(), pojoGroupInfo,
				new ArrayList<>(), null, null);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			PojoGroupMember pojoGroupMember = new PojoGroupMember();
			pojoGroupMember.setMember_id(memberDTO.getUid());
			pojoGroupMember.setType(2);// 0普通成员；1，管理员；2，群主，与Owner相同
			IMResult<Void> voidIMResult = GroupAdmin.addGroupMembers(memberDTO.getUid(),
					imResult.getResult().getGroup_id(), Arrays.asList(pojoGroupMember), null, null);
			if (voidIMResult.code == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
				//群组管理-新增 log
				OperateLogList list = new OperateLogList();
				list.addLog("群组名称",bo.getGroupName(),false);
				list.addLog("群组类型",bo.getGroupType(),false);
				list.addLog("群主账号",bo.getMemberName(),false);
				logService.addOperateLog("/admin/group/add", list);
				return true;
			}
		}
		return isSuccess;
	}

	@Transactional
	public boolean mute(String groupId, Integer muteType) throws Exception {
		GroupPO groupPO = groupMapper.selectByGid(groupId);
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_ID_ERROR));

		MemberPO memberPO = memberMapper.selectById(groupPO.getManagerId());
		Integer muteTypeValues = GroupMuteTypeEnum.convert(GroupMuteTypeEnum.parse(muteType));
		IMResult<Void> voidIMResult = GroupAdmin.modifyGroupInfo(memberPO.getUid(), groupPO.getGid(),
				ProtoConstants.ModifyGroupInfoType.Modify_Group_Mute, muteTypeValues.toString(), null, null);
		if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {

			//群组管理-禁言群组 log
			OperateLogList list = new OperateLogList();
			list.addLog("群ID",groupId,false);
			if(muteType == GroupMuteTypeEnum.NORMAL.getValue()){
				list.addDiffLog("发言状态",GroupMuteTypeEnum.ALL.getMessage(),GroupMuteTypeEnum.NORMAL.getMessage(),false);
			}else if(muteType == GroupMuteTypeEnum.ALL.getValue()) {
				list.addDiffLog("发言状态",GroupMuteTypeEnum.NORMAL.getMessage(),GroupMuteTypeEnum.ALL.getMessage(),false);
			}
			logService.addOperateLog("/admin/group/mute",list);
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean privateChat(String groupId, Integer privateChat) throws Exception {
		GroupPO groupPO = groupMapper.selectByGid(groupId);
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_ID_ERROR));

		MemberPO memberPO = memberMapper.selectById(groupPO.getManagerId());
		//im接收 0 允许私聊；1 禁止群成员私聊
		Integer privateChatValues = GroupPrivateChatEnum.convert(GroupPrivateChatEnum.parse(privateChat));
		IMResult<Void> voidIMResult = GroupAdmin.modifyGroupInfo(memberPO.getUid(), groupPO.getGid(),
				ProtoConstants.ModifyGroupInfoType.Modify_Group_PrivateChat, privateChatValues.toString(), null, null);
		if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			//群组管理-私聊群组 log
			OperateLogList list = new OperateLogList();
			list.addLog("群ID",groupId,false);
			if(privateChatValues == 0){
				list.addDiffLog("私聊状态",GroupPrivateChatEnum.BLOCK.getMessage(),GroupPrivateChatEnum.NORMAL.getMessage(),false);
			}else if(privateChatValues == 1){
				list.addDiffLog("私聊状态",GroupPrivateChatEnum.NORMAL.getMessage(),GroupPrivateChatEnum.BLOCK.getMessage(),false);
			}
			logService.addOperateLog("/admin/group/privateChat",list);

			return true;
		} else {
			return false;
		}
	}

	public Boolean update(GroupPO groupPO) {
		return groupMapper.update(groupPO) > 0;
	}

	@Transactional
	public Boolean update(GroupUpdateBO groupUpdateBO) {
		GroupQuery groupQuery = GroupQuery.builder().gid(groupUpdateBO.getGroupId()).build();
		GroupPO groupPO = groupMapper.list(groupQuery).stream().findFirst().orElse(null);

		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		MemberDTO memberDTO = memberService
				.list(MemberQuery.builder().memberName(groupUpdateBO.getMemberName()).build()).stream().findFirst()
				.orElse(null);

		MemberDTO originalMemberDTO = memberService.list(MemberQuery.builder().id(groupPO.getManagerId()).build())
				.stream().findFirst().orElse(null);
		Assert.notNull(memberDTO, message.get(I18nAdmin.MEMBER_NOT_EXIST));

		OperateLogList operateLogList = new OperateLogList();

		try {
			if (StringUtil.isNotEmpty(groupUpdateBO.getMemberName())
					&& !Objects.equals(groupPO.getManagerId(), memberDTO.getId())) {
				IMResult<Void> memberNameIMResult = GroupAdmin.transferGroup("admin", groupPO.getGid(),
						memberDTO.getUid(), null, null);
				if (memberNameIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
					operateLogList.addDiffLog("群主账号", originalMemberDTO.getMemberName(), memberDTO.getMemberName(),
							false);
				}
			}

			if (StringUtil.isNotEmpty(groupUpdateBO.getGroupImage())
					&& !groupPO.getGroupImage().equals(groupUpdateBO.getGroupImage())) {
				IMResult<Void> portraitIMResult = GroupAdmin.modifyGroupInfo("admin", groupPO.getGid(),
						ModifyGroupInfoType.Modify_Group_Portrait, uploadFileUtils.parseFilePathToUrl(groupUpdateBO.getGroupImage()), null,
						null);
				if (portraitIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
					operateLogList.addDiffLog("头像", groupPO.getGroupImage(), groupUpdateBO.getGroupImage(), false);
				}
			}

			if (StringUtil.isNotEmpty(groupUpdateBO.getName()) && !groupPO.getName().equals(groupUpdateBO.getName())) {
				IMResult<Void> nameIMResult = GroupAdmin.modifyGroupInfo("admin", groupPO.getGid(),
						ModifyGroupInfoType.Modify_Group_Name, groupUpdateBO.getName(), null, null);
				if (nameIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
					operateLogList.addDiffLog("群组名称", groupPO.getName(), groupUpdateBO.getName(), false);
				}
			}
		} catch (Exception e) {
			return false;
		}
		logService.addOperateLog("/admin/group/edit", operateLogList);

		return true;
	}

	@Transactional
	public Boolean delete(Long groupId) {
		GroupPO groupPO = list(GroupQuery.builder().id(groupId).build()).stream().findFirst().orElse(null);
//		状态 1: 正常, 2: 解散
		String originalStatus = (groupPO.getStatus() == GroupStatusEnum.NORMAL.getValue())?
				GroupStatusEnum.NORMAL.getMessage() : GroupStatusEnum.DISSOLVE.getMessage();
		Assert.notNull(groupPO, message.get(I18nAdmin.GROUP_NOT_EXIST));
		MemberDTO memberDTO = memberService.list(MemberQuery.builder().id(groupPO.getManagerId()).build()).stream()
				.findFirst().orElse(new MemberDTO());
		try {
			IMResult<Void> voidIMResult = GroupAdmin.dismissGroup(memberDTO.getUid(), groupPO.getGid(), null, null);
			if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
				//群组管理-解散log
				OperateLogList operateLogList = new OperateLogList();
				operateLogList.addLog("群ID",groupPO.getGid(),false);
				operateLogList.addDiffLog("群状态",originalStatus,GroupStatusEnum.DISSOLVE.getMessage(),false);
				logService.addOperateLog("/admin/group/delete/{groupId}",operateLogList);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("解散{}群组失败", groupId, e);
			return false;
		}
	}

	public List<GroupPO> selectGroupByIds(Collection<Long> idList) {
		return idList.size() > 0 ? groupMapper.selectGroupByIds(idList) : new ArrayList<>();
	}

	@Transactional
	public void syncGroup(GroupSyncBO bo) throws JsonProcessingException {
		MemberPO memberPO = memberMapper.selectByUid(bo.getOwner());
		String operator = memberPO.getMemberName();
		EditorRoleEnum operatorRole = EditorRoleEnum.MEMBER;
		GroupTypeEnum groupType = GroupTypeEnum.GENERAL;
		ObjectMapper objMapper = new ObjectMapper();
		if (StringUtil.isNotBlank(bo.getExtra())) {
			GroupAddDTO groupAddDTO = objMapper.readValue(bo.getExtra(), GroupAddDTO.class);
			operator = groupAddDTO.getCreator();
			operatorRole = EditorRoleEnum.parse(groupAddDTO.getCreatorRole());
			groupType = GroupTypeEnum.parse(groupAddDTO.getGroupType());
		}
		log.info("输出 groupSyncBO: {}", bo);
		// GroupPO groupPO =
		// list(GroupQuery.builder().gid(bo.getGid()).build()).stream().findFirst()
		// .orElse(new GroupPO());
		GroupPO targetPO = GroupPO.builder().gid(bo.getGid()).name(bo.getName()).managerId(memberPO.getId())
				.memberCount(bo.getMemberCount()).memberCountLimit(GroupPO.DEFAULT_MEMBER_COUNT_LIMIT)
				.groupImage(uploadFileUtils.parseFileUrlToPath(bo.getPortrait()))
				.muteType(GroupMuteTypeEnum.convert(bo.getMute()).getValue())
				.enterAuthType(GroupEnterAuthTypeEnum.UNNECESSARY.getValue())// 待IM功能扩充
				.invitePermission(GroupInvitePermissionEnum.parse(bo.getJoinType()).getValue())
				.inviteAuth(GroupInviteAuthEnum.NORMAL.getValue())// 待IM功能扩充
				.modifyPermission(GroupModifyPermissionEnum.ALL.getValue())// 待IM功能扩充
				.privateChat(GroupPrivateChatEnum.convert(bo.getPrivateChat()).getValue()).bulletinTitle(null)// 待IM功能扩充
				.bulletinContent(null)// 待IM功能扩充
				.status(GroupStatusEnum.NORMAL.getValue()).groupType(groupType.getValue())
				.createTime(new Date(bo.getDt())).updateTime(new Date(bo.getMemberDt())).creator(operator)
				.updater(operator).creatorRole(operatorRole.getValue()).updaterRole(operatorRole.getValue()).build();
		groupMapper.insertUpdate(targetPO);

	}

	@Transactional
	public void dissolveGroup(GroupDissolveBO bo, EditorRoleEnum roleEnum) {
		GroupPO groupPO = groupMapper.selectByGid(bo.getGid());
		String updater;
		if (roleEnum == EditorRoleEnum.ADMIN) {
			AdminUserPO adminPO = adminUserMapper.selectById(bo.getAdminUserId());
			updater = adminPO.getUsername();
		} else {
			MemberPO memberPO = memberMapper.list(MemberQuery.builder().id(groupPO.getManagerId()).build()).stream()
					.findFirst().orElse(null);
			updater = memberPO.getMemberName();
		}

		GroupPO targetPO = GroupPO.builder().id(groupPO.getId()).status(GroupStatusEnum.DISSOLVE.getValue())
				.updater(updater).updaterRole(roleEnum.getValue()).build();
		groupMapper.update(targetPO);
	}
}
