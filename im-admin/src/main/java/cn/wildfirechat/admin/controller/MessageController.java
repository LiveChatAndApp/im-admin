package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.config.RedisConfig;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.MessageService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.MessageTypeEnum;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.exception.ResponseCode;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.MessageSendBO;
import cn.wildfirechat.common.model.dto.MessageLastChatDTO;
import cn.wildfirechat.common.model.enums.MessageSenderRoleEnum;
import cn.wildfirechat.common.model.form.MessageQueryForm;
import cn.wildfirechat.common.model.form.MessageQueryPageForm;
import cn.wildfirechat.common.model.form.MessageSendForm;
import cn.wildfirechat.common.model.po.MessagePO;
import cn.wildfirechat.common.model.query.MessagePageQuery;
import cn.wildfirechat.common.model.query.MessageQuery;
import cn.wildfirechat.common.model.vo.MessageLastChatVO;
import cn.wildfirechat.common.model.vo.MessagePageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.DateUtils;
import cn.wildfirechat.sdk.MessageAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/admin/message")
@Api(tags = "消息管理")
public class MessageController extends BaseController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private RedisUtil redisUtil;

	@GetMapping("/page")
	@ApiOperation("消息列表")
	public ResponseVO<PageVO<MessagePageVO>> page(MessageQueryPageForm form, Page page) {
		log.info("消息列表 form: {}, page: {}", form, page);
		MessagePageQuery messagePageQuery = new MessagePageQuery();
		ReflectionUtils.copyFields(messagePageQuery, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		if (Objects.equals(form.getAccount1(), "@admin") || Objects.equals(form.getAccount2(), "@admin")) {
			messagePageQuery.setSenderRole(MessageSenderRoleEnum.ADMIN.getValue());
			if (form.getAccount1() != null) {
				form.setAccount1(form.getAccount1().equals("@admin") ? form.getAccount2() : form.getAccount1());
			}
			if (form.getAccount2() != null) {
				form.setAccount2(form.getAccount2().equals("@admin") ? form.getAccount1() : form.getAccount2());
			}
		}
		PageVO<MessagePageVO> messagePageVOPageVO = messageService.page(messagePageQuery, page);

		log.info("消息列表查询成功");
		return ResponseVO.success(messagePageVOPageVO);
	}

	@PostMapping("/revert/{messageId}")
	@ApiOperation("撤回消息")
	public ResponseVO<?> revert(@PathVariable("messageId") Long messageId) {
		String username = SpringSecurityUtil.getSuffixUsername();

		log.info("撤回消息 messageId:{}, username: {}", messageId, username);
		MessagePO messagePO = messageService.list(MessageQuery.builder().id(messageId).isRevert(0).build()).stream()
				.findFirst().orElse(null);
		Assert.notNull(messagePO, message.getMessage(I18nAdmin.MESSAGE_NOT_EXIST));

		try {
			IMResult<Void> voidIMResult = MessageAdmin.recallMessage("admin", messagePO.getMid());
			if (voidIMResult.getCode() == ErrorCode.ERROR_CODE_SUCCESS.getCode()) {
				redisUtil.set(RedisConfig.MESSAGE_REVERT_PREFIX + messagePO.getMid(), username);

				//消息列表-撤回消息 log

				OperateLogList list = new OperateLogList();
				list.addLog("消息类型", MessageTypeEnum.getMessageByValue(messagePO.getMessageType()),false);
				list.addLog("发送者账号",messagePO.getSenderAccountName(),false);
				list.addLog("接收者账号",messagePO.getReceiverAccountName(),false);
				list.addLog("内容",messagePO.getContent(),false);
				logService.addOperateLog("/admin/message/revert/{messageId}",list);

				return ResponseVO.success();
			} else {
				return ResponseVO.error(ResponseCode.IM_ERROR, voidIMResult.getMsg());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// log.info("撤回消息 Success:{}", messageService.revert(messagePO, username));
	}

	@GetMapping("/lastChat")
	@ApiOperation("最後消息列表")
	public ResponseVO<?> lastChat(MessageQueryForm form, Page page) {
		log.info("最後消息列表 form: {}, page: {}", form, page);
		MessageQuery query = new MessageQuery();
		ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		return ResponseVO.success(covertLastChats(messageService.lastChat(query)));
	}

	private List<MessageLastChatVO> covertLastChats(List<MessageLastChatDTO> list) {
		List<MessageLastChatVO> vos = new ArrayList<>();
		list.forEach(po -> {
			MessageLastChatVO vo = MessageLastChatVO.builder().build();
			ReflectionUtils.copyFields(vo, po, ReflectionUtils.STRING_TRIM_TO_NULL);
			vo.setCreateTime(DateUtils.format(po.getCreateTime(), DateUtils.YMD_HMS));
			vos.add(vo);
		});
		return vos;
	}

	@PostMapping("/send/message")
	@ApiOperation("發送消息")
	public ResponseVO<?> sendMessage(MessageSendForm form) throws Exception {
//		log.info("發送消息 form:{}", form);
		MessageSendBO bo = MessageSendBO.builder().build();
		ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
		messageService.sendMessage(bo);
		return ResponseVO.success();
	}
}
