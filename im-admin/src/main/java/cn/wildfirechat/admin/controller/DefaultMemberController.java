package cn.wildfirechat.admin.controller;

import cn.wildfirechat.common.model.dto.DefaultMemberDTO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.DefaultMemberService;
import cn.wildfirechat.admin.service.InviteCodeService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.DefaultMemberAddBO;
import cn.wildfirechat.common.model.enums.DefaultMemberDefaultTypeEnum;
import cn.wildfirechat.common.model.enums.DefaultMemberTypeEnum;
import cn.wildfirechat.common.model.enums.InviteCodeDefaultFriendTypeEnum;
import cn.wildfirechat.common.model.form.DefaultMemberAddForm;
import cn.wildfirechat.common.model.form.DefaultMemberEditForm;
import cn.wildfirechat.common.model.form.DefaultMemberQueryPageForm;
import cn.wildfirechat.common.model.form.DefaultMemberTypeForm;
import cn.wildfirechat.common.model.po.DefaultMemberPO;
import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.query.DefaultMemberQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.vo.DefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.StringUtil;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/defaultMember")
@Api(tags = "预设好友_群")
public class DefaultMemberController extends BaseController {

	@Autowired
	private DefaultMemberService defaultMemberService;

	@Autowired
	private InviteCodeService inviteCodeService;

	@GetMapping("/page")
	@ApiOperation(value = "默认好友/群列表")
	public ResponseVO<PageVO<DefaultMemberPageVO>> page(DefaultMemberQueryPageForm defaultMemberQueryPageForm,
			Page page) {
		log.info("默认好友/群列表查询:{}", defaultMemberQueryPageForm);
		DefaultMemberQuery defaultMemberQuery = new DefaultMemberQuery();
		InviteCodePO inviteCodePO = null;
		if (StringUtil.isNotEmpty(defaultMemberQueryPageForm.getInviteCode())) {
			inviteCodePO = inviteCodeService
					.list(InviteCodeQuery.builder().inviteCode(defaultMemberQueryPageForm.getInviteCode()).build())
					.stream().findFirst().orElse(null);
			defaultMemberQuery.setInviteCodeId(-1L);
		}

		ReflectionUtils.copyFields(defaultMemberQuery, defaultMemberQueryPageForm, ReflectionUtils.STRING_TRIM_TO_NULL);
		if (inviteCodePO != null) {
			defaultMemberQuery.setInviteCodeId(inviteCodePO.getId());
		}
		PageVO<DefaultMemberPageVO> defaultMemberPageVOPageVO = defaultMemberService.page(defaultMemberQuery, page);

		log.info("默认好友/群列表查询成功");

		return ResponseVO.success(defaultMemberPageVOPageVO);
	}

	@PostMapping("/add")
	@ApiOperation(value = "新增默认好友/群")
	public ResponseVO<?> add(@RequestBody DefaultMemberAddForm defaultMemberAddForm) {
		log.info("新增默认好友/群:{}", defaultMemberAddForm);

		String username = SpringSecurityUtil.getPrincipal();

		DefaultMemberAddBO defaultMemberAddBO = DefaultMemberAddBO.builder().build();
		ReflectionUtils.copyFields(defaultMemberAddBO, defaultMemberAddForm, ReflectionUtils.STRING_TRIM_TO_NULL);
		DefaultMemberTypeEnum defaultMemberTypeEnum = DefaultMemberTypeEnum.parse(defaultMemberAddForm.getType());
		defaultMemberAddBO.setDefaultMemberTypeEnum(
				defaultMemberTypeEnum == null ? DefaultMemberTypeEnum.INVITE_CODE_ONLY : defaultMemberTypeEnum);
		defaultMemberAddBO.setDefaultMemberDefaultTypeEnum(
				DefaultMemberDefaultTypeEnum.parse(defaultMemberAddForm.getDefaultType()));
		defaultMemberAddBO.setCreator(username);
		defaultMemberAddBO.setUpdater(username);

		defaultMemberService.insert(defaultMemberAddBO);
		log.info("新增默认好友/群查询成功");
		return ResponseVO.success();
	}

