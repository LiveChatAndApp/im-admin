package cn.wildfirechat.admin.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.wildfirechat.admin.common.enums.AdminUserStatusEnum;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.MessageSenderRoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.wildfirechat.admin.common.strategy.PasswordUtil;
import cn.wildfirechat.admin.mapper.AdminUserMapper;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.AdminUserBO;
import cn.wildfirechat.common.model.bo.MemberAddBO;
import cn.wildfirechat.common.model.enums.MemberAccountTypeEnum;
import cn.wildfirechat.common.model.enums.MemberGenderEnum;
import cn.wildfirechat.common.model.po.AdminRolePO;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.AdminRoleQuery;
import cn.wildfirechat.common.model.query.AdminUserQuery;
import cn.wildfirechat.common.model.vo.AdminUserVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminUserService extends BaseService {
	@Resource
	private AdminUserMapper adminUserMapper;

	@Resource
	private AdminRoleService adminRoleService;

	@Resource
	private MemberService memberService;

	public List<AdminUserPO> list(AdminUserQuery query) {
		return adminUserMapper.list(query);
	}

	public PageVO<AdminUserVO> page(AdminUserQuery query, Page page) {
		page.startPageHelper();
		List<AdminUserPO> list = adminUserMapper.list(query);
		PageVO<AdminUserPO> adminUserPOPageVO = new PageInfo<>(list).convertToPageVO();

		Map<Long, AdminRolePO> roleMap = adminRoleService.selectMap(AdminRoleQuery.builder().build());
		Set<Long> memberIdSet = list.stream().map(AdminUserPO::getMemberId).collect(Collectors.toSet());
		Map<Long, MemberPO> memberPOMap = memberService.selectMemberByIds(memberIdSet).stream()
				.collect(Collectors.toMap(MemberPO::getId, memberPO -> memberPO));

		List<AdminUserVO> vos = new ArrayList<>();
		list.forEach(po -> {
			AdminUserVO vo = AdminUserVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			vo.setRoleName(roleMap.get(vo.getRoleId()).getName());
			MemberPO memberPO = memberPOMap.get(po.getMemberId());
			if(memberPO != null){
				vo.setChatNickName(memberPO.getNickName());
				vo.setChatMemberName(memberPO.getMemberName());
			}
			vos.add(vo);
		});
		PageVO<AdminUserVO> adminUserVOPageVO = new PageInfo<>(vos).convertToPageVO();
		adminUserVOPageVO.setTotal(adminUserPOPageVO.getTotal());
		adminUserVOPageVO.setTotalPage(adminUserPOPageVO.getTotalPage());
		return adminUserVOPageVO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void add(AdminUserBO bo) {

		// 检查userName
		List<AdminUserPO> poList = adminUserMapper.list(AdminUserQuery.builder().username(bo.getUsername()).build());
		Assert.isTrue(poList.size() == 0, message.get(I18nAdmin.ADMIN_USER_IS_EXIST));

		AdminUserPO po = AdminUserPO.builder().build();
		ReflectionUtils.copyFields(po, bo, ReflectionUtils.STRING_TRIM_TO_EMPTY);

		MemberAddBO memberAddBO = new MemberAddBO();
		memberAddBO.setPassword(bo.getPassword());
		memberAddBO.setPhone(bo.getPhone());
		memberAddBO.setAccountType(MemberAccountTypeEnum.MANAGE.getValue());
		memberAddBO.setGender(MemberGenderEnum.SECRET.getValue());
		memberAddBO.setEmail(bo.getEmail());
		memberAddBO.setNickName(bo.getNickname());
		memberAddBO.setMemberName(bo.getUsername());
		memberAddBO.setChannel(bo.getChannel());

		Long memberId = memberService.add(memberAddBO);

		String password = po.getPassword();
		if (StringUtils.isEmpty(password)) {
			password = PasswordUtil.generatePassword();
		}
		po.setPassword(PasswordUtil.encodeA(password));

		Date timestamp = new Date();
		po.setMemberId(memberId);
		po.setLoginError(0);
		po.setLoginFail(0);
		po.setLoginFrozen(timestamp);
		po.setLoginIp("");
		po.setLoginSucceed(0);
		po.setLoginTime(timestamp);
		po.setRegisterTime(timestamp);
		po.setCreateTime(timestamp);
		po.setBrandName("SYSTEMADMIN");
		po.setFullPath(SpringSecurityUtil.getFullPath());
		po.setParentId(SpringSecurityUtil.getUserId());

		adminUserMapper.insert(po);

		po.setFullPath(po.getFullPath() + po.getId() + AdminUserPO.PATH_SEPARATOR);
		adminUserMapper.updateFullPath(po.getId(), po.getFullPath());
		//后台账号-新增 log
		OperateLogList list = new OperateLogList();

		String role = MessageSenderRoleEnum.parse(bo.getRoleId().intValue()).getMessage();

		list.addLog("用户昵称",bo.getNickname(),false);
		list.addLog("用户账号",bo.getUsername(),false);
		list.addLog("用户密码","*********",false);
		list.addLog("角色名称",role,false);
		if(StringUtils.isNotBlank(bo.getEmail())){
			list.addLog("电子邮箱",bo.getEmail(),false);
		}
		if(StringUtils.isNotBlank(bo.getMemo())){
			list.addLog("备注",bo.getMemo(),false);
		}
		logService.addOperateLog("/admin/user/insert",list);

	}

	public int update(AdminUserPO po) {
		updateLog(po);
		return adminUserMapper.update(po);
	}

	public int updateLoginInfo(AdminUserPO po) {
		return adminUserMapper.update(po);
	}

	public String updatePassword(Long id, String password) {
		if (StringUtils.isEmpty(password)) {
			password = PasswordUtil.generatePassword();
		}
		AdminUserPO po = adminUserMapper.selectById(id);

		int result = adminUserMapper.updatePassword(id, PasswordUtil.encodeA(password));
		Assert.isTrue(result != 0, message.get(I18nAdmin.DB_FAIL));
		//后台账号-重置密码 log
		String userName = SpringSecurityUtil.getPrincipal();
		OperateLogList list = new OperateLogList();
		list.addLog("用户账号",userName,false);
		list.addDiffLog("用户密码","*********","********",false);
		logService.addOperateLog("/admin/user/resetPwd",list);
		return password;
	}

	public AdminUserPO info(Long adminId) {
		AdminUserPO info = adminUserMapper.selectById(adminId);
		Assert.notNull(info, message.get(I18nAdmin.ADMIN_USER_NOT_EXIST));
		return info;
	}

	public boolean updateOtpKey(AdminUserPO po) {
		return adminUserMapper.updateOtpKey(po) > 0;
	}

	public List<AdminUserPO> selectAdminUserByIds(Collection<Long> idList) {
		return idList.size() > 0 ? adminUserMapper.selectAdminUserByIds(idList) : new ArrayList<>();
	}

	public void updateLog(AdminUserPO po){
		//后台账号-更新 log
		AdminUserPO beforePO = adminUserMapper.selectById(po.getId());
		OperateLogList list = new OperateLogList();
		list.addLog("用户账号",beforePO.getUsername(),false);
		list.addDiffLog("用户昵称",beforePO.getNickname(),po.getNickname(),false);list.addDiffLog("电子邮箱",beforePO.getEmail(),po.getEmail(),false);
		list.addDiffLog("状态", AdminUserStatusEnum.parse(beforePO.getStatus()).getMessage(),
				AdminUserStatusEnum.parse(po.getStatus()).getMessage(),false);
		list.addDiffLog("备注",beforePO.getMemo(),po.getMemo(),false);
		logService.addOperateLog("/admin/user/update",list);

	}


}
