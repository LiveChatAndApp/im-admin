package cn.wildfirechat.admin.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wildfirechat.admin.mapper.FriendMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.MemberFriendQueryBO;
import cn.wildfirechat.common.model.bo.MemberJoinFriendBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.enums.FriendBlackedEnum;
import cn.wildfirechat.common.model.enums.RelateVerifyEnum;
import cn.wildfirechat.common.model.po.FriendPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.FriendQuery;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.vo.MemberFriendVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Assert;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.StringUtil;
import cn.wildfirechat.sdk.RelationAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FriendService extends BaseService {
	@Resource
	private FriendMapper friendMapper;

	@Autowired
	private MemberService memberService;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private UploadFileUtils uploadFileUtils;

	public Boolean insert(MemberJoinFriendBO memberJoinFriendBO) {
		if (memberJoinFriendBO.getSelfMemberId() == null && memberJoinFriendBO.getSelfUid() != null) {
			MemberDTO memberDTO = memberService.list(MemberQuery.builder().uid(memberJoinFriendBO.getSelfUid()).build())
					.stream().findFirst().orElse(new MemberDTO());
			memberJoinFriendBO.setSelfMemberId(memberDTO.getId());
		}

		if (memberJoinFriendBO.getMemberId() == null && memberJoinFriendBO.getTargetUid() != null) {
			MemberDTO memberDTO = memberService
					.list(MemberQuery.builder().uid(memberJoinFriendBO.getTargetUid()).build()).stream().findFirst()
					.orElse(new MemberDTO());
			memberJoinFriendBO.setMemberId(memberDTO.getId());
		}

		Assert.isFalse(memberJoinFriendBO.getSelfMemberId().equals(memberJoinFriendBO.getMemberId()),
				message.get(I18nAdmin.FRIEND_TARGET_SELF));
		FriendPO friendPO = relateExist(null, memberJoinFriendBO.getSelfMemberId(), memberJoinFriendBO.getMemberId())
				.stream().findFirst().orElse(null);

		if (friendPO == null) {
			log.info("输出 line: 69");
			friendPO = new FriendPO();
			ReflectionUtils.copyFields(friendPO, memberJoinFriendBO, ReflectionUtils.STRING_TRIM_TO_NULL);
			friendPO.setMemberSourceId(memberJoinFriendBO.getSelfMemberId());
			friendPO.setMemberTargetId(memberJoinFriendBO.getMemberId());
			if (memberJoinFriendBO.getVerify() != null && memberJoinFriendBO.getVerify() == RelateVerifyEnum.VERIFY.getValue()) {
				friendPO.setVerify(RelateVerifyEnum.VERIFY.getValue());
				friendPO.setRequestReceiver(memberJoinFriendBO.getMemberId());
			}
			if (Objects.equals(memberJoinFriendBO.getAction(), MemberJoinFriendBO.ADD_FRIEND)) {
				friendPO.setRequestReceiver(memberJoinFriendBO.getMemberId());
				log.info("输出 line: 75");
				friendPO.setVerify(RelateVerifyEnum.SUCCESS.getValue());
				friendPO.setAddTime(
						memberJoinFriendBO.getAddTime() != null ? memberJoinFriendBO.getAddTime() : new Date());
			}
		} else {
			log.info("输出 line: 81");
			if (!friendPO.getVerify().equals(RelateVerifyEnum.SUCCESS.getValue())) {
				log.info("输出 line: 83");
				friendPO.setAddTime(memberJoinFriendBO.getAddTime());
				friendPO.setVerify(RelateVerifyEnum.SUCCESS.getValue());
				friendPO.setRequestReceiver(memberJoinFriendBO.getMemberId());
			}
		}

		return friendMapper.insert(friendPO) > 0;
	}

	public Boolean update(MemberJoinFriendBO bo) {
		MemberPO sourcePO = memberMapper.selectByUid(bo.getSelfUid());
		MemberPO targetPO = memberMapper.selectByUid(bo.getTargetUid());
		FriendPO friendPO = FriendPO.builder().memberSourceId(sourcePO.getId()).memberTargetId(targetPO.getId())
				.sourceBlacked(FriendBlackedEnum.convert(bo.getBlacked()).getValue()).build();
		if (bo.getVerify() != null) {
			friendPO.setVerify(bo.getVerify());
		}
		return friendMapper.update(friendPO) > 0;
	}

	public List<FriendPO> list(FriendQuery friendQuery) {
		return friendMapper.list(friendQuery);
	}

	public List<FriendPO> relateExist(Long id, Long memberSourceId, Long memberTargetId) {
		return friendMapper.relateExist(id, memberSourceId, memberTargetId);
	}

	public List<FriendPO> selectFriendRelate(Long memberId, RelateVerifyEnum relateVerifyEnum,
			Collection<Long> memberIdList) {
		return friendMapper.selectFriendRelate(memberId, relateVerifyEnum.getValue(), memberIdList);
	}

	public PageVO<MemberFriendVO> selectMemberFriend(MemberFriendQueryBO memberFriendQueryBO, Page page) {
		MemberQuery memberQuery = new MemberQuery();
		ReflectionUtils.copyFields(memberQuery, memberFriendQueryBO, ReflectionUtils.STRING_TRIM_TO_NULL);
		memberQuery.setId(null);
		List<MemberDTO> memberDTOList = memberService.list(memberQuery).stream()
				.filter(memberDTO -> !memberDTO.getId().equals(memberFriendQueryBO.getMemberId()))
				.collect(Collectors.toList());
		Map<Long, MemberDTO> memberMap = memberDTOList.stream()
				.collect(Collectors.toMap(MemberDTO::getId, memberDTO -> memberDTO));

		List<FriendPO> friendPOList = new ArrayList<>();
		PageVO<FriendPO> friendPOPageVO = new PageInfo<>(friendPOList).convertToPageVO();
		if (memberMap.size() > 0) {
			page.startPageHelper();
			friendPOList = selectFriendRelate(memberFriendQueryBO.getMemberId(), RelateVerifyEnum.SUCCESS,
					memberMap.keySet());
			friendPOPageVO = new PageInfo<>(friendPOList).convertToPageVO();
			log.info("Friend 關係 : {}", friendPOList);
		}
		List<MemberFriendVO> memberFriendVOList = new ArrayList<>();
		for (FriendPO friendPO : friendPOList) {
			MemberFriendVO memberFriendVO = new MemberFriendVO();
			MemberDTO member = memberMap.get(friendPO.getMemberSourceId().equals(memberFriendQueryBO.getMemberId())
					? friendPO.getMemberTargetId()
					: friendPO.getMemberSourceId());
			ReflectionUtils.copyFields(memberFriendVO, member, ReflectionUtils.STRING_TRIM_TO_NULL);

			memberFriendVO.setId(friendPO.getId());// 好友流水号
			if (StringUtil.isNoneEmpty(member.getAvatarUrl())) {
				memberFriendVO.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(member.getAvatarUrl())); // 头像
			}
			memberFriendVO.setMemberId(member.getId());// 好友会员流水号
			memberFriendVO.setAddTime(friendPO.getAddTime());// 成为好友时间
			memberFriendVO.setGender(member.getGender());// 性别
			memberFriendVO.setBlacked(friendPO.getSourceBlacked());// 黑名单
			memberFriendVOList.add(memberFriendVO);
		}
		PageVO<MemberFriendVO> pageVO = new PageInfo<>(memberFriendVOList).convertToPageVO();

		pageVO.setTotalPage(friendPOPageVO.getTotalPage());
		pageVO.setTotal(friendPOPageVO.getTotal());
		return pageVO;
	}

	public Boolean delete(Long id) {
		FriendPO friendPO = list(FriendQuery.builder().id(id).build()).stream().findFirst().orElse(null);
		Assert.notNull(friendPO, message.get(I18nAdmin.FRIEND_NOT_EXIST));
		List<MemberPO> memberPOList = memberService
				.selectMemberByIds(Arrays.asList(friendPO.getMemberSourceId(), friendPO.getMemberTargetId()));
		try {
			IMResult<Void> voidIMResult = RelationAdmin.setUserFriend(memberPOList.get(0).getUid(),
					memberPOList.get(1).getUid(), false, "");
			if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.code) {
				log.info("[IM][删除好友] 成功");

				// 用户列表-删除好友 log
				OperateLogList list = new OperateLogList();
				list.addLog("账号", memberPOList.get(1).getMemberName(), false);
				list.addLog("删除好友账号", memberPOList.get(0).getMemberName(), false);
				logService.addOperateLog("/admin/member/friend/delete/{id}", list);
				return true;
			}
			log.info("[IM][删除好友] 失败:{}", voidIMResult.getResult());
		} catch (Exception e) {
			log.info("[IM][删除好友] 失败");
			throw new RuntimeException(e);
		}
		return false;
	}
}