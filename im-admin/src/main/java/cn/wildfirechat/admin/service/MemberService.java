package cn.wildfirechat.admin.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import javax.annotation.Resource;

import cn.wildfirechat.admin.utils.TextUtils;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.*;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.admin.mapper.MemberPasswordMapper;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.i18n.I18nBase;
import cn.wildfirechat.common.model.bo.MemberAddBO;
import cn.wildfirechat.common.model.bo.MemberBatchAddBO;
import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.po.DefaultMemberPO;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.po.MemberPasswordPO;
import cn.wildfirechat.common.model.query.DefaultMemberQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.query.MemberPasswordQuery;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.vo.MemberVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.IpTools;
import cn.wildfirechat.common.utils.StringUtil;
import cn.wildfirechat.pojos.InputOutputUserInfo;
import cn.wildfirechat.pojos.OutputCreateUser;
import cn.wildfirechat.pojos.PojoGroupMember;
import cn.wildfirechat.sdk.GroupAdmin;
import cn.wildfirechat.sdk.RelationAdmin;
import cn.wildfirechat.sdk.UserAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService extends BaseService {

	public static final String ALGORITHM_NAME = "SHA-1";

	@Resource
	private MemberMapper memberMapper;
	@Resource
	private FriendService friendService;

	@Resource
	private MemberPasswordMapper memberPasswordMapper;

	@Resource
	private MemberPasswordService memberPasswordService;

	@Resource
	private DefaultMemberService defaultMemberService;

	@Resource
	private GroupService groupService;

	@Resource
	private MessageService messageService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	@Transactional(readOnly = true)
	public PageVO<MemberVO> page(MemberQuery query, Page page) {
		page.startPageHelper();
		List<MemberVO> voList = new ArrayList<>();
		List<MemberPO> list = memberMapper.list(query);
		PageVO<MemberPO> memberPOPageVO = new PageInfo<>(list).convertToPageVO();
		list.forEach(po -> {
			MemberVO vo = MemberVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			if (StringUtil.isNoneEmpty(vo.getAvatarUrl())) {
				vo.setAvatarUrl(uploadFileUtils.parseFilePathToUrl(vo.getAvatarUrl()));
			}
			vo.setBalance(po.getBalance() == null ? "0" : String.format("%.2f", po.getBalance()));
			vo.setFreeze(po.getFreeze() == null ? "0" : String.format("%.2f", po.getFreeze()));
			voList.add(vo);
		});
		PageVO<MemberVO> memberVOPageVO = new PageInfo<>(voList).convertToPageVO();
		memberVOPageVO.setTotal(memberPOPageVO.getTotal());
		memberVOPageVO.setTotalPage(memberPOPageVO.getTotalPage());
		return memberVOPageVO;
	}

	public List<MemberDTO> list(MemberQuery query) {
		List<MemberDTO> dtos = new ArrayList<>();
		List<MemberPO> list = memberMapper.list(query);
		list.forEach(po -> {
			MemberDTO dto = MemberDTO.builder().build();
			ReflectionUtils.copyFields(dto, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			dtos.add(dto);
		});
		return dtos;
	}

	@Transactional(rollbackFor = Exception.class)
	public Long add(MemberAddBO bo) {
		Assert.notNull(bo.getPassword(), message.get(I18nBase.PARAM_NULL, "password"));
		Assert.notNull(bo.getPhone(), message.get(I18nBase.PARAM_NULL, "phone"));

		// 检查电话是否重复
		isPhoneExist(null, bo.getPhone());
		// 检查帐号是否重复
		if (bo.getMemberName() != null) {
			isMemberNameExist(null, bo.getMemberName());
		}

		MemberPO po = MemberPO.builder().build();
		ReflectionUtils.copyFields(po, bo, ReflectionUtils.STRING_TRIM_TO_NULL);
		po.setMemberName(bo.getMemberName() == null ? MemberPO.createMemberName() : bo.getMemberName());
		po.setRegisterIp(IpTools.LOCAL_IP);
		po.setRegisterArea(IpTools.LOCAL_AREA);
		po.setLoginEnable(true);
		po.setAddFriendEnable(true);
		po.setCreateGroupEnable(true);
		po.setChannel(bo.getChannel());

		if (bo.getNickName() == null) {
			po.setNickName(po.getMemberName());
		}
		memberMapper.inserts(Collections.singletonList(po));

		String nickName = bo.getNickName() == null ? po.getMemberName() : bo.getNickName();
		// 调用IM sdk注册member,成功后再给予uid,密码
		Boolean IMResult = registerIMMember(po.getId(), bo.getPassword(), po.getMemberName(), nickName, bo.getPhone(), bo.getAvatarUrl());

		// 用户列表-新增 log
		if (IMResult.equals(Boolean.TRUE)) {
			OperateLogList list = new OperateLogList();
			list.addLog("账号", po.getMemberName(), false);
			list.addLog("手机号", po.getPhone(), false);
			list.addLog("昵称", po.getNickName(), false);
			list.addLog("邮箱", po.getEmail(), false);
			list.addLog("性别", MemberGenderEnum.parse(po.getGender()).getMessage(), false);
			list.addLog("备注", po.getMemo(), false);
			logService.addOperateLog("/admin/member/add", list);
		}
		return po.getId();

		// redis记录首页相关资讯,除非有实时需求,否则改用DB查询就好
		// redisUtil.increment(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY, 1);
		// redisUtil.setExpireAt(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY,
		// DateUtils.getTailDay(new Date()));
	}

	@Transactional(rollbackFor = Exception.class)
	@Deprecated
	public void addBatch(MemberBatchAddBO bo) throws Exception {
		List<MemberPO> pos = new ArrayList<>();
		for (int i = 0; i < bo.getBatchCount(); i++) {
			MemberPO po = MemberPO.builder().build();
			ReflectionUtils.copyFields(po, bo, ReflectionUtils.STRING_TRIM_TO_NULL);
			po.setMemberName(MemberPO.createMemberName());
			po.setNickName(bo.getNickNameType() == MemberNickNameTypeEnum.DEFINITION ? bo.getNickName() : "百家姓");
			po.setAccountType(bo.getAccountType().getValue());
			po.setAvatarUrl(bo.getAvatarType() == MemberAvatarTypeEnum.PRESET
					? "/static/image/avatar/df.png"
					: "/static/image/avatar/random.png");
			po.setRegisterIp(IpTools.LOCAL_IP);
			po.setRegisterArea(IpTools.LOCAL_AREA);
			po.setChannel(bo.getChannel());
			pos.add(po);
		}

		if (pos.size() > 0) {
			memberMapper.inserts(pos);

			for (MemberPO po : pos) {
				// TODO 视情况以多线程方式调用
				// 调用IM sdk注册member,成功后再给予uid,密码
				registerIMMember(po.getId(), bo.getLoginPwd(), po.getMemberName(), po.getNickName(), po.getPhone(), null);
			}

		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(MemberPO memberPO, String password) throws Exception{
		// 检查电话是否重复
		isPhoneExist(memberPO.getId(), memberPO.getPhone());

		if (StringUtil.isNotBlank(memberPO.getTradePwd())) {
			memberPO.setTradePwd(encryptTradePwd(memberPO.getTradePwd(), MemberPO.MEMBER_TRADE_PWD_SALT));
//			memberPO.setTradePwd(PasswordUtil.encode(memberPO.getTradePwd(), ConstDigest.SALT, 1));
		}

		log.info("更新頭像: {}", memberPO.getAvatarUrl());
		MemberPO mEntity = memberMapper.list(MemberQuery.builder().id(memberPO.getId()).build()).get(0);
		memberMapper.update(memberPO);

		MemberPasswordPO mpEntity = memberPasswordMapper.find(MemberPasswordQuery.builder().uid(mEntity.getUid()).build());

		// 修改登入密码
		if (StringUtil.isNotBlank(password)) {
			memberPasswordService.changePassword(MemberPasswordPO.builder().uid(mEntity.getUid()).build(), password);
		}

		// 同步IM Server
		Boolean IMResult = syncUpdateMemberInfo(memberPO, mEntity.getUid());

		// 用户列表-編輯 log
		if (IMResult.equals(Boolean.TRUE)) {
			OperateLogList list = new OperateLogList();
			list.addLog("账号", mEntity.getMemberName(), false);
			list.addLog("手机号", mEntity.getPhone(), false);
			list.addDiffLog("昵称", mEntity.getNickName(), memberPO.getNickName(), false);

			if (StringUtil.isNotBlank(memberPO.getAvatarUrl())) {
				list.addDiffLog("头像", "原头像", "新头像", false);
			}

			if (StringUtil.isNotBlank(password)) {
				list.addDiffLog("密码", "********", "*********", false);
			}

			if (StringUtil.isNotBlank(memberPO.getTradePwd())) {
				list.addDiffLog("交易密码", "********", "*********", false);
			}

			list.addDiffLog("邮箱", mEntity.getEmail(), memberPO.getEmail(), false);
			list.addDiffLog("性别", MemberGenderEnum.parse(mEntity.getGender()).getMessage(),
					MemberGenderEnum.parse(memberPO.getGender()).getMessage(), false);
			list.addDiffLog("备注", mEntity.getMemo(), memberPO.getMemo(), false);
			list.addDiffLog("登陆状态", TextUtils.getEnableString(mEntity.getLoginEnable()),
					TextUtils.getEnableString(memberPO.getLoginEnable()), false);
			list.addDiffLog("添加好友状态", TextUtils.getEnableString(mEntity.getAddFriendEnable()),
					TextUtils.getEnableString(memberPO.getAddFriendEnable()), false);
			list.addDiffLog("建群权限状态", TextUtils.getEnableString(mEntity.getCreateGroupEnable()),
					TextUtils.getEnableString(memberPO.getCreateGroupEnable()), false);
			logService.addOperateLog("/admin/member/edit", list);
		}
	}

	private String encryptTradePwd(String tradePwd, String salt) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt.getBytes(StandardCharsets.UTF_8));
		byte[] hashed = digest.digest(tradePwd.getBytes(StandardCharsets.UTF_8));
		String hashedPwd = Base64.getEncoder().encodeToString(hashed);
		return hashedPwd;
	}

	private boolean syncUpdateMemberInfo(MemberPO memberPO, String uid) {
		InputOutputUserInfo user = new InputOutputUserInfo();
		user.setUserId(uid);
		int flag = 0;
		if (StringUtil.isNotBlank(memberPO.getNickName())) {
			// 1: 呢称
			user.setDisplayName(memberPO.getNickName());
			flag += UpdateUserInfoMask.Update_User_DisplayName;
		}
		if (StringUtil.isNotBlank(memberPO.getAvatarUrl())) {
			// 2: 头像
			user.setPortrait(memberPO.getAvatarUrl());
			flag += UpdateUserInfoMask.Update_User_Portrait;
		}
		if (null != memberPO.getGender()) {
			// 3: 性别
			user.setGender(memberPO.getGender());
			flag += UpdateUserInfoMask.Update_User_Gender;
		}
		if (StringUtil.isNotBlank(memberPO.getPhone())) {
			// 4: 电话
			user.setMobile(memberPO.getPhone());
			flag += UpdateUserInfoMask.Update_User_Mobile;
		}
		if (StringUtil.isNotBlank(memberPO.getEmail())) {
			// 5: email
			user.setEmail(memberPO.getEmail());
			flag += UpdateUserInfoMask.Update_User_Email;
		}
		// if(StringUtil.isNotBlank(memberPO.getAddress())){
		// //6: 地址
		// user.setAddress(memberPO.getAddress());
		// flag += UpdateUserInfoMask.Update_User_Address;
		// }
		// if(StringUtil.isNotBlank(memberPO.getCompany())){
		// //7: 公司
		// user.setCompany(memberPO.getCompany());
		// flag += UpdateUserInfoMask.Update_User_Company;
		// }
		// if(StringUtil.isNotBlank(memberPO.getSocial())){
		// //8: 社交信息
		// user.setSocial(memberPO.getSocial());
		// flag += UpdateUserInfoMask.Update_User_Social;
		// }
		// if(StringUtil.isNotBlank(memberPO.getExtra())){
		// //9: extra信息
		// user.setExtra(memberPO.getExtra());
		// flag += UpdateUserInfoMask.Update_User_Extra;
		// }
		// if(StringUtil.isNotBlank(memberPO.getMemberName())){
		// //10: name信息
		// user.setName(memberPO.getMemberName());
		// flag += UpdateUserInfoMask.Update_User_Name;
		// }
		try {
			UserAdmin.updateUserInfo(user, flag);
		} catch (Exception e) {
			log.error("同步更新member 錯誤:{}", e.getMessage(), e);
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	public interface UpdateUserInfoMask {
		int Update_User_DisplayName = 0x01;
		int Update_User_Portrait = 0x02;
		int Update_User_Gender = 0x04;
		int Update_User_Mobile = 0x08;
		int Update_User_Email = 0x10;
		int Update_User_Address = 0x20;
		int Update_User_Company = 0x40;
		int Update_User_Social = 0x80;
		int Update_User_Extra = 0x100;
		int Update_User_Name = 0x200;
	}

	public void cleanLoginError(MemberDTO memberDTO) {
		MemberPasswordPO mp = MemberPasswordPO.builder().uid(memberDTO.getUid()).build();
		memberPasswordService.update(mp);
	}

	public void batchUpdateLoginAndCreateGroup(MemberPO memberPO) {
		memberMapper.batchUpdateLoginAndCreateGroup(memberPO);
	}

	public List<MemberPO> selectMemberByIds(Collection<Long> idList) {
		return idList.size() > 0 ? memberMapper.selectMemberByIds(idList) : new ArrayList<>();
	}

	public void isPhoneExist(Long memberId, String phone) {
		if (phone != null) {
			MemberQuery memberQuery = MemberQuery.builder().phone(phone).build();
			MemberPO memberPO = memberMapper.list(memberQuery).stream()
					.filter(member -> !member.getId().equals(memberId)).findFirst().orElse(null);
			Assert.isNull(memberPO, message.get(I18nAdmin.MEMBER_PHONE_IS_EXIST));
		}
	}

	public void isMemberNameExist(Long memberId, String memberName) {
		if (memberName != null) {
			MemberQuery memberQuery = MemberQuery.builder().memberName(memberName).build();
			MemberPO memberPO = memberMapper.list(memberQuery).stream()
					.filter(member -> !member.getId().equals(memberId)).findFirst().orElse(null);
			Assert.isNull(memberPO, message.get(I18nAdmin.MEMBER_USERNAME_IS_EXIST));
		}
	}

	/**
	 * 向IM服务器注册新用户
	 * 
	 * @param userName
	 * @param displayName
	 * @param mobile
	 * @return uid
	 */
	public boolean registerIMMember(Long id, String loginPwd, String userName, String displayName, String mobile, String portrait) {
		OutputCreateUser outputCreateUser = registerIMMember(userName, displayName, mobile, portrait);
		if (outputCreateUser != null) {
			MemberPasswordPO mp = MemberPasswordPO.builder().uid(outputCreateUser.getUserId()).build();
			memberPasswordService.insertPassword(mp, loginPwd);
			memberMapper.updateUid(MemberPO.builder().id(id).uid(outputCreateUser.getUserId())
					.memberName(outputCreateUser.getName()).build());
			return true;
		}
		return false;
	}

	public OutputCreateUser registerIMMember(String userName, String displayName, String mobile, String portrait) {
		InputOutputUserInfo user = new InputOutputUserInfo();
		user.setName(userName);
		user.setDisplayName(displayName);
		user.setMobile(mobile);
		user.setPortrait(portrait);
		IMResult<OutputCreateUser> userIdResult = null;// 同步IM服务,新用户注册
		String uid = null;

		try {
			userIdResult = UserAdmin.createUser(user);
			if (userIdResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
				uid = userIdResult.getResult().getUserId();

				List<DefaultMemberPO> defaultMemberPOList = defaultMemberService
						.list(DefaultMemberQuery.builder().inviteCodeId(0L).build());

				for (DefaultMemberPO defaultMemberPO : defaultMemberPOList) {
					if (defaultMemberPO.getMemberId() != null) {
						MemberPO memberPO = selectMemberByIds(Collections.singletonList(defaultMemberPO.getMemberId()))
								.stream().findFirst().orElse(null);
						if (memberPO != null) {
							String targetUid = memberPO.getUid();
							IMResult<Void> voidIMResult = RelationAdmin.setUserFriend(uid, targetUid, true,
									null);
							if (voidIMResult.code == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
								log.info("新增预设好友:{} 成功", memberPO.getUid());
								messageService.sendMessageToIM(targetUid, uid, defaultMemberPO.getWelcomeText());
								messageService.sendSystemMessageToIM(targetUid, uid, MessageChatEnum.SYSTEM_FRIEND_HELLO_MESSAGE);
								messageService.sendSystemMessageToIM(targetUid, uid, MessageChatEnum.SYSTEM_FRIEND_ALREADY_MESSAGE);
							}
						}
					} else if (defaultMemberPO.getGroupId() != null) {
						GroupPO groupPO = groupService.list(GroupQuery.builder().id(defaultMemberPO.getGroupId()).build())
								.stream().findFirst().orElse(null);

						if (groupPO != null) {
							MemberDTO mangerMember = list(MemberQuery.builder().id(groupPO.getManagerId()).build())
									.stream().findFirst().orElse(null);
							if (mangerMember != null) {
								PojoGroupMember groupMember = new PojoGroupMember();
								groupMember.setMember_id(uid);
								groupMember.setType(0);
								groupMember.setCreateDt(new Date().getTime());
								IMResult<Void> voidIMResult = GroupAdmin.addGroupMembers(mangerMember.getUid(), groupPO.getGid(),
										Collections.singletonList(groupMember), null, null);
								if (voidIMResult.code == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
									log.info("新增预设群:{} 成功", groupPO.getGid());
								}
							}
						}
					}
				}
				return userIdResult.getResult();
			} else {
				log.info("同步IM服务,新用户注册失败 Create user failure {}", userIdResult.code);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("IM server注册新用户失败:{}", e.getMessage(), e);
		}
		return null;
	}

	public List<MemberPO> selectMemberByMemberNameOrPhone(String member) {
		return memberMapper.selectMemberByMemberNameOrPhone(member);
	}

	public void update(MemberPO memberPO){
		memberMapper.update(memberPO);
	}
}