	@PostMapping("/edit")
	@ApiOperation(value = "编辑好友/群")
	public ResponseVO<?> edit(@RequestBody DefaultMemberEditForm form) {
		log.info("编辑好友/群 form:{}", form);
		String username = SpringSecurityUtil.getPrincipal();

		// InviteCodePO inviteCodePO = checkInviteCodeExist(form.getInviteCode());
		DefaultMemberPO defaultMemberPO = defaultMemberService
				.list(DefaultMemberQuery.builder().id(form.getId()).build()).stream().findFirst().orElse(null);
		Assert.notNull(defaultMemberPO, message.get(I18nAdmin.DEFAULT_MEMBER_NOT_EXIST));

		DefaultMemberPO updateTarget = new DefaultMemberPO();
		ReflectionUtils.copyFields(updateTarget, form, ReflectionUtils.STRING_TRIM_NONE);
		updateTarget.setUpdater(username);

		defaultMemberService.update(updateTarget);

		DefaultMemberDTO defaultMemberDTO = defaultMemberService.selectById(defaultMemberPO.getId());

		// 默认好友-编辑 log
		OperateLogList list = new OperateLogList();
		list.addLog("邀请码", defaultMemberDTO.getInviteCode(), false);
		list.addLog("账号", defaultMemberDTO.getMemberName(), false);
		list.addLog("群组ID", defaultMemberDTO.getGid(), false);
		list.addDiffLog("欢迎词", defaultMemberPO.getWelcomeText(), form.getWelcomeText(), false);
		logService.addOperateLog("/admin/defaultMember/edit", list);
		return ResponseVO.success();
	}

	@PostMapping("/delete/{id}")
	@ApiOperation(value = "删除预设好友_群")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "预设好友_群ID", dataTypeClass = Long.class)})
	public ResponseVO<?> delete(@PathVariable("id") Long id) {
		log.info("删除预设好友/群 id:{}", id);
		DefaultMemberPO defaultMemberPO = defaultMemberService.list(DefaultMemberQuery.builder().id(id).build())
				.stream().findFirst().orElse(null);
		DefaultMemberDTO defaultMemberDTO = defaultMemberService.selectById(id);
		Assert.notNull(defaultMemberDTO, message.get(I18nAdmin.DEFAULT_MEMBER_NOT_EXIST));

		defaultMemberService.delete(id);
		log.info("删除预设好友/群 成功");


		// 默认好友-删除预设好友/群 log
		OperateLogList list = new OperateLogList();
		list.addLog("邀请码", defaultMemberDTO.getInviteCode(), false);
		list.addLog("昵称", defaultMemberDTO.getNickName(), false);
		list.addLog("群组ID", defaultMemberDTO.getGid(), false);
		logService.addOperateLog("/admin/defaultMember/delete/{id}", list);
		return ResponseVO.success();
	}

	@PostMapping("/type")
	@ApiOperation(value = "全局预设好友/群状态")
	public ResponseVO<?> setDefaultType(@RequestBody DefaultMemberTypeForm defaultMemberTypeForm) {
		log.info("全局预设好友/群状态:{}", defaultMemberTypeForm);
		String username = SpringSecurityUtil.getPrincipal();
		inviteCodeService.updateFriendDefaultType(
				InviteCodeDefaultFriendTypeEnum.parse(defaultMemberTypeForm.getDefaultMemberType()), username);
		return ResponseVO.success();
	}

	private InviteCodePO checkInviteCodeExist(String inviteCode) {
		InviteCodePO inviteCodePO = inviteCodeService.list(InviteCodeQuery.builder().inviteCode(inviteCode).build())
				.stream().findFirst().orElse(null);
		Assert.notNull(inviteCodePO, message.getMessage(I18nAdmin.INVITE_CODE_NOT_EXIST));
		return inviteCodePO;
	}
}
