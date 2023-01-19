package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.CsvUtil;
import cn.wildfirechat.admin.service.MemberOperateLogService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.dto.MemberOperateLogCsvDTO;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.query.MemberOperateLogQuery;
import cn.wildfirechat.common.model.vo.*;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/member/operateLog")
@Api(tags = "用户操作日誌管理")
public class MemberOperateLogController extends BaseController {

    private final String MEMBER_OPERATE_LOG_REPORT_FILENAME = "MemberOperateLog";

    @Resource
    private MemberOperateLogService memberOperateLogService;


    @GetMapping(value = "/page")
    @ApiOperation(value = "用户操作日誌列表")
    public ResponseVO<?> page(MemberOperateLogQueryForm form, Page page) {
        try {
            log.info("page form: {}, page: {}", form, page);
            MemberOperateLogQuery query = MemberOperateLogQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            PageVO<MemberOperateLogVO> memberOperateLogPageVO = memberOperateLogService.page(query, page);
            log.info("用户操作日誌列表查询成功");
            return ResponseVO.success(memberOperateLogPageVO);
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    @ApiOperation(value = "用户操作日誌报表")
    @GetMapping(value = "/list/csv")
    public void exportReportCsv(MemberOperateLogQueryForm form, HttpServletResponse response) {
        try{
            log.info("exportReportCsv form: {}", form);
            MemberOperateLogQuery query = MemberOperateLogQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            List<MemberOperateLogVO> voList = memberOperateLogService.list(query);


            Date createTimeGt = FormatUtil.parseDateTime(form.getCreateTimeGt());
            Date createTimeLe = FormatUtil.parseDateTime(form.getCreateTimeLe());

            List<MemberOperateLogCsvDTO> dtoList = voList.stream().map(MemberOperateLogCsvDTO::from).collect(Collectors.toList());

            CsvUtil.export(
                    response,
                    dtoList,
                    MemberOperateLogCsvDTO.class,
                    MEMBER_OPERATE_LOG_REPORT_FILENAME,
                    createTimeGt,
                    createTimeLe);

            // 用户日志-导出报表
            OperateLogList list = new OperateLogList();
            list.addLog("导出报表", "操作成功", false);
            logService.addOperateLog("/admin/member/operateLog/list/csv", list);
        }catch(Exception e){
            log.error("用户操作日誌报表输出错误 message: {}, form params:{}", e.getMessage(), form);
            e.printStackTrace();
        }

    }


}
