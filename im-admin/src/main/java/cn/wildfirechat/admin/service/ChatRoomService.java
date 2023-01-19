package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.mapper.ChatRoomMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.vo.MemberChatroomVO;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.model.bo.ChatRoomCreateBo;
import cn.wildfirechat.common.model.bo.ChatRoomQueryOnlineMemberBo;
import cn.wildfirechat.common.model.enums.StatusBasicEnum;
import cn.wildfirechat.common.model.po.ChatRoomPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.ChatRoomQuery;
import cn.wildfirechat.common.model.vo.ChatRoomVO;
import cn.wildfirechat.common.model.vo.MemberVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.StringUtil;
import cn.wildfirechat.pojos.OutputCreateChatroom;
import cn.wildfirechat.pojos.OutputStringList;
import cn.wildfirechat.sdk.ChatroomAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatRoomService extends BaseService {

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private ChatRoomMapper chatRoomMapper;

	@Resource
	private UploadFileUtils uploadFileUtils;

	@Autowired
	private RedisUtil redisUtil;

	@Transactional(readOnly = true)
	public PageVO<ChatRoomVO> page(ChatRoomQuery query, Page page) {
		page.startPageHelper();
		List<ChatRoomPO> list = chatRoomMapper.list(query);
		List<ChatRoomVO> voList = new ArrayList<>();
		PageVO<ChatRoomPO> ChatRoomPOPageVO = new PageInfo<>(list).convertToPageVO();
		list.forEach(po -> {
			ChatRoomVO vo = ChatRoomVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			if (StringUtil.isNoneEmpty(vo.getImage())) {
				vo.setImage(uploadFileUtils.parseFilePathToUrl(vo.getImage()));
			}
			vo.setOnlineCount(onlineMemberCount(po.getCid()));
			voList.add(vo);
		});
		PageVO<ChatRoomVO> chatRoomVOPageVO = new PageInfo<>(voList).convertToPageVO();
		chatRoomVOPageVO.setTotal(ChatRoomPOPageVO.getTotal());
		chatRoomVOPageVO.setTotalPage(ChatRoomPOPageVO.getTotalPage());
		return chatRoomVOPageVO;
	}

	public boolean createChatroom(ChatRoomCreateBo bo) {
		boolean result = false;
		ChatRoomPO cp = ChatRoomPO.builder()
				.name(bo.getName())
				.sort(bo.getSort())
				.image(bo.getImage())
				.status(StatusBasicEnum.OPEN.getValue())
				.chatStatus(StatusBasicEnum.OPEN.getValue())
				.build();
		int insertResult = chatRoomMapper.insert(cp);

		if(insertResult > 0){
			log.info("同步IM server创建聊天室 ChatRoomPO:{}", ReflectionToStringBuilder.toString(cp));
			String chatroomId = createIMChatroom(cp);//同步IM
			if(StringUtil.isNotBlank(chatroomId)){
				cp.setCid(chatroomId);
				chatRoomMapper.update(cp);
				// 聊天室列表-创建 log
				OperateLogList list = new OperateLogList();
				list.addLog("聊天室名称",bo.getName(),false);
				list.addLog("顺序",bo.getSort(),false);
				logService.addOperateLog("/admin/chatroom/create",list);
				result = true;
			}

		}
		return result;
	}
	public String createIMChatroom(ChatRoomPO po){
		IMResult<OutputCreateChatroom> chatroomResult = null; // 同步IM-Server , 创建聊天室
		String cid = null;
		try {
			// 聊天室状态 使用0 or null 会被设置为正常启用 0 : 正常启用 ; 1 : 停用 ; 2 : END
			chatroomResult = ChatroomAdmin.createChatroom(null, po.getName(), po.getDesc(), po.getImage(), po.getExtra(), 0);
			if (chatroomResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				cid = chatroomResult.getResult().getChatroomId();
			} else {
				log.info("同步IM server创建聊天室失败: code :{} , message :{}", chatroomResult.getCode(), chatroomResult.getMsg());
			}
		} catch(Exception e) {
			log.error("IM server创建聊天室失败: {}", e.getMessage(), e);
		}
		return cid;
	}

	public boolean destroyChatroom(Long id, String cid) {
		ChatRoomPO cp = ChatRoomPO.builder()
				.id(id)
				.status(StatusBasicEnum.CLOSE.getValue())
				.build();
		int insertResult = chatRoomMapper.update(cp);

		if(insertResult > 0){
			ChatRoomPO chatRoomPO = chatRoomMapper.selectById(id);
			log.info("同步IM server解散聊天室 chatRoomID:{}", cid);
			//聊天室列表-解散log
			OperateLogList list = new OperateLogList();
			list.addLog("聊天室ID",cid,false);
			list.addLog("聊天室名称",chatRoomPO.getName(),false);
			list.addDiffLog("聊天室状态","正常","解散",false);

			logService.addOperateLog("/admin/chatroom/destroy",list);

			return destroyIMChatroom(cid);//同步IM
		}
		return false;
	}

	public boolean destroyIMChatroom(String cid){
		IMResult<Void> chatroomResult = null; // 同步IM-Server , 创建聊天室
		try {
			chatroomResult = ChatroomAdmin.destroyChatroom(cid);
			if (chatroomResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				return true;
			} else {
				log.info("同步IM server解散聊天室失败: code :{} , message :{}", chatroomResult.getCode(), chatroomResult.getMsg());
			}
		} catch(Exception e) {
			log.error("IM server解散聊天室失败: {}", e.getMessage(), e);
		}
		return false;
	}

	public List<MemberChatroomVO> onlineMember(ChatRoomQueryOnlineMemberBo bo) {
		log.info("向IM server查看聊天室在线用户 bo:{}", bo);
		IMResult<OutputStringList> chatroomResult = null;
		List<String> uids = null;
		try {
			chatroomResult = ChatroomAdmin.getChatroomMembers(bo.getCid());
			if (chatroomResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				uids = chatroomResult.getResult().getList();
			} else {
				log.info("向IM server查看聊天室在线用户: code :{} , message :{}", chatroomResult.getCode(), chatroomResult.getMsg());
			}

			List<MemberChatroomVO> memberVOS = new ArrayList<>();
			if(!CollectionUtils.isEmpty(uids)){
				memberVOS = uids.stream().map(uid->{
					MemberPO memberPO = memberMapper.selectByUid(uid);
					MemberChatroomVO vo = MemberChatroomVO.builder().build();
					ReflectionUtils.copyFields(vo, memberPO, ReflectionUtils.STRING_TRIM_TO_NULL);
					String avatarUrl = StringUtils.isNotBlank(memberPO.getAvatarUrl())?uploadFileUtils.parseFilePathToUrl(memberPO.getAvatarUrl()) :"";
					vo.setAvatarUrl(avatarUrl);

					vo.setCid(bo.getCid());
					StringBuilder sb = new StringBuilder().append("chatroomId_").append(bo.getCid()).append(":").append("uid_").append(uid);
					if(redisUtil.exists(sb.toString())){
						String operationTime = (String) redisUtil.get(sb.toString());
						vo.setCreateTime(new Date(Long.valueOf(operationTime)));
					}

					return vo;
				}).collect(Collectors.toList());

				if(StringUtils.isNotBlank(bo.getNickNameOrMemberName())){
					memberVOS = memberVOS.stream()
							.filter(vo -> bo.getNickNameOrMemberName().equals(vo.getNickName()) || bo.getNickNameOrMemberName().equals(vo.getMemberName()))
							.collect(Collectors.toList());
				}
			}

			return memberVOS;
		} catch(Exception e) {
			log.error("向IM server查看聊天室在线用户: {}", e.getMessage(), e);
		}

		return null;
	}

	public boolean updateSort(Long id, String cid, Integer sort) {
		ChatRoomPO cp = ChatRoomPO.builder()
				.id(id)
				.sort(sort)
				.build();
		int insertResult = chatRoomMapper.update(cp);
		return insertResult >0;
	}

	public Integer onlineMemberCount(String cid) {
		IMResult<OutputStringList> chatroomResult = null;
		List<String> uids = null;
		try {
			chatroomResult = ChatroomAdmin.getChatroomMembers(cid);
			if (chatroomResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				uids = chatroomResult.getResult().getList();
				if(!CollectionUtils.isEmpty(uids)){
					return uids.size();
				}
			} else {
				log.info("向IM server查看聊天室在线用户: code :{} , message :{}", chatroomResult.getCode(), chatroomResult.getMsg());
			}
		} catch(Exception e) {
			log.error("向IM server查看聊天室在线用户数: {}", e.getMessage(), e);
		}
		return 0;
	}

	public List<ChatRoomPO> selectChatRoomByIds(Collection<Long> idList) {
		return idList.size() > 0 ? chatRoomMapper.selectChatRoomByIds(idList) : new ArrayList<>();
	}

	public List<ChatRoomPO> select(ChatRoomQuery query) {
		return chatRoomMapper.list(query);
	}
}
