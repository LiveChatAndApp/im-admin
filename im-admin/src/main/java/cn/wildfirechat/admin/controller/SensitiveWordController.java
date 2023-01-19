package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.SensitiveWordService;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.bo.SensitiveWordAddBO;
import cn.wildfirechat.common.model.form.SensitiveWordHitQueryForm;
import cn.wildfirechat.common.model.form.SensitiveWordQueryForm;
import cn.wildfirechat.common.model.query.SensitiveWordQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.model.vo.SensitiveWordHitVO;
import cn.wildfirechat.common.model.vo.SensitiveWordVO;
import cn.wildfirechat.common.support.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/sensitiveWord")
@Api(tags = "敏感词管理")
public class SensitiveWordController extends BaseController {

    @Resource
    private SensitiveWordService sensitiveWordService;

    @GetMapping(value = "/swPage")
    @ApiOperation(value = "敏感词列表")
    public ResponseVO<?> swPage(SensitiveWordQueryForm form, Page page) {
        try {
            log.info("page form: {}, page: {}", form, page);
            SensitiveWordQuery query = SensitiveWordQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            PageVO<SensitiveWordVO> sensitiveWordPageVO = sensitiveWordService.swPage(query, page);
            log.info("系统操作日誌列表查询成功");
            return ResponseVO.success(sensitiveWordPageVO);
        } catch (IllegalArgumentException e) {
            log.error("swPage::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swPage::", e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping(value = "/swCreate")
    @ApiOperation(value = "新增敏感词")
    public ResponseVO<?> swCreate(@RequestParam List<String> contentList) {
        try {
            List<SensitiveWordAddBO> boList = new ArrayList<>();
            contentList.forEach(s ->{
                SensitiveWordAddBO bo = SensitiveWordAddBO.builder().build();
                bo.setContent(s);
                boList.add(bo);
            });
            sensitiveWordService.swInsertBatch(boList);
            log.info("敏感词新增成功");
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("swCreate::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swCreate::", e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping(value = "/swDelete")
    @ApiOperation(value = "删除敏感词")
    public ResponseVO<?> swDelete(@RequestParam List<Long> ids) {
        try {
            sensitiveWordService.swDeleteBatch(ids);
            log.info("删除敏感词成功");
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("swDelete::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swDelete::", e);
            return ResponseVO.error(e.getMessage());
        }
    }


    @GetMapping(value = "/swHitPage")
    @ApiOperation(value = "敏感词命中列表")
    public ResponseVO<?> swHitPage(SensitiveWordHitQueryForm form, Page page) {
        try {
            log.info("form: {}, page: {}", form, page);
            PageVO<SensitiveWordHitVO> sensitiveWordHitPageVO = sensitiveWordService.swHitPage(form.getSenderAccount(), page);
            log.info("敏感词命中列表查询成功");
            return ResponseVO.success(sensitiveWordHitPageVO);
        } catch (IllegalArgumentException e) {
            log.error("swHitPage::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swHitPage::", e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping(value = "/swHitDelete")
    @ApiOperation(value = "删除命中敏感词")
    public ResponseVO<?> swHitDelete(@RequestParam List<Long> ids) {
        try {
            sensitiveWordService.swHitDelete(ids);
            log.info("删除命中敏感词成功");
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("swHitDelete::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swHitDelete::", e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @PostMapping(value = "/swHitDeleteAll")
    @ApiOperation(value = "清空命中敏感词")
    public ResponseVO<?> swHitDeleteAll() {
        try {
            sensitiveWordService.swHitDeleteAll();
            log.info("清空敏感词命中成功");
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("swHitDeleteAll::", e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("swHitDeleteAll::", e);
            return ResponseVO.error(e.getMessage());
        }
    }





}
