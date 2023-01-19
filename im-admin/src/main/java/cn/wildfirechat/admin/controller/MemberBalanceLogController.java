package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.MemberBalanceLogService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.dto.MemberBalanceLogDTO;
import cn.wildfirechat.common.model.form.MemberBalanceLogForm;
import cn.wildfirechat.common.model.query.MemberBalanceLogQuery;
import cn.wildfirechat.common.model.vo.MemberBalanceLogVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.DateUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/memberBalanceLog")
@Api(tags = "手动充_提")
public class MemberBalanceLogController {
    @Resource
    private MemberBalanceLogService memberBalanceLogService;

    @GetMapping(value = "/page")
    @ResponseBody
    public ResponseVO<PageVO<MemberBalanceLogVO>> page(MemberBalanceLogForm form, Page page) throws Exception {
        log.info("page form: {}", form);
        MemberBalanceLogQuery query = MemberBalanceLogQuery.builder().build();
        ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
        return ResponseVO.success(covert(memberBalanceLogService.page(query, page)));
    }

    private PageVO<MemberBalanceLogVO> covert(PageVO<MemberBalanceLogDTO> pages) {
        List<MemberBalanceLogVO> vos = new ArrayList<>();
        pages.getPage().forEach(page -> {
            MemberBalanceLogVO vo = MemberBalanceLogVO.builder().build();
            ReflectionUtils.copyFields(vo, page, ReflectionUtils.STRING_TRIM_TO_NULL);
            vo.setBeforeBalance(page.getBeforeBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            vo.setAfterBalance(page.getAfterBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());

            BigDecimal amount = page.getAfterBalance().subtract(page.getBeforeBalance());
            vo.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());

            vo.setBeforeFreeze(page.getBeforeFreeze().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            vo.setAfterFreeze(page.getAfterFreeze().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            
            BigDecimal freezeAmount = page.getAfterFreeze().subtract(page.getBeforeFreeze());
            vo.setFreezeAmount(freezeAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());


            vo.setCreateTime(DateUtils.format(page.getCreateTime(), DateUtils.YMD_HMS));
            vos.add(vo);
        });

        PageVO<MemberBalanceLogVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(pages.getTotal());
        pageVO.setTotalPage(pages.getTotalPage());
        return pageVO;
    }
}
