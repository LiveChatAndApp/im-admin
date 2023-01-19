package cn.wildfirechat.admin.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.util.StringUtil;

import cn.wildfirechat.admin.mapper.GroupMapper;
import cn.wildfirechat.admin.mapper.GroupMemberMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.GroupMemberAddBO;
import cn.wildfirechat.common.model.bo.GroupMemberDeleteBO;
import cn.wildfirechat.common.model.bo.GroupMemberEditBO;
import cn.wildfirechat.common.model.bo.GroupMemberSyncBO;
import cn.wildfirechat.common.model.enums.GroupMemberBehaviorEnum;
import cn.wildfirechat.common.model.enums.GroupMemberTypeEnum;
import cn.wildfirechat.common.model.enums.RelateVerifyEnum;
import cn.wildfirechat.common.model.po.GroupMemberPO;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.GroupMemberQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.vo.GroupMemberPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.pojos.PojoGroupMember;
import cn.wildfirechat.sdk.GroupAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroupMemberService extends BaseService {
	@Resource
	private GroupMapper groupMapper;

	@Resource
	private GroupMemberMapper groupMemberMapper;

	@Resource
	private MemberMapper memberMapper;

	@Autowired
	private GroupService groupService;

	@Autowired
	private MemberService memberService;

	@Resource
	private UploadFileUtils uploadFileUtils;

	@Transactional
	public Integer insert(GroupMemberPO groupMemberPO) {
		return groupMemberMapper.inserts(Collections.singletonList(groupMemberPO), groupMemberPO.getMemberType());
	}

	@Transactional
	public Boolean inserts(GroupMemberAddBO bo) throws Exception {
		// String username = SpringSecurityUtil.getPrincipal();
		GroupQuery groupQuery = GroupQuery.builder().id(bo.getGroupId()).build();
		GroupPO groupPO = groupService.list(groupQuery).stream().findFirst().orElse(null);
		int originalMemberCount = groupPO.getMemberCount();
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		// MemberPO managerMemberPO = memberMapper.selectById(groupPO.getManagerId());

		List<MemberPO> memberPOs = memberService.selectMemberByIds(bo.getMemberIdList());
		Assert.isTrue(memberPOs.size() == bo.getMemberIdList().size(), message.getMessage(I18nAdmin.MEMBER_NOT_EXIST));

		List<PojoGroupMember> groupMembers = new ArrayList<>();
		memberPOs.forEach(memberPO -> {
			PojoGroupMember groupMember = new PojoGroupMember();
			groupMember.setMember_id(memberPO.getUid());
			groupMember.setAlias(memberPO.getNickName());
			groupMember.setType(0);
			groupMember.setCreateDt(new Date().getTime());
			groupMembers.add(groupMember);
		});

		IMResult<Void> imResult = GroupAdmin.addGroupMembers("admin", groupPO.getGid(), groupMembers, null, null);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			//群组管理-添加成员log
			int memberCount =groupPO.getMemberCount()+bo.getMemberIdList().size();
			OperateLogList list = new OperateLogList();
			list.addLog("群ID", groupPO.getGid(), false);
			list.addLog("新增成员",memberPOs.stream().map(MemberPO::getMemberName).collect(Collectors.toSet()),
					false);
			list.addDiffLog("群组人数",originalMemberCount,memberCount,false);
			logService.addOperateLog("/admin/group/member/add", list);
			return true;
		} else {
			return false;
		}
	}

	public Boolean update(GroupMemberPO groupMemberPO) {
		return groupMemberMapper.update(groupMemberPO) > 0;
	}

	@Transactional
	public Boolean update(GroupMemberEditBO groupMemberEditBO) {
		if (groupMemberEditBO.getGroupMemberTypeEnum().equals(GroupMemberTypeEnum.OWNER)
				&& groupMemberEditBO.getMemberIdList().size() > 1) {
			ResponseVO.error(message.getMessage(I18nAdmin.GROUP_MEMBER_Large_ONE));
		}
		GroupPO groupPO = groupService.list(GroupQuery.builder().id(groupMemberEditBO.getGroupId()).build()).stream()
				.findFirst().orElse(null);
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		List<Long> idList = null;

		// 如果是要修改owner，则需先把原本的owner改为member
		if (groupMemberEditBO.getGroupMemberTypeEnum().equals(GroupMemberTypeEnum.OWNER)) {
			GroupMemberPO groupMemberPO = GroupMemberPO.builder().groupId(groupMemberEditBO.getGroupId())
					.memberId(groupPO.getManagerId()).memberType(GroupMemberTypeEnum.MEMBER.getValue()).build();
			update(groupMemberPO);
			groupPO.setManagerId(groupMemberEditBO.getMemberIdList().stream().findFirst().orElse(null));
			groupService.update(groupPO);
			idList = Collections.singletonList(groupPO.getManagerId());
		} else {
			// 过滤为owner的群成员
			idList = groupMemberEditBO.getMemberIdList().stream().filter(id -> !id.equals(groupPO.getManagerId()))
					.collect(Collectors.toList());
		}

		Assert.isTrue(idList.size() > 0, message.get(I18nAdmin.GROUP_MEMBER_ERROR));
		return groupMemberMapper.updates(idList, groupMemberEditBO.getGroupId(),
				groupMemberEditBO.getGroupMemberTypeEnum().getValue()) > 0;
	}

	public List<GroupMemberPO> list(GroupMemberQuery groupMemberQuery) {
		return groupMemberMapper.list(groupMemberQuery);
	}

	public PageVO<GroupMemberPageVO> page(GroupMemberQuery groupMemberQuery, Page page) {

		if (page.isNeedSort()) {
			if (page.getSort() != null && page.getSort().equals("_create_time,DESC")) {
				page.setSort("GM._create_time,DESC");
			} else if (StringUtil.isEmpty(page.getSort())) {
				page.setSort("GM._create_time,DESC");
			}
		}

		page.startPageHelper();

		List<GroupMemberPageVO> groupMemberPageVOList = groupMemberMapper.selectGroupMemberPageVO(groupMemberQuery);
		for (GroupMemberPageVO groupMemberPageVO : groupMemberPageVOList) {
			if (StringUtil.isNotEmpty(groupMemberPageVO.getAvatarUrl())) {
				groupMemberPageVO.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(groupMemberPageVO.getAvatarUrl()));
			}
		}

		return new PageInfo<>(groupMemberPageVOList).convertToPageVO();
	}

	public PageVO<GroupMemberPageVO> withoutGroupPage(GroupMemberQuery groupMemberQuery, Page page) {
		if (page.isNeedSort()) {
			if (page.getSort() != null && page.getSort().equals("_create_time,DESC")) {
				page.setSort("M._create_time,DESC");
			} else if (StringUtil.isEmpty(page.getSort())) {
				page.setSort("M._create_time,DESC");
			}
		}
		page.startPageHelper();

		List<GroupMemberPageVO> groupMemberPageVOList = groupMemberMapper
				.selectWithoutGroupMemberPageVO(groupMemberQuery);

		for (GroupMemberPageVO groupMemberPageVO : groupMemberPageVOList) {
			if (StringUtil.isNotEmpty(groupMemberPageVO.getAvatarUrl())) {
				groupMemberPageVO.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(groupMemberPageVO.getAvatarUrl()));
			}
		}

		return new PageInfo<>(groupMemberPageVOList).convertToPageVO();
	}

	public Boolean deleteAll(Long groupId) throws Exception {
		GroupPO groupPO = groupService.list(GroupQuery.builder().id(groupId).build()).stream().findFirst().orElse(null);
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		MemberPO managerMemberPO = memberMapper.selectById(groupPO.getManagerId());

		List<GroupMemberPO> groupMemberPOs = groupMemberMapper
				.list(GroupMemberQuery.builder().groupId(groupId).build());
		List<Long> memberIds = new ArrayList<>();
		groupMemberPOs.forEach(groupMemberPO -> {
			memberIds.add(groupMemberPO.getMemberId());
		});

		List<String> imMembers = new ArrayList<>();
		List<MemberPO> memberPOs = memberMapper.selectMemberByIds(memberIds);
		memberPOs.forEach(memberPO -> {
			imMembers.add(memberPO.getUid());
		});

		IMResult<Void> imResult = GroupAdmin.kickoffGroupMembers(managerMemberPO.getUid(), groupPO.getGid(), imMembers,
				null, null);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean delete(GroupMemberDeleteBO bo) throws Exception {
		GroupPO groupPO = groupService.list(GroupQuery.builder().id(bo.getGroupId()).build()).stream().findFirst()
				.orElse(null);
		int memberCount = groupPO.getMemberCount();
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		MemberPO managerMemberPO = memberMapper.selectById(groupPO.getManagerId());

		List<String> imMembers = new ArrayList<>();
		List<MemberPO> memberPOs = memberMapper.selectMemberByIds(bo.getMemberIdList());
		memberPOs.forEach(memberPO -> {
			imMembers.add(memberPO.getUid());
			Assert.isTrue(!managerMemberPO.getId().equals(memberPO.getId()),
					message.getMessage(I18nAdmin.GROUP_MEMBER_MUST_NOT_BE_DELETED_REMOVE_MANAGE_ERROR));
		});

		IMResult<Void> imResult = GroupAdmin.kickoffGroupMembers("admin", groupPO.getGid(), imMembers, null, null);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			//群组管理-删除成员 log
			OperateLogList list = new OperateLogList();
			list.addLog("群ID", groupPO.getGid(), false);
			list.addDiffLog("群组人数",memberCount,memberCount-bo.getMemberIdList().size(),false);
			list.addLog("移除成员",memberPOs.stream().map(MemberPO::getMemberName).collect(Collectors.toSet()), false);
			logService.addOperateLog("/admin/group/member/delete", list);
			return true;
		} else {
			return false;
		}
	}

	public Boolean addManager(GroupMemberDeleteBO bo) throws Exception {
		Assert.isTrue(bo.getMemberIdList().size() > 0, "memberId list size is 0");

		GroupPO groupPO = groupService.list(GroupQuery.builder().id(bo.getGroupId()).build()).stream().findFirst()
				.orElse(null);
		Assert.notNull(groupPO, message.getMessage(I18nAdmin.GROUP_NOT_EXIST));

		Long memberId = bo.getMemberIdList().get(0);

		GroupMemberQuery query = GroupMemberQuery.builder().memberId(memberId).groupId(groupPO.getId()).build();
		GroupMemberPO groupMemberPO = groupMemberMapper.list(query).stream().findFirst().orElse(null);
		Assert.notNull(groupMemberPO, message.getMessage(I18nAdmin.GROUP_MEMBER_TO_BE_AN_MANAGE_ERROR));

		MemberPO ownerMemberPO = memberMapper.selectById(groupPO.getManagerId());
		MemberPO managerMemberPO = memberMapper.selectById(memberId);
		IMResult<Void> imResult = GroupAdmin.setGroupManager(ownerMemberPO.getUid(), groupPO.getGid(),
				Arrays.asList(managerMemberPO.getUid()), bo.getIsManager(), null, null);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			//群组管理-编辑会员身份log
			String originalManager = (bo.getIsManager() == true)?
					GroupMemberTypeEnum.MEMBER.getMessage() : GroupMemberTypeEnum.MANAGER.getMessage();
			String manager = (bo.getIsManager() == true)?
					GroupMemberTypeEnum.MANAGER.getMessage() : GroupMemberTypeEnum.MEMBER.getMessage();
				OperateLogList operateLogList = new OperateLogList();
				operateLogList.addLog("群ID",groupPO.getGid(),false);
				operateLogList.addLog("用户账号",managerMemberPO.getUid(),false);
				operateLogList.addDiffLog("成员类型",originalManager,manager,false);
				logService.addOperateLog("/admin/group/member/edit",operateLogList);
				return true;
			} else {
			return false;
		}
	}


	@Transactional
	public boolean syncGroupMember(GroupMemberSyncBO bo) {
		boolean isGroupDelay = false;
		if (bo.getMembers().size() > 0) {
			GroupPO groupPO = groupMapper.selectByGid(bo.getMembers().get(0).getGid());
			if (groupPO != null) {
				Map<String, GroupMemberSyncBO.Member> imMemberMap = new HashMap<>();
				List<String> imMembers = new ArrayList<>();
				bo.getMembers().forEach(member -> {
					imMemberMap.put(member.getMid(), member);
					imMembers.add(member.getMid());
				});

				List<Long> memberIds = new ArrayList<>();
				List<MemberPO> memberPos = memberMapper.selectMemberByUids(imMembers);
				Map<String, Long> memberMap = new HashMap<>();
				memberPos.forEach(memberPo -> {
					memberMap.put(memberPo.getUid(), memberPo.getId());
					memberIds.add(memberPo.getId());
				});

				GroupMemberBehaviorEnum behaviorEnum = GroupMemberBehaviorEnum.parse(bo.getBehavior());
				AtomicBoolean isDelay = new AtomicBoolean(false);
				switch (behaviorEnum) {
					case SYNC :
						bo.getMembers().forEach(member -> {
							Long memberId = memberMap.get(member.getMid());
							GroupMemberTypeEnum memberTypeEnum;
							if (memberId != null) {
								// if (groupPO.getManagerId().equals(memberId)) {
								// memberTypeEnum = GroupMemberTypeEnum.OWNER;
								// } else {
								GroupMemberSyncBO.Member imMember = imMemberMap.get(member.getMid());
								memberTypeEnum = GroupMemberTypeEnum.convert(imMember.getType());
								// }

								GroupMemberPO po = GroupMemberPO.builder().memberId(memberId).groupId(groupPO.getId())
										.memberType(memberTypeEnum.getValue())
										.verify(RelateVerifyEnum.SUCCESS.getValue())
										.createTime(new Date(member.getDt())).build();
								groupMemberMapper.insertUpdate(po);
							} else {
								isDelay.set(true);
							}
						});
						break;
					case REMOVE :
						memberPos.forEach(member -> {
							groupMemberMapper.delete(groupPO.getId(), memberIds);
						});
						break;
				}
				isGroupDelay = isDelay.get();
			} else {
				isGroupDelay = true;
			}
		}
		return isGroupDelay;
	}
}
