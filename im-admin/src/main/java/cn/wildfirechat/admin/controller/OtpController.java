package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.OtpUtil;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.AdminUserService;
import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.admin.service.OtpLogService;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.form.OtpQrCodeRequestForm;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.po.OtpLogPO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.SpringMessage;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/otp")
@Api(tags = "otp验证")
public class OtpController {
    public static final String OTP_GENERATE_QR_CODE = "/generate/qrCode";
    public static final String OTP_VERIFY_TOTP_CODE = "/verify/totpCode";
    public static final String OTP_RESET_SECRET_KEY = "/reset/secretKey";

    @Resource
    private LogService logService;

    @Resource
    protected SpringMessage message;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private OtpLogService otpLogService;

    @NacosValue(value = "${upload.real.path}", autoRefreshed = true)
    private String uploadPath;

    @NacosValue(value = "${otp.qrcode.url}", autoRefreshed = true)
    private String qrCodeUrl;

    @NacosValue(value = "${otp.qrcode.size}", autoRefreshed = true)
    private Integer qrCodeSize;

    @PostMapping(value = OTP_GENERATE_QR_CODE)
    @ApiOperation(value = "otp_生成", httpMethod = "POST")
    public ResponseVO<?> generateQrCode(@RequestBody @Valid OtpQrCodeRequestForm form){
        try {
            log.info("OtpController generateQrCode Start");
            log.info("generateQrCode form: {}", form);
            AdminUserPO admin = adminUserService.info(form.getAdminUserId());
            Assert.notNull(admin, message.get(I18nAdmin.ADMIN_USER_NOT_EXIST));

            if(admin.getIsVerifyOtpKey() == 1){
                throw new IllegalArgumentException(message.get(I18nAdmin.ADMIN_USER_BINDING_OTP));
            }

            // 產生 QR Code Start
            String secretKey = OtpUtil.generateSecretKey();
            String totpCode = OtpUtil.getTOTPCode(secretKey);
            String companyName = "IMAdmin系统";
            String barCodeUrl = OtpUtil.getGoogleAuthenticatorBarCode(secretKey, admin.getUsername(), companyName);
            String qrCodeName = UUID.randomUUID().toString().replace("-", "");
            OtpUtil.createQRCode(barCodeUrl, uploadPath+"/qr-code/"+qrCodeName, qrCodeSize, qrCodeSize);
            // 產生 QR Code End

            boolean otpResult = adminUserService.updateOtpKey(AdminUserPO.builder().id(admin.getId()).otpKey(secretKey).isVerifyOtpKey(0).build());

            //添增一笔otpLog
            OtpLogPO otpLog = OtpLogPO.builder()
                    .userId(form.getAdminUserId())
                    .secretKey(secretKey)
                    .totpCode(totpCode)
                    .status(otpResult? 1: 0)//狀態 0:驗證失敗 1成功
                    .action("GENERAT_QR_CODE")
                    .creator(SpringSecurityUtil.getPrincipal())
                    .build();
            otpLogService.insert(otpLog);

            String optQrCodeUrl =  qrCodeUrl+"/"+qrCodeName;
            return ResponseVO.success(optQrCodeUrl);
        } catch (IllegalArgumentException e) {
            log.error("generateQrCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("generateQrCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }

    }


    @PostMapping(value = OTP_VERIFY_TOTP_CODE)
    @ApiOperation(value = "otp_绑定验证", httpMethod = "POST")
    public ResponseVO<?> verifyTotpCode(@RequestBody @Valid OtpQrCodeRequestForm form){
        try {
            log.info("OtpController verifyTotpCode Start");
            log.info("verifyTotpCode form: {}", form);
            Assert.notNull(form.getTotpCode(), message.get(I18nAdmin.ADMIN_USER_VERIFY_OTP_FAILURE));

            AdminUserPO adminUser = adminUserService.info(form.getAdminUserId());
            Assert.notNull(adminUser, message.get(I18nAdmin.ADMIN_USER_NOT_EXIST));
            Assert.notNull(adminUser.getOtpKey(), message.get(I18nAdmin.ADMIN_USER_NOT_BINDING_OTP));

            boolean isSuccess = OtpUtil.verifyTotpCode(adminUser.getOtpKey(), form.getTotpCode());
            Assert.isTrue(isSuccess, message.get(I18nAdmin.ADMIN_USER_VERIFY_OTP_FAILURE));

            OtpLogPO otpLog = OtpLogPO.builder()
                    .userId(adminUser.getId())
                    .secretKey(adminUser.getOtpKey())
                    .totpCode(form.getTotpCode())
                    .status(isSuccess? 1 : 0)//狀態 0:驗證失敗 1成功
                    .action("VERIFY_TOTP_CODE")
                    .creator(SpringSecurityUtil.getPrincipal())
                    .build();
            otpLogService.insert(otpLog);

            if(isSuccess){
                adminUserService.updateOtpKey(AdminUserPO.builder().id(adminUser.getId()).isVerifyOtpKey(1).build());
            }

            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("verifyTotpCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("verifyTotpCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }


    @PostMapping(value = OTP_RESET_SECRET_KEY)
    @ApiOperation(value = "otp_重置", httpMethod = "POST")
    public ResponseVO<?> resetSecretKey(@RequestBody @Valid OtpQrCodeRequestForm form) {
        try {
            log.info("OtpController resetSecretKey Start");
            log.info("resetSecretKey form: {}", form);
            AdminUserPO adminUser = adminUserService.info(form.getAdminUserId());
            Assert.notNull(adminUser, message.get(I18nAdmin.ADMIN_USER_NOT_EXIST));

            if (adminUser.getOtpKey() != null) {
                boolean otpResult = adminUserService.updateOtpKey(AdminUserPO.builder().id(adminUser.getId()).isVerifyOtpKey(0).build());

                OtpLogPO otpLog = OtpLogPO.builder()
                        .userId(adminUser.getId())
                        .secretKey(adminUser.getOtpKey())
                        .totpCode("-")
                        .status(otpResult? 1: 0)//狀態 0:驗證失敗 1成功
                        .action("RESET_OTP")
                        .creator(SpringSecurityUtil.getPrincipal())
                        .build();
                otpLogService.insert(otpLog);
            }

            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("verifyTotpCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("verifyTotpCode form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }


    }
}
