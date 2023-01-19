package cn.wildfirechat.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.mapper.*;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.SystemOperateLogDTO;
import cn.wildfirechat.common.model.po.*;
import cn.wildfirechat.common.model.query.SystemOperateLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.SensitiveWordAddBO;
import cn.wildfirechat.common.model.bo.SensitiveWordHitCreateBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.dto.SensitiveWordHitDTO;
import cn.wildfirechat.common.model.enums.MessageChatEnum;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.query.SensitiveWordQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.SensitiveWordHitVO;
import cn.wildfirechat.common.model.vo.SensitiveWordVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.sdk.SensitiveAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SensitiveWordService {

	@Resource
	private SensitiveWordMapper sensitiveWordMapper;

	@Resource
	private SensitiveWordHitMapper sensitiveWordHitMapper;

	@Resource
	protected SpringMessage message;

	@Resource
	private AdminUserMapper adminUserMapper;

	@Resource
	SystemOperateLogMapper systemOperateLogMapper;

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private ChatRoomMapper chatRoomMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private GroupService groupService;

	@Resource
	protected LogService logService;

	@Transactional(readOnly = true)
	public PageVO<SensitiveWordVO> swPage(SensitiveWordQuery query, Page page) {
		page.startPageHelper();
		List<SensitiveWordPO> list = sensitiveWordMapper.find(query);
		PageVO<SensitiveWordPO> sensitiveWordPOPageVO = new PageInfo<>(list).convertToPageVO();

		List<SensitiveWordVO> vos = new ArrayList<>();

		list.forEach(po -> {
			SensitiveWordVO vo = SensitiveWordVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			vos.add(vo);
		});

		PageVO<SensitiveWordVO> pageVO = new PageInfo<>(vos).convertToPageVO();
		pageVO.setTotal(sensitiveWordPOPageVO.getTotal());
		pageVO.setTotalPage(sensitiveWordPOPageVO.getTotalPage());
		return pageVO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void swInsertBatch(List<SensitiveWordAddBO> list) {
		// 检查是否重复
		String allContent = list.stream().map(SensitiveWordAddBO::getContent).collect(Collectors.joining(", "));
		long count = list.stream().map(SensitiveWordAddBO::getContent).distinct().count();
		boolean isRepeat = count < list.size();
		log.info("添加敏感词为:{}, 总数:{}, 去重複个数为:{}, 其中是否重复:{}", allContent, list.size(), count, isRepeat);
		Assert.isTrue(!isRepeat, message.get(I18nAdmin.SENSITIVE_WORD_REPEAT));

		List<SensitiveWordAddBO> updateList = new ArrayList<>();
		list.forEach(bo -> {
			List<SensitiveWordPO> swList = sensitiveWordMapper
					.find(SensitiveWordQuery.builder().content(bo.getContent()).build());
			if (swList.size() == 0) {
				updateList.add(bo);
			}
		});

		if (updateList.size() > 0) {
			try {
				IMResult<Void> voidIMResult = SensitiveAdmin.addSensitives(
						updateList.stream().map(SensitiveWordAddBO::getContent).collect(Collectors.toList()));
				if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
					List<SensitiveWordPO> poList = new ArrayList<>();
					updateList.forEach(bo -> {
						SensitiveWordPO po = SensitiveWordPO.builder().content(bo.getContent())
								.creator(SpringSecurityUtil.getPrincipal()).updater(SpringSecurityUtil.getPrincipal())
								.build();
						poList.add(po);
					});
					// 新增敏感词 log
					OperateLogList operateLogList = new OperateLogList();
					poList.forEach(p -> {
						operateLogList.addLog("敏感词内容", p.getContent(), false);
					});
					sensitiveWordMapper.insertBatch(poList);
					logService.addOperateLog("/admin/sensitiveWord/swCreate", operateLogList);
				}
			} catch (Exception e) {
				log.error("[IM][同步新增敏感词]失敗", e);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void swDeleteBatch(List<Long> list) {
		List<SensitiveWordPO> sensitiveWordPOList = findSWExist(list);
		try {
			sensitiveWordMapper.deleteBatch(list);
			IMResult<Void> voidIMResult = SensitiveAdmin.removeSensitives(
					sensitiveWordPOList.stream().map(SensitiveWordPO::getContent).collect(Collectors.toList()));
			if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
				// 删除敏感词 log
				OperateLogList operateLogList = new OperateLogList();
				String content = sensitiveWordPOList.get(0).getContent();
				operateLogList.addLog("敏感词内容", content, false);
				logService.addOperateLog("/admin/sensitiveWord/swDelete", operateLogList);
				log.info("[IM][敏感词] 删除敏感词成功");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			log.error("[IM][敏感词] 删除敏感词失败", e);
		}
	}

	@Transactional(readOnly = true)
	public PageVO<SensitiveWordHitVO> swHitPage(String memberName, Page page) {
		page.startPageHelper();
		if (StringUtils.isEmpty(memberName)) {
			memberName = null;
		}
		List<SensitiveWordHitDTO> list = sensitiveWordHitMapper.find(memberName);
		PageVO<SensitiveWordHitDTO> sensitiveWordHitDTOPageVO = new PageInfo<>(list).convertToPageVO();

		Set<Long> memberIdSet = list.stream().map(SensitiveWordHitDTO::getSenderId).collect(Collectors.toSet());
		memberIdSet.addAll(list.stream().filter(item -> item.getChatType().equals(MessageChatEnum.PRIVATE.getValue()))
				.map(SensitiveWordHitDTO::getReceiverId).collect(Collectors.toSet()));

		Set<Long> groupIdSet = list.stream().filter(item -> item.getChatType().equals(MessageChatEnum.GROUP.getValue()))
				.map(SensitiveWordHitDTO::getReceiverId).collect(Collectors.toSet());

		Set<Long> chatroomIdSet = list.stream().filter(item -> item.getChatType().equals(MessageChatEnum.CHAT_ROOM.getValue()))
				.map(SensitiveWordHitDTO::getReceiverId).collect(Collectors.toSet());

		Map<Long, MemberPO> memberPOMap = memberService.selectMemberByIds(memberIdSet).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));
		Map<Long, GroupPO> groupPOMap = groupService.selectGroupByIds(groupIdSet).stream()
				.collect(Collectors.toMap(GroupPO::getId, groupPO -> groupPO));

		Map<Long,ChatRoomPO > chatroomPOMap = chatRoomService.selectChatRoomByIds(chatroomIdSet).stream()
				.collect(Collectors.toMap(ChatRoomPO::getId, chatRoomPO -> chatRoomPO));

		List<SensitiveWordHitVO> vos = new ArrayList<>();
		list.forEach(po -> {
			SensitiveWordHitVO vo = SensitiveWordHitVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);

			MemberPO sender = memberPOMap.get(po.getSenderId());
			vo.setSenderNickName(sender != null ? sender.getNickName() : "");

			if (po.getChatType().equals(MessageChatEnum.PRIVATE.getValue())) {
				MemberPO memberReceiver = memberPOMap.get(po.getReceiverId());
				vo.setReceiverNickName(memberReceiver != null ? memberReceiver.getNickName() : "");
			} else if (po.getChatType().equals(MessageChatEnum.GROUP.getValue())) {
				GroupPO groupReceiver = groupPOMap.get(po.getReceiverId());
				vo.setReceiverNickName(groupReceiver != null ? groupReceiver.getName() : "");

			} else if (po.getChatType().equals(MessageChatEnum.CHAT_ROOM.getValue())){
				ChatRoomPO chatRoomPOReceiver =  chatroomPOMap.get(po.getReceiverId());
				vo.setReceiverNickName(chatRoomPOReceiver != null ? chatRoomPOReceiver.getName() : "");
			}
			vos.add(vo);
		});

		PageVO<SensitiveWordHitVO> pageVO = new PageInfo<>(vos).convertToPageVO();
		pageVO.setTotal(sensitiveWordHitDTOPageVO.getTotal());
		pageVO.setTotalPage(sensitiveWordHitDTOPageVO.getTotalPage());
		return pageVO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void swHitDelete(List<Long> ids) {
		try {
			List<SensitiveWordHitDTO> list = sensitiveWordHitMapper.selectByIds(ids);
			int response = sensitiveWordHitMapper.deleteBatch(ids);
			// 删除敏感词命中 log
			if (response >= 1) {
				list.forEach(l -> {
					OperateLogList operateLogList = new OperateLogList();
					operateLogList.addLog("敏感词命中内容", l.getContent(), false);
					logService.addOperateLog("/admin/sensitiveWord/swHitDelete", operateLogList);
				});
			}
		} catch (Exception e) {
			log.info("敏感词命中内容删除失败 : {}", e);
		}
	}
	@Transactional(rollbackFor = Exception.class)
	public void swHitDeleteAll() {
		try {
			int response = sensitiveWordHitMapper.deleteAll();
			// 清空敏感词命中 log
			if (response >= 1) {
				OperateLogList operateLogList = new OperateLogList();
				operateLogList.addLog("清空敏感词命中", "操作成功", false);
				logService.addOperateLog("/admin/sensitiveWord/swHitDeleteAll", operateLogList);
			}
		} catch (Exception e) {
			log.info("清空敏感词命中失败 : {}", e);
		}
	}

	public List<SensitiveWordPO> findSWExist(List<Long> idList) {
		return idList.size() > 0 ? sensitiveWordMapper.isExistById(idList) : new ArrayList<>();
	}

	public void syncSWHit(SensitiveWordHitCreateBO bo) {
		MemberDTO senderMember = memberService.list(MemberQuery.builder().uid(bo.getSenderUid()).build()).stream()
				.findFirst().orElse(MemberDTO.builder().uid("{无法识别}").id(0L).build());
		SensitiveWordHitPO sensitiveWordHitPO = SensitiveWordHitPO.builder().build();
		sensitiveWordHitPO.setContent(bo.getContent());
		sensitiveWordHitPO.setSenderId(senderMember.getId());
		sensitiveWordHitPO.setSender(senderMember.getMemberName());
		sensitiveWordHitPO.setCreator(senderMember.getMemberName());
		if (bo.isGroup()) {
			GroupPO groupPO = groupService.list(GroupQuery.builder().gid(bo.getReceiverUid()).build()).stream()
					.findFirst().orElse(GroupPO.builder().gid("{无法识别}").id(0L).build());

			sensitiveWordHitPO.setChatType(MessageChatEnum.GROUP.getValue());

			sensitiveWordHitPO.setReceiverId(groupPO.getId());
			sensitiveWordHitPO.setReceiver(groupPO.getGid());
		} else if (bo.isMember()) {
			MemberDTO receiverMember = memberService.list(MemberQuery.builder().uid(bo.getReceiverUid()).build())
					.stream().findFirst().orElse(MemberDTO.builder().uid("{无法识别}").id(0L).build());

			sensitiveWordHitPO.setChatType(MessageChatEnum.PRIVATE.getValue());

			sensitiveWordHitPO.setReceiverId(receiverMember.getId());
			sensitiveWordHitPO.setReceiver(receiverMember.getMemberName());
		} else if(bo.isChatroom()){
			ChatRoomPO chatRoomPO = chatRoomMapper.selectByCid(bo.getReceiverUid());
			sensitiveWordHitPO.setChatType(MessageChatEnum.CHAT_ROOM.getValue());
			sensitiveWordHitPO.setReceiverId(chatRoomPO.getId());
			sensitiveWordHitPO.setReceiver(chatRoomPO.getCid());
		}
		sensitiveWordHitMapper.insert(sensitiveWordHitPO);
	}

}
