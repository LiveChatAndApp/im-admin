package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.ChatRoomService;
import cn.wildfirechat.admin.service.MemberService;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.vo.*;
import cn.wildfirechat.common.utils.FileUtils;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.ChatRoomCreateBo;
import cn.wildfirechat.common.model.bo.ChatRoomQueryOnlineMemberBo;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.po.ChatRoomPO;
import cn.wildfirechat.common.model.query.ChatRoomQuery;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.FileNameUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/chatroom")
@Api(tags = "聊天室管理")
public class ChatRoomController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	@GetMapping(value = "/page")
	@ApiOperation(value = "搜寻聊天室列表")
	public ResponseVO<?> page(ChatRoomQueryForm form, Page page) {
		try {
			log.info("page form: {}, page: {}", form, page);
			ChatRoomQuery query = ChatRoomQuery.builder().build();
			ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
			log.info("page query: {}, page: {}", query, page);
			PageVO<ChatRoomVO> pageResult = chatRoomService.page(query, page);
			return ResponseVO.success(pageResult);
		} catch (IllegalArgumentException e) {
			log.error("page form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("page form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping("/create")
	@ApiOperation("创建聊天室")
	public ResponseVO<?> create(@Valid ChatRoomCreateForm form) {
		try {
			log.info("创建聊天室: {}", form);
			ChatRoomCreateBo bo = ChatRoomCreateBo.builder().build();
			BeanUtils.copyProperties(form, bo, "imageFile");
			List<ChatRoomPO> sameSortList = chatRoomService.select(ChatRoomQuery.builder().sort(bo.getSort()).build());
			if (sameSortList.size() > 0) {
				Assert.isTrue(false, message.getMessage(I18nAdmin.CHATROOM_SORT_REPEAT_ERROR));
			}

			// 头像挡案处理
			MultipartFile chatImage = form.getImageFile();
			String urlPath = uploadFileUtils.uploadFile(chatImage, FileUtils.CHATROOM_AVATAR_PATH, FileNameUtils.CHATROOM_IMAGE_PREFIX);
			if (urlPath != null) {
				bo.setImage(urlPath);
			}

			boolean result = chatRoomService.createChatroom(bo);
			return ResponseVO.success(result);
		} catch (IllegalArgumentException e) {
			log.error("create form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("create form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping("/destroy")
	@ApiOperation("解散聊天室")
	public ResponseVO<?> destroy(@RequestBody @Valid ChatRoomDestroyForm form) {
		try {
			log.info("解散聊天室: {}", form);
			boolean result = chatRoomService.destroyChatroom(form.getId(), form.getCid());
			return ResponseVO.success(result);
		} catch (IllegalArgumentException e) {
			log.error("destroy form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("destroy form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@GetMapping("/onlineMember")
	@ApiOperation("查看聊天室在线用户")
	public ResponseVO<?> onlineMember(@Valid ChatRoomQueryOnlineMemberForm form) {
		try {
			log.info("查看聊天室在线用户: {}", form);
			ChatRoomQueryOnlineMemberBo chatRoomQueryOnlineMemberBo = ChatRoomQueryOnlineMemberBo.builder().build();
			ReflectionUtils.copyFields(chatRoomQueryOnlineMemberBo, form, ReflectionUtils.STRING_TRIM_TO_NULL);

			List<MemberChatroomVO> list = chatRoomService.onlineMember(chatRoomQueryOnlineMemberBo);
			return ResponseVO.success(list);
		} catch (IllegalArgumentException e) {
			log.error("onlineMember form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("onlineMember form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

	@PostMapping("/updateSort")
	@ApiOperation("編輯聊天室排序")
	public ResponseVO<?> updateSort(@RequestBody @Valid ChatRoomUpdateForm form) {
		try {
			log.info("編輯聊天室排序: {}", form);
			List<ChatRoomPO> cidList = chatRoomService
					.select(ChatRoomQuery.builder().cid(form.getCid()).build());

			List<ChatRoomPO> sameSortList = chatRoomService
					.select(ChatRoomQuery.builder().sort(form.getSort()).build());
			if (sameSortList.size() > 0) {
				Assert.isTrue(false, message.getMessage(I18nAdmin.CHATROOM_SORT_REPEAT_ERROR));
			}

			boolean result = chatRoomService.updateSort(form.getId(), form.getCid(), form.getSort());
			
			//聊天室列表-排序 log
			if (result){
				OperateLogList list = new OperateLogList();
				list.addLog("聊天室ID",form.getCid(),false);
				list.addLog("聊天室名称",cidList.get(0).getName(),false);
				list.addDiffLog("顺序",cidList.get(0).getSort(),form.getSort(),false);
				logService.addOperateLog("/admin/chatroom/updateSort",list);
			}
			return ResponseVO.success(result);
		} catch (IllegalArgumentException e) {
			log.error("updateSort form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("updateSort form: {}", form, e);
			return ResponseVO.error(e.getMessage());
		}
	}

}
