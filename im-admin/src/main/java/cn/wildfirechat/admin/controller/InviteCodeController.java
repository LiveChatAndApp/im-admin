package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.InviteCodeService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.InviteCodeDefaultFriendTypeEnum;
import cn.wildfirechat.common.model.enums.InviteCodeStatusEnum;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.InviteCodeAddBO;
import cn.wildfirechat.common.model.enums.DefaultMemberDefaultTypeEnum;
import cn.wildfirechat.common.model.enums.EditorRoleEnum;
import cn.wildfirechat.common.model.form.InviteCodeAddForm;
import cn.wildfirechat.common.model.form.InviteCodeEditForm;
import cn.wildfirechat.common.model.form.InviteCodeQueryPageForm;
import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.query.InviteCodePageQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.vo.InviteCodeDefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.InviteCodePageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/admin/inviteCode")
@Api(tags = "邀请码")
public class InviteCodeController extends BaseController {

	@Autowired
	private InviteCodeService inviteCodeService;

	@ApiOperation(value = "邀请码查询")
	@GetMapping("/page")
	public ResponseVO<PageVO<InviteCodePageVO>> page(@Valid InviteCodeQueryPageForm form, Page page) {
		log.info("邀请码查询 form: {}, page: {}", form, page);
		InviteCodePageQuery inviteCodePageQuery = new InviteCodePageQuery();
		ReflectionUtils.copyFields(inviteCodePageQuery, form, ReflectionUtils.STRING_TRIM_TO_NULL);

		PageVO<InviteCodePageVO> inviteCodePageVO = inviteCodeService.page(inviteCodePageQuery, page);
		log.info("查询邀请码页面成功");

		return ResponseVO.success(inviteCodePageVO);
	}

	@ApiOperation(value = "新增邀请码")
	@PostMapping("/add")
	public ResponseVO<?> add(InviteCodeAddForm form) {
		log.info("新增邀请码 form: {}", form);
		String username = SpringSecurityUtil.getPrincipal();

		InviteCodeAddBO inviteCodeAddBO = new InviteCodeAddBO();
		ReflectionUtils.copyFields(inviteCodeAddBO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		inviteCodeAddBO.setCreator(username);
		inviteCodeAddBO.setUpdater(username);
		log.info("邀请码新增成功 ID:{}", inviteCodeService.insert(inviteCodeAddBO));

		return ResponseVO.success();
	}

	@ApiOperation(value = "编辑邀请码")
	@PostMapping("/edit")
	public ResponseVO<?> edit(@Valid InviteCodeEditForm form) {
		log.info("编辑邀请码 form: {}", form);
		String username = SpringSecurityUtil.getPrincipal();

		InviteCodePO inviteCodePO = inviteCodePOExist(form.getInviteCodeID());

		// 邀请码-编辑 log
		OperateLogList list = new OperateLogList();
		list.addLog("邀请码", inviteCodePO.getInviteCode(), false);
		list.addDiffLog("备注", inviteCodePO.getMemo(), form.getMemo(),false);

		if (!Objects.isNull(form.getFriendsDefaultType())) {
			list.addDiffLog("预设好友模式",
					InviteCodeDefaultFriendTypeEnum.parse(inviteCodePO.getFriendsDefaultType()).getMessage(),
					InviteCodeDefaultFriendTypeEnum.parse(form.getFriendsDefaultType()).getMessage(), false);
		}

		if (!Objects.isNull(form.getStatus())) {
			list.addDiffLog("状态",
					InviteCodeStatusEnum.parse(inviteCodePO.getStatus()).getMessage(),
					InviteCodeStatusEnum.parse(form.getStatus()).getMessage(), false);
		}

		ReflectionUtils.copyFields(inviteCodePO, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		inviteCodePO.setUpdater(username);
		inviteCodePO.setUpdaterRole(EditorRoleEnum.ADMIN.getValue());

		inviteCodeService.update(inviteCodePO);
		log.info("邀请码编辑成功 ID:{}", inviteCodePO.getId());
		logService.addOperateLog("/admin/inviteCode/edit", list);
		return ResponseVO.success();
	}

//	@ApiOperation(value = "删除邀请码")
//	@PostMapping("/delete/{id}")
//	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "邀请码流水号")})
//	public ResponseVO<?> delete(@PathVariable("id") Long id) {
//		log.info("删除邀请码 form: {}", id);
//		InviteCodeQuery inviteCodeQuery = new InviteCodeQuery();
//		inviteCodeQuery.setId(id);
//
//		InviteCodePO inviteCodePO = inviteCodeService.list(inviteCodeQuery).stream().findFirst().orElse(null);
//		Assert.notNull(inviteCodePO, "邀请码不存在");
//		inviteCodePO.setStatus(InviteCodeStatusEnum.DELETE.getValue());
//		inviteCodeService.update(inviteCodePO);
//		log.info("邀请码删除成功 ID:{}", inviteCodePO.getId());
//
//		return ResponseVO.success();
//	}

	@GetMapping("/defaultMember/{id}")
	@ApiOperation("预设好友列表")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "邀请码流水号", dataTypeClass=Long.class)})
	public ResponseVO<PageVO<InviteCodeDefaultMemberPageVO>> defaultMember(@PathVariable("id") Long id, Page page) {
		log.info("预设好友列表 id:{}, page:{}", id, page);
		InviteCodePO inviteCodePO = inviteCodePOExist(id);
		PageVO<InviteCodeDefaultMemberPageVO> inviteCodeDefaultMemberPageVOPageVO = inviteCodeService
				.selectInviteCodeDefaultMemberPageVO(id, inviteCodePO.getInviteCode(),
						DefaultMemberDefaultTypeEnum.FRIEND, page);

		log.info("查询预设好友列表成功");
		return ResponseVO.success(inviteCodeDefaultMemberPageVOPageVO);
	}

	@GetMapping("/defaultGroup/{id}")
	@ApiOperation("预设群列表")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "邀请码流水号", dataTypeClass=Long.class)})
	public ResponseVO<PageVO<InviteCodeDefaultMemberPageVO>> defaultGroup(@PathVariable("id") Long id, Page page) {
		log.info("预设好友列表 id:{}, page:{}", id, page);
		InviteCodePO inviteCodePO = inviteCodePOExist(id);
		PageVO<InviteCodeDefaultMemberPageVO> inviteCodeDefaultMemberPageVOPageVO = inviteCodeService
				.selectInviteCodeDefaultMemberPageVO(id, inviteCodePO.getInviteCode(),
						DefaultMemberDefaultTypeEnum.GROUP, page);

		log.info("查询预设群列表成功");
		return ResponseVO.success(inviteCodeDefaultMemberPageVOPageVO);
	}

	private InviteCodePO inviteCodePOExist(Long id) {
		InviteCodePO inviteCodePO = inviteCodeService.list(InviteCodeQuery.builder().id(id).build()).stream()
				.findFirst().orElse(null);
		Assert.notNull(inviteCodePO, message.getMessage(I18nAdmin.INVITE_CODE_NOT_EXIST));
		return inviteCodePO;
	}

}
