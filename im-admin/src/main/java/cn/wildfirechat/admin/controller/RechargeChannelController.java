package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.RechargeChannelService;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.RechargeChannelEnum;
import cn.wildfirechat.common.model.enums.RechargeChannelStatusEnum;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.dto.RechargeChannelInfoDTO;
import cn.wildfirechat.common.model.form.RechargeChannelAddForm;
import cn.wildfirechat.common.model.form.RechargeChannelDeleteForm;
import cn.wildfirechat.common.model.form.RechargeChannelQueryForm;
import cn.wildfirechat.common.model.form.RechargeChannelUpdateForm;
import cn.wildfirechat.common.model.po.RechargeChannelPO;
import cn.wildfirechat.common.model.query.RechargeChannelQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.RechargeChannelVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.FileNameUtils;
import cn.wildfirechat.common.utils.FileUtils;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/rechargeChannel")
@Api(tags = "充值渠道")
public class RechargeChannelController extends BaseController {

    @Resource
    private RechargeChannelService rechargeChannelService;

    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private UploadFileUtils uploadFileUtils;

    @GetMapping("/page")
    @ApiOperation("充值渠道列表")
    public ResponseVO<?> page(RechargeChannelQueryForm form, Page page) {
        try {
            log.info("page form: {}, page: {}", form, page);
            RechargeChannelQuery query = RechargeChannelQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            log.info("充值渠道列表查询成功");
            return ResponseVO.success(covert(rechargeChannelService.page(query, page)));
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    private PageVO<RechargeChannelVO> covert(PageVO<RechargeChannelPO> pages) {
        List<RechargeChannelVO> vos = new ArrayList<>();
        pages.getPage().forEach(po -> {
            RechargeChannelVO vo = RechargeChannelVO.builder().build();
//            ReflectionUtils.copyFields(vo, page, ReflectionUtils.STRING_TRIM_TO_NULL);
            BeanUtils.copyProperties(po, vo, "info");
            try {
                RechargeChannelInfoDTO dto = objectMapper.readValue(po.getInfo(), RechargeChannelInfoDTO.class);
                String qrCodeUrl = StringUtil.isNotBlank(po.getQrCodeImage())? uploadFileUtils.parseFilePathToUrl(po.getQrCodeImage()) :null;
                dto.setQrCodeImage(qrCodeUrl);
                vo.setInfo(dto);
            } catch (JsonProcessingException e) {
                log.error("error msg:{}", e.getMessage(), e);
            }
            vos.add(vo);
        });

        PageVO<RechargeChannelVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(pages.getTotal());
        pageVO.setTotalPage(pages.getTotalPage());
        return pageVO;
    }

    @PostMapping("/add")
    @ApiOperation("充值渠道添加")
    public ResponseVO add(RechargeChannelAddForm form) {
        String logPrefix = "【充值渠道添加】";
        try {
            log.info("{} form: {}", logPrefix, form);
            RechargeChannelPO po = RechargeChannelPO.builder().build();
            RechargeChannelInfoDTO infoDto = RechargeChannelInfoDTO.builder().build();
            ReflectionUtils.copyFields(po, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            ReflectionUtils.copyFields(infoDto, form, ReflectionUtils.STRING_TRIM_TO_NULL);

            // 上传图片
            MultipartFile qrCodeFile = form.getQrCodeImageFile();
            String urlPath = uploadFileUtils.uploadFile(qrCodeFile, FileUtils.RECHARGE_CHANNEL_QR_PATH, FileNameUtils.RECHARGE_CHANNEL_QR_PREFIX);
            if (urlPath != null) {
                po.setQrCodeImage(urlPath);
            }
            po.setInfo(objectMapper.writeValueAsString(infoDto));
            rechargeChannelService.insert(po);
            rechargeChannelService.addLog(form);
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("充值渠道編輯")
    public ResponseVO update(@RequestBody RechargeChannelUpdateForm form) {
        String logPrefix = "【充值渠道編輯】";
        try {
            log.info("{} form: {}", logPrefix, form);

            RechargeChannelPO po = RechargeChannelPO.builder().build();
            ReflectionUtils.copyFields(po, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            rechargeChannelService.updateLog(po);
            rechargeChannelService.update(po);
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("充值渠道刪除")
    public ResponseVO delete(@RequestBody RechargeChannelDeleteForm form) {
        String logPrefix = "【充值渠道刪除】";
        try {
            log.info("{} form: {}", logPrefix, form);
            rechargeChannelService.deleteLog(form.getId());
            rechargeChannelService.delete(form.getId());
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        }
    }
}
