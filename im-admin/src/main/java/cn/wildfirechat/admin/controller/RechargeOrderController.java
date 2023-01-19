package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.OtpUtil;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.AdminUserService;
import cn.wildfirechat.admin.service.RechargeService;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.RechargeOrderBO;
import cn.wildfirechat.common.model.dto.RechargeChannelInfoDTO;
import cn.wildfirechat.common.model.dto.RechargeOrderDTO;
import cn.wildfirechat.common.model.enums.EditorRoleEnum;
import cn.wildfirechat.common.model.enums.RechargeOrderStatusEnum;
import cn.wildfirechat.common.model.form.RechargeOrderAuditForm;
import cn.wildfirechat.common.model.form.RechargeOrderDirectDepositForm;
import cn.wildfirechat.common.model.form.RechargeOrderQueryForm;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.RechargeOrderVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.DateUtils;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/rechargeOrder")
@Api(tags = "充值订单")
public class RechargeOrderController extends BaseController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UploadFileUtils uploadFileUtils;

    @GetMapping("/page")
    @ApiOperation("充值订单列表")
    public ResponseVO<?> page(RechargeOrderQueryForm form, Page page) {
        RechargeOrderQuery query = RechargeOrderQuery.builder().build();
        ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        return ResponseVO.success(covert(rechargeService.page(query, page)));
    }

    private PageVO<RechargeOrderVO> covert(PageVO<RechargeOrderDTO> pages) {
        List<RechargeOrderVO> vos = new ArrayList<>();
        pages.getPage().forEach(page -> {
            RechargeOrderVO vo = RechargeOrderVO.builder().build();
            ReflectionUtils.copyFields(vo, page, ReflectionUtils.STRING_TRIM_TO_NULL);
            vo.setAmount(page.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            vo.setCreateTime(DateUtils.format(page.getCreateTime(), DateUtils.YMD_HMS));
            vo.setCompleteTime(DateUtils.format(page.getCompleteTime(), DateUtils.YMD_HMS));
            String payImage = StringUtil.isNotBlank(page.getPayImage())? uploadFileUtils.parseFilePathToUrl(page.getPayImage()) :null;
            vo.setPayImage(payImage);

            try {
                RechargeChannelInfoDTO dto;
                if(StringUtil.isNotBlank(page.getChannelInfo())){
                    dto = objectMapper.readValue(page.getChannelInfo(), RechargeChannelInfoDTO.class);
                }else{
                    dto = new RechargeChannelInfoDTO();
                }
                vo.setChannelInfo(dto);
            } catch (JsonProcessingException e) {
                log.error("error msg:{}", e.getMessage(), e);
            }

            vos.add(vo);
        });

        PageVO<RechargeOrderVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(pages.getTotal());
        pageVO.setTotalPage(pages.getTotalPage());
        return pageVO;
    }

    @PostMapping("/audit")
    @ApiOperation("充值订单审核")
    public ResponseVO audit(@RequestBody RechargeOrderAuditForm form) {
        String logPrefix = "【充值订单审核】";
        Long updaterId = SpringSecurityUtil.getUserId();
        log.info("{} userId: {}, form: {}", logPrefix, updaterId, form);

        //otp验证
        authOTP(form.getTotpCode());

        RechargeOrderBO bo = RechargeOrderBO.builder().build();
        ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        bo.setUpdaterId(updaterId);
        bo.setUpdaterRole(EditorRoleEnum.ADMIN.getValue());
        rechargeService.audit(bo);
        return ResponseVO.success();
    }

    @PostMapping("/directDeposit")
    @ApiOperation("直接打币")
    public ResponseVO directDeposit(@RequestBody RechargeOrderDirectDepositForm form) {
        String logPrefix = "【直接打币】";
        Long updaterId = SpringSecurityUtil.getUserId();
        log.info("{} userId: {}, form: {}", logPrefix, updaterId, form);

        // opt验证
        authOTP(form.getTotpCode());
        RechargeOrderBO bo = RechargeOrderBO.builder().build();
        ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        bo.setUpdaterId(updaterId);
        bo.setUpdaterRole(EditorRoleEnum.ADMIN.getValue());
        bo.setStatus(RechargeOrderStatusEnum.COMPLETED.getValue());
        rechargeService.audit(bo);
        return ResponseVO.success();
    }

    /**
     * otp验证
     * @param totpCode
     */
    private void authOTP(String totpCode) {
        AdminUserPO admin = adminUserService.info(SpringSecurityUtil.getUserId());
        if (admin.getIsVerifyOtpKey() == 1){
            if(StringUtil.isEmpty(totpCode)){
                Assert.isTrue(false, message.get(I18nAdmin.ADMIN_USER_OTP_IS_REQUIRED));
            }
            boolean isSuccess = OtpUtil.verifyTotpCode(admin.getOtpKey(), totpCode);
            if (!isSuccess){
                Assert.isTrue(false, message.get(I18nAdmin.ADMIN_USER_VERIFY_OTP_FAILURE));
            }
        } else {
            Assert.isTrue(false, message.get(I18nAdmin.ADMIN_USER_NOT_BINDING_OTP));
        }
    }
}
