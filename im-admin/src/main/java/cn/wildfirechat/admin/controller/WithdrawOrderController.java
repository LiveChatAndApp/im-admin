package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.WithdrawOrderService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.bo.WithdrawOrderBO;
import cn.wildfirechat.common.model.dto.WithdrawOrderDTO;
import cn.wildfirechat.common.model.enums.EditorRoleEnum;
import cn.wildfirechat.common.model.form.WithdrawOrderAuditForm;
import cn.wildfirechat.common.model.form.WithdrawOrderQueryForm;
import cn.wildfirechat.common.model.query.WithdrawOrderQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.model.vo.WithdrawOrderVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/withdrawOrder")
@Api(tags = "提现订单")
public class WithdrawOrderController extends BaseController {
    @Resource
    private WithdrawOrderService withdrawOrderService;

    @GetMapping("/page")
    @ApiOperation("提现订单列表")
    public ResponseVO<?> page(WithdrawOrderQueryForm form, Page page) {
        String logPrefix = "【提现订单查询】";
        try {
            WithdrawOrderQuery query = WithdrawOrderQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            log.info("{} query: {}", logPrefix, query);
            return ResponseVO.success(covert(withdrawOrderService.page(query, page)));
        } catch (IllegalArgumentException e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("{} form: {}", logPrefix, form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    private PageVO<WithdrawOrderVO> covert(PageVO<WithdrawOrderDTO> pages) {
        List<WithdrawOrderVO> vos = new ArrayList<>();
        pages.getPage().forEach(page -> {
            WithdrawOrderVO vo = WithdrawOrderVO.builder().build();
            ReflectionUtils.copyFields(vo, page, ReflectionUtils.STRING_TRIM_TO_NULL);
            vo.setAmount(page.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            vo.setCreateTime(DateUtils.format(page.getCreateTime(), DateUtils.YMD_HMS));
            vo.setCompleteTime(DateUtils.format(page.getCompleteTime(), DateUtils.YMD_HMS));
            vos.add(vo);
        });

        PageVO<WithdrawOrderVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(pages.getTotal());
        pageVO.setTotalPage(pages.getTotalPage());
        return pageVO;
    }

    @PostMapping("/audit")
    @ApiOperation("提现订单审核")
    public ResponseVO audit(@RequestBody WithdrawOrderAuditForm form) {
        String logPrefix = "【提现订单审核】";
        Long updaterId = SpringSecurityUtil.getUserId();
        log.info("{} userId: {}, form: {}", logPrefix, updaterId, form);

        WithdrawOrderBO bo = WithdrawOrderBO.builder().build();
        ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        bo.setUpdaterId(updaterId);
        bo.setUpdaterRole(EditorRoleEnum.ADMIN.getValue());
        withdrawOrderService.audit(bo);
        return ResponseVO.success();
    }
}
