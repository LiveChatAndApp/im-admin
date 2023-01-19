package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.CsvUtil;
import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.admin.service.SystemOperateLogService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.dto.SystemOperateLogCsvDTO;
import cn.wildfirechat.common.model.form.SystemOperateLogQueryForm;
import cn.wildfirechat.common.model.query.SystemOperateLogQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.model.vo.SystemOperateLogVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.utils.FormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.oas.mappers.OasLicenceMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/system/operateLog")
@Api(tags = "系统操作日誌管理")
public class SystemOperateLogController extends BaseController {

    private final String SYSTEM_OPERATE_LOG_REPORT_FILENAME = "SystemOperateLog";

    @Resource
    private SystemOperateLogService systemOperateLogService;

    @Resource
    private LogService logService;


    @GetMapping(value = "/page")
    @ApiOperation(value = "系统操作日誌列表")
    public ResponseVO<?> page(SystemOperateLogQueryForm form, Page page) {
        try {
            log.info("page form: {}, page: {}", form, page);
            SystemOperateLogQuery query = SystemOperateLogQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            PageVO<SystemOperateLogVO> systemOperateLogPageVO = systemOperateLogService.page(query, page);
            log.info("系统操作日誌列表查询成功");
            return ResponseVO.success(systemOperateLogPageVO);
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }



    @ApiOperation(value = "系统操作日誌报表")
    @GetMapping(value = "/list/csv")
    public void exportReportCsv(SystemOperateLogQueryForm form, HttpServletResponse response) {
        try{
            log.info("exportReportCsv form: {}", form);
            SystemOperateLogQuery query = SystemOperateLogQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            List<SystemOperateLogVO> voList = systemOperateLogService.list(query);


            Date createTimeGt = FormatUtil.parseDateTime(form.getCreateTimeGt());
            Date createTimeLe = FormatUtil.parseDateTime(form.getCreateTimeLe());

            Map<Long, String> authMap = logService.getAuthMap();
            List<SystemOperateLogCsvDTO> dtoList = voList.stream().map(vo -> SystemOperateLogCsvDTO.from(vo, authMap)).collect(Collectors.toList());

            CsvUtil.export(
                    response,
                    dtoList,
                    SystemOperateLogCsvDTO.class,
                    SYSTEM_OPERATE_LOG_REPORT_FILENAME,
                    createTimeGt,
                    createTimeLe);
            //系统日志-导出报表 log
            OperateLogList list = new OperateLogList();
            list.addLog("导出报表","操作成功",false);
            logService.addOperateLog("/admin/system/operateLog/list/csv",list);
        }catch(Exception e){
            log.error("系统操作日誌报表输出错误 message: {}, form params:{}", e.getMessage(), form);
            e.printStackTrace();
        }

    }


}
