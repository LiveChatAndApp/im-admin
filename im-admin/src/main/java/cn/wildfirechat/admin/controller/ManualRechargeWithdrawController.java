package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.ManualRechargeWithdrawService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.bo.MemberBalanceBO;
import cn.wildfirechat.common.model.form.ManualRechargeWithdrawForm;
import cn.wildfirechat.common.model.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin/manual")
@Api(tags = "手动充_提")
public class ManualRechargeWithdrawController extends BaseController {
    @Resource
    private ManualRechargeWithdrawService manualRechargeWithdrawService;

    @PostMapping("/rechargeWithdraw")
    @ApiOperation("充/提")
    public ResponseVO recharge(ManualRechargeWithdrawForm form) {
        log.info("手动充/提: {}", form);
        Long updaterId = SpringSecurityUtil.getUserId();
        MemberBalanceBO bo = MemberBalanceBO.builder().build();
        ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        bo.setUpdaterId(updaterId);
        manualRechargeWithdrawService.rechargeWithdraw(bo);
        return ResponseVO.success();
    }
}
