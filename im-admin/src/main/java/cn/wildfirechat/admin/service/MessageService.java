package cn.wildfirechat.admin.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.VideoInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.config.RedisConfig;
import cn.wildfirechat.admin.mapper.*;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.MemberMessageSendBO;
import cn.wildfirechat.common.model.bo.MessageCreateBO;
import cn.wildfirechat.common.model.bo.MessageSendBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.dto.MessageLastChatDTO;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.po.*;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.query.MessagePageQuery;
import cn.wildfirechat.common.model.query.MessageQuery;
import cn.wildfirechat.common.model.vo.MessagePageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Assert;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.*;
import cn.wildfirechat.pojos.Conversation;
import cn.wildfirechat.pojos.MessagePayload;
import cn.wildfirechat.pojos.SendMessageResult;
import cn.wildfirechat.proto.ProtoConstants;
import cn.wildfirechat.sdk.MessageAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService extends BaseService {
	@Resource
	private MessageMapper messageMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private AdminUserService adminUserService;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private AdminUserMapper adminUserMapper;

	@Resource
	private GroupMapper groupMapper;

	@Resource
	private ChatRoomMapper chatRoomMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	public List<MessagePO> list(MessageQuery query) {
		return messageMapper.list(query);
	}

	@Transactional
	public PageVO<MessagePageVO> page(MessagePageQuery query, Page page) {
		page.startPageHelper();
		List<MessagePO> messagePOList = messageMapper.page(query);
		PageVO<MessagePO> messagePOPageVO = new PageInfo<>(messagePOList).convertToPageVO();
		PageVO<MessagePageVO> messagePageVOPageVO = new PageVO<>();
		messagePageVOPageVO.setTotal(messagePOPageVO.getTotal());
		messagePageVOPageVO.setTotalPage(messagePOPageVO.getTotalPage());

		// 发送者会员id
		Set<Long> memberIdSet = messagePOList.stream()
				.filter(messagePO -> messagePO.getSenderRole().equals(EditorRoleEnum.MEMBER.getValue()))
				.map(MessagePO::getSenderId).collect(Collectors.toSet());
		// 接收者会员id
		memberIdSet.addAll(messagePOList.stream()
				.filter(messagePO -> messagePO.getMessageType().equals(MessageChatEnum.PRIVATE.getValue()))
				.map(MessagePO::getReceiverId).collect(Collectors.toSet()));

		// 发送者系统管理者id
		Set<Long> adminIdSet = messagePOList.stream()
				.filter(messagePO -> messagePO.getSenderRole().equals(EditorRoleEnum.ADMIN.getValue()))
				.map(MessagePO::getSenderId).collect(Collectors.toSet());

		// 接收者群组id
		Set<Long> groupIdSet = messagePOList.stream()
				.filter(messagePO -> messagePO.getChatType().equals(MessageChatEnum.GROUP.getValue()))
				.map(MessagePO::getReceiverId).collect(Collectors.toSet());

		Set<Long> chatRoomIdSet = messagePOList.stream()
				.filter(messagePO -> messagePO.getChatType().equals(MessageChatEnum.CHAT_ROOM.getValue()))
				.map(MessagePO::getReceiverId).collect(Collectors.toSet());

		List<MemberPO> memberPOList = memberService.selectMemberByIds(memberIdSet);
		List<AdminUserPO> adminUserPOList = adminUserService.selectAdminUserByIds(adminIdSet);
		List<GroupPO> groupPOList = groupService.selectGroupByIds(groupIdSet);
		List<ChatRoomPO> chatRoomPOList = chatRoomService.selectChatRoomByIds(chatRoomIdSet);

		List<MessagePageVO> messagePageVOList = new ArrayList<>();

		messagePOList.forEach(messagePO -> {
			MessagePageVO messagePageVO = new MessagePageVO();
			ReflectionUtils.copyFields(messagePageVO, messagePO, ReflectionUtils.STRING_TRIM_TO_NULL);
			String senderNickName = null;
			String senderAccount = null;
			String receiverNickName = null;
			String receiverAccount = null;

			if (messagePO.getSenderRole().equals(EditorRoleEnum.ADMIN.getValue())) {
				AdminUserPO sender = adminUserPOList.stream()
						.filter(adminUserPO -> adminUserPO.getId().equals(messagePO.getSenderId())).findFirst()
						.orElse(new AdminUserPO());
				senderNickName = sender.getNickname();
				senderAccount = sender.getUsername() + "(admin)";
			} else if (messagePO.getSenderRole().equals(EditorRoleEnum.MEMBER.getValue())) {
				MemberPO sender = memberPOList.stream()
						.filter(memberPO -> memberPO.getId().equals(messagePO.getSenderId())).findFirst()
						.orElse(new MemberPO());
				senderNickName = sender.getNickName();
				senderAccount = sender.getMemberName();
			}

			switch (MessageChatEnum.parse(messagePO.getChatType())) {
				case PRIVATE :
					MemberPO memberReceiver = memberPOList.stream()
							.filter(memberPO -> memberPO.getId().equals(messagePO.getReceiverId())).findFirst()
							.orElse(new MemberPO());
					receiverNickName = memberReceiver.getNickName();
					receiverAccount = memberReceiver.getMemberName();
					break;
				case GROUP :
					GroupPO groupReceiver = groupPOList.stream()
							.filter(groupPO -> groupPO.getId().equals(messagePO.getReceiverId())).findFirst()
							.orElse(new GroupPO());
					receiverNickName = groupReceiver.getName();
					receiverAccount = groupReceiver.getGid();
					break;
				case CHAT_ROOM :
					ChatRoomPO chatRoomReceiver = chatRoomPOList.stream()
							.filter(chatRoomPO -> chatRoomPO.getId().equals(messagePO.getReceiverId())).findFirst()
							.orElse(new ChatRoomPO());
					receiverNickName = chatRoomReceiver.getName();
					receiverAccount = chatRoomReceiver.getCid();
					break;
			}

			messagePageVO.setSenderNickname(senderNickName);
			messagePageVO.setSenderAccount(senderAccount);
			messagePageVO.setReceiverNickname(receiverNickName);
			messagePageVO.setReceiverAccount(receiverAccount);
			messagePageVO.setMessageTypeString(MessageTypeEnum.parse(messagePO.getMessageType()).getMessage());
			messagePageVO.setChatTypeString(MessageChatEnum.parse(messagePO.getChatType()).getMessage());
			if (StringUtil.isNotEmpty(messagePO.getFilePath())) {
				messagePageVO.setFileUrl(uploadFileUtils.parseFilePathToUrl(messagePO.getFilePath()));
			}
			messagePageVOList.add(messagePageVO);
		});
		messagePageVOPageVO.setPage(messagePageVOList);
		return messagePageVOPageVO;
	}

	@Transactional
	public Boolean create(MessageCreateBO messageCreateBO) {
		MessagePO messagePO = new MessagePO();
		ReflectionUtils.copyFields(messagePO, messageCreateBO, ReflectionUtils.STRING_TRIM_TO_NULL);
		MemberPO memberPO = memberService.selectMemberByIds(Collections.singleton(messageCreateBO.getReceiverId()))
				.stream().findFirst().orElse(null);
		Assert.notNull(memberPO, message.get(I18nAdmin.MEMBER_NOT_EXIST));

		Conversation conversation = new Conversation();
		conversation.setTarget(memberPO.getUid());
		conversation.setType(ProtoConstants.ConversationType.ConversationType_Private);
		MessagePayload payload = new MessagePayload();
		payload.setType(1);
		payload.setSearchableContent(messageCreateBO.getContent());

		try {
			IMResult<SendMessageResult> resultSendMessage = MessageAdmin.sendMessage("admin", conversation, payload);
			if (resultSendMessage != null && resultSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				messagePO.setMid(resultSendMessage.getResult().getMessageUid());
				messageMapper.insert(messagePO);
				log.info("send message success");

				// 用户列表-发送消息 log
				OperateLogList list = new OperateLogList();
				list.addLog("账号", memberPO.getMemberName(), false);
				list.addLog("消息内容", messageCreateBO.getContent(), false);
				logService.addOperateLog("/admin/member/sent/{id}", list);
				return true;
			} else {
				log.error("send message error {}",
						resultSendMessage != null ? resultSendMessage.getErrorCode().code : "unknown");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("send message error {}", e.getLocalizedMessage());
		}
		return false;
	}

	public Boolean update(MessagePO messagePO) {
		return messageMapper.update(messagePO) > 0;
	}

	public Boolean revert(MessagePO messagePO, String username) {
		messagePO.setIsRevert(true);
		messagePO.setMemo(String.format("%s撤回消息%s", username, DateUtils.format(new Date(), "YYYY-MM-dd HH:mm:ss")));
		return update(messagePO);
	}

	public Integer revert(Long memberId, Long memberId2, String username, Integer count) {
		if (count < 0) {
			count = null;
		}

		String memo = String.format("%s撤回消息%s", username, DateUtils.format(new Date(), "YYYY-MM-dd HH:mm:ss"));

		return messageMapper.revert(memberId, memberId2, memo, count);
	}

	public Integer delete(Long memberId, Long memberId2, String username, Integer count) {
		if (count < 0) {
			count = null;
		}

		String memo = String.format("%s删除消息%s", username, DateUtils.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
		return messageMapper.delete(memberId, memberId2, memo, count);
	}

	public void synMessage(MemberMessageSendBO bo) throws Exception {
		Long receiverId;
		String receiverAccountName;
		MessageChatEnum chatType;
		MessageContentTypeEnum messageContentTypeEnum = MessageContentTypeEnum.parse(bo.getContentType());
		if (bo.isChatRoom()) {
			ChatRoomPO chatRoomPO = chatRoomMapper.selectByCid(bo.getTarget());
			if (chatRoomPO != null) {
				receiverId = chatRoomPO.getId();
				receiverAccountName = chatRoomPO.getCid();
			} else {
				receiverId = 0L;
				receiverAccountName = "{无法识别聊天室}";
			}
			chatType = MessageChatEnum.CHAT_ROOM;
		} else if (bo.isGroup()) {
			GroupPO groupPO = groupMapper.selectByGid(bo.getTarget());
			if (groupPO != null) {
				receiverId = groupPO.getId();
				receiverAccountName = bo.getTarget();
			} else {
				receiverId = 0L;
				receiverAccountName = "{无法识别群组}";
			}
			chatType = MessageChatEnum.GROUP;
		} else if (bo.isMember()) {
			MemberPO toBO = memberMapper.selectByUid(bo.getTarget());
			if (toBO != null) {
				receiverId = toBO.getId();
				receiverAccountName = toBO.getMemberName();
			} else {
				receiverId = 0L;
				receiverAccountName = "{无法识别用户}";
			}
			chatType = MessageChatEnum.PRIVATE;
		} else {
			receiverId = 0L;
			receiverAccountName = "{无法识别}";
			chatType = MessageChatEnum.UNKNOWN;
		}

		MessageTypeEnum messageTypeEnum = MessagePO.getMessageTypeEnum(messageContentTypeEnum.getValue());
		MessagePO po;
		if (bo.getContentType().equals(MessageContentTypeEnum.RECALL.getValue())) {
			po = list(MessageQuery.builder().mid(bo.getMid()).build()).stream().findFirst().orElse(null);
			if (po != null) {
				String uid = bo.getMessage().getContent();
				MemberDTO memberDTO = memberService.list(MemberQuery.builder().uid(uid).build()).stream().findFirst()
						.orElse(new MemberDTO());
				String username = memberDTO.getMemberName();
				if (redisUtil.exists(RedisConfig.MESSAGE_REVERT_PREFIX + bo.getMid())) {
					username = (String) redisUtil.get(RedisConfig.MESSAGE_REVERT_PREFIX + bo.getMid());
					redisUtil.delete(RedisConfig.MESSAGE_REVERT_PREFIX + bo.getMid());
				}
				Date date = new Date(bo.getServerTimestamp());
				po.setMemo(String.format("%s【%s %s撤回消息】", po.getMemo(), username,
						DateUtils.format(date, "yyyy-MM-dd hh:mm:ss")));
				po.setIsRevert(true);
			}
		} else {
			MemberPO fromBO = memberMapper.selectByUid(bo.getFrom());
			Long senderId = 0L;
			String senderAccountName = "{无法识别}";
			int senderRole = MessageSenderRoleEnum.MEMBER.getValue();
			if (fromBO != null) {
				senderId = fromBO.getId();
				senderAccountName = fromBO.getMemberName();
			}

			po = MessagePO.builder().mid(bo.getMid()).senderAccountName(senderAccountName).receiverId(receiverId)
					.receiverAccountName(receiverAccountName).messageType(messageTypeEnum.getValue())
					.chatType(chatType.getValue()).isRevert(false)
					.isDeleted(MessageContentTypeEnum.DELETE.getValue() == bo.getContentType())
					.createTime(new Date(bo.getServerTimestamp())).build();
			log.info("synMessage po: {}", po);
			MemberMessageSendBO.Message message = bo.getMessage();
			if (message != null) {
				po.setContent(message.getSearchableContent());
				po.setData(new ObjectMapper().writeValueAsString(message));
				String filePath = "";
				if (StringUtil.isNotEmpty(message.getRemoteMediaUrl())) {
					filePath = message.getRemoteMediaUrl();
				}
				if (StringUtils.isNotEmpty(message.getExtra())) {
					log.info("synMessage extra: {}", message.getExtra());
					MemberMessageSendBO.Message.Extra extra = new ObjectMapper().readValue(message.getExtra(),
							MemberMessageSendBO.Message.Extra.class);
					if (StringUtil.isNotEmpty(extra.getFileUrl())) {
						filePath = extra.getFileUrl();
					}
					if (extra.getSenderRole().equals(EditorRoleEnum.ADMIN.getValue())) {
						po.setSenderRole(EditorRoleEnum.ADMIN.getValue());

						AdminUserPO adminUserPO = adminUserMapper.selectByMemberId(fromBO.getId());
						if (adminUserPO != null) {
							senderId = adminUserPO.getId();
							senderAccountName = adminUserPO.getUsername();
							senderRole = MessageSenderRoleEnum.ADMIN.getValue();
						}
					}
				}

				po.setSenderId(senderId);
				po.setSenderRole(senderRole);
				po.setSenderAccountName(senderAccountName);

				log.info("synMessage filePath: {}", filePath);
				po.setFilePath(filePath);
				po.setMemo(po.covertMemo(messageContentTypeEnum, message.getContent()));
			}
		}

		messageMapper.insertUpdate(po);
	}

	public List<MessageLastChatDTO> lastChat(MessageQuery query) {
		List<MessageLastChatDTO> dtos = new ArrayList<>();
		List<MessagePO> messagePOs = messageMapper.lastChats(query);
		if (messagePOs.size() > 0) {
			// 发送者会员id
			Set<Long> memberIdSet = messagePOs.stream()
					.filter(messagePO -> messagePO.getSenderRole().equals(EditorRoleEnum.MEMBER.getValue()))
					.map(MessagePO::getSenderId).collect(Collectors.toSet());
			// 发送者系统管理者id
			Set<Long> adminIdSet = messagePOs.stream()
					.filter(messagePO -> messagePO.getSenderRole().equals(EditorRoleEnum.ADMIN.getValue()))
					.map(MessagePO::getSenderId).collect(Collectors.toSet());

			List<MemberPO> memberPOList = memberService.selectMemberByIds(memberIdSet);
			List<AdminUserPO> adminUserPOList = adminUserService.selectAdminUserByIds(adminIdSet);

			messagePOs.forEach(messagePO -> {
				MessageLastChatDTO dto = MessageLastChatDTO.builder().build();
				ReflectionUtils.copyFields(dto, messagePO, ReflectionUtils.STRING_TRIM_TO_NULL);
				if (StringUtils.isNotEmpty(messagePO.getFilePath())) {
					dto.setFilePath(uploadFileUtils.parseFilePathToUrl(messagePO.getFilePath()));
				}
				if (messagePO.getSenderRole().equals(EditorRoleEnum.ADMIN.getValue())) {
					AdminUserPO sender = adminUserPOList.stream()
							.filter(adminUserPO -> adminUserPO.getId().equals(messagePO.getSenderId())).findFirst()
							.orElse(new AdminUserPO());
					dto.setSenderNickName(sender != null ? sender.getNickname() : null);
				} else if (messagePO.getSenderRole().equals(EditorRoleEnum.MEMBER.getValue())) {
					MemberPO sender = memberPOList.stream()
							.filter(memberPO -> memberPO.getId().equals(messagePO.getSenderId())).findFirst()
							.orElse(new MemberPO());
					dto.setSenderNickName(sender != null ? sender.getNickName() : null);
				}
				dtos.add(dto);
			});
		}
		return dtos;
	}

	public Boolean sendMessage(MessageSendBO bo) throws Exception {
		ObjectMapper objMapper = new ObjectMapper();
		Conversation conversation = new Conversation();
		conversation.setType(ProtoConstants.ConversationType.ConversationType_Group);
		conversation.setTarget(bo.getGid());

		MessageTypeEnum messageTypeEnum = MessageTypeEnum.parse(bo.getMessageType());
		MessagePayload payload = new MessagePayload();
		payload.setType(MessageTypeEnum.convertContentType(messageTypeEnum));
		payload.setSearchableContent(bo.getText());
		payload.setMediaType(MessageTypeEnum.convert(messageTypeEnum));

		payload.setPushContent("管理端登录请求");
		payload.setPushData("");

		payload.setMentionedType(0);

		MemberMessageSendBO.Message.Extra dto = MemberMessageSendBO.Message.Extra.builder()
				.senderRole(EditorRoleEnum.ADMIN.getValue()).groupType(GroupTypeEnum.BROADCAST.getValue()).build();

		if (bo.getUploadFile() != null) {
			if (!bo.getUploadFile().isEmpty()) {
				String mediaType = Arrays.stream(bo.getUploadFile().getContentType().split("/")).findFirst().orElse("");

				log.info("contentType: {}, MediaType.MP4_VIDEO: {}", mediaType, MediaType.ANY_IMAGE_TYPE.type());

				if (Objects.equals(mediaType, MediaType.MP4_VIDEO.type())) {
					VideoInfoDTO videoInfo = VideoUtils.getVideoInfo(bo.getUploadFile());
					long duration = videoInfo.getDuration();
					long fileSize = videoInfo.getFileSize();

					payload.setContent("{\"d\":" + duration + ",\"duration\":" + duration + "}");
					payload.setBase64edData(videoInfo.getThumb());
					payload.setSearchableContent("[视频]");
					log.info("UploadFIle duration: {}秒, fileSize: {}, thumb: {}", duration / 1000, fileSize,
							payload.getBase64edData());
				} else if (Objects.equals(mediaType, MediaType.ANY_IMAGE_TYPE.type())) {
					payload.setSearchableContent("[图片]");
					payload.setBase64edData(ImageUtils.convertToBase64(ImageUtils.zoomImage(bo.getUploadFile(), 0.5f),
							ImageUtils.IMAGE_JPG));
				}
			}
			dto.setFileUrl(uploadFileUtils.uploadFile(bo.getUploadFile(), FileUtils.GROUP_MESSAGE_PATH, FileNameUtils.GROUP_MESSAGE_PREFIX));
			payload.setRemoteMediaUrl(dto.getFileUrl());
		}
		payload.setExtra(objMapper.writeValueAsString(dto));
		log.info("sendMessage extra: {}", payload.getExtra());

		IMResult<SendMessageResult> imResult = MessageAdmin.sendMessage(SpringSecurityUtil.getChatUID(), conversation,
				payload);
		if (imResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
			return true;
		} else {
			return false;
		}
	}

	public void sendMessageToIM(String fromUser, String toUser, String text) {
		sendMessageToIM(fromUser, toUser, text, ProtoConstants.ConversationType.ConversationType_Private,
				MessageChatEnum.PRIVATE);
	}

	public void sendSystemMessageToIM(String fromUser, String toUser, MessageChatEnum messageChatEnum) {
		sendMessageToIM(fromUser, toUser, "", ProtoConstants.ConversationType.ConversationType_Private,
				messageChatEnum);
	}

	public void sendMessageToIM(String fromUser, String toUser, String text, int conversationType,
			MessageChatEnum messageChatEnum) {
		Conversation conversation = new Conversation();
		conversation.setTarget(toUser);
		conversation.setType(conversationType);
		MessagePayload payload = new MessagePayload();
		payload.setType(messageChatEnum.getValue());
		payload.setSearchableContent(text);

		try {
			IMResult<SendMessageResult> resultSendMessage = MessageAdmin.sendMessage(fromUser, conversation, payload);
			if (resultSendMessage != null && resultSendMessage.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				log.info("send message success");
			} else {
				log.error("send message error {}",
						resultSendMessage != null ? resultSendMessage.getErrorCode().code : "unknown");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("send message error {}", e.getLocalizedMessage());
		}

	}
	
	public List<MessagePO> getDeletedList (){
		List<MessagePO> messagePOList =  messageMapper.selectDeleted();
		return messagePOList;
	}
	
}
