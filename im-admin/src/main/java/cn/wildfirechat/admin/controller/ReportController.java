package cn.wildfirechat.admin.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.vo.OperateReportCSVVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.wildfirechat.admin.common.utils.CsvUtil;
import cn.wildfirechat.admin.service.RechargeService;
import cn.wildfirechat.admin.service.WithdrawOrderService;
import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import cn.wildfirechat.common.model.vo.OperateReportVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/report")
@Api(tags = "报表管理")
public class ReportController extends BaseController {
	private final String SYSTEM_OPERATE_REPORT_FILENAME = "SystemOperateReport";

	@Autowired
	private WithdrawOrderService withdrawOrderService;

	@Autowired
	private RechargeService rechargeService;

	@GetMapping("/operate")
	@ApiOperation("经营报表")
	public ResponseVO<?> operateReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		List<OperateReportVO> voList = getOperateReportList(date);
		Map<String, Object> map = new HashMap<>();
		map.put("data", voList);
		map.put("total", countReportTotal(voList));
		return ResponseVO.success(map);
	}

	@GetMapping("/operate/csv")
	@ApiOperation("经营报表CSV")
	public void operateReportExcel(HttpServletRequest request, HttpServletResponse response,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		List<OperateReportVO> operateReportVOList = getOperateReportList(date);
		operateReportVOList.add(countReportTotal(operateReportVOList));
		try {
			List<OperateReportCSVVO> vo = operateReportVOList.stream().map(item -> {OperateReportCSVVO operateReportCSVVO = new OperateReportCSVVO();
				ReflectionUtils.copyFields(operateReportCSVVO, item, ReflectionUtils.STRING_TRIM_TO_NULL);
				return operateReportCSVVO;
			}).collect(Collectors.toList());

			CsvUtil.export(response, vo, OperateReportCSVVO.class, SYSTEM_OPERATE_REPORT_FILENAME,
					date, new Date());
			//经营报表-导出报表 log
			OperateLogList list = new OperateLogList();
			list.addLog("导出报表","操作成功",false);
			logService.addOperateLog("/admin/report/operate/csv",list);
		} catch (IOException e) {
			log.error("系统操作日誌报表输出错误 message: {}, form params:{}", e.getMessage(), DateUtils.format(date, "yyyy-MM-dd"));
			e.printStackTrace();
		}
	}

	private List<OperateReportVO> getOperateReportList(Date date) {
		Date currenDate = DateUtils.getPureDay(new Date());
		Map<Date, OperateTotalDTO> withdrawTotalMap = withdrawOrderService.totalByDate(date).stream()
				.collect(Collectors.toMap(OperateTotalDTO::getDate, dto -> dto));
		Map<Date, OperateTotalDTO> rechargeTotalMap = rechargeService.totalByDate(date).stream()
				.collect(Collectors.toMap(OperateTotalDTO::getDate, dto -> dto));

		List<OperateReportVO> operateReportVOList = new ArrayList<>();
		date = DateUtils.addDays(date, -1);
		while (currenDate.after(date)) {
			log.info("currentDate: {}, date: {}", currenDate, date);
			OperateReportVO vo = new OperateReportVO();
			vo.setDate(DateUtils.format(currenDate, "yyyy-MM-dd"));
			vo.setWithdrawTotal("0");
			vo.setRechargeTotal("0");
			if (withdrawTotalMap.get(currenDate) != null) {
				vo.setWithdrawTotal(
						withdrawTotalMap.get(currenDate).getTotal().setScale(2, RoundingMode.DOWN).toString());
			}
			if (rechargeTotalMap.get(currenDate) != null) {
				vo.setRechargeTotal(
						rechargeTotalMap.get(currenDate).getTotal().setScale(2, RoundingMode.DOWN).toString());
			}
			operateReportVOList.add(vo);
			currenDate = DateUtils.addDays(currenDate, -1);
		}

		return operateReportVOList;
	}

	private OperateReportVO countReportTotal(List<OperateReportVO> list) {
		OperateReportVO operateReportCSVVO = new OperateReportVO();
		operateReportCSVVO.setDate("总计");
		BigDecimal withdraw = BigDecimal.ZERO;
		BigDecimal recharge = BigDecimal.ZERO;
		for (OperateReportVO operateReportVO : list) {
			withdraw = withdraw.add(new BigDecimal(operateReportVO.getWithdrawTotal()));
			recharge = recharge.add(new BigDecimal(operateReportVO.getRechargeTotal()));
		}
		operateReportCSVVO.setWithdrawTotal(withdraw.setScale(2, RoundingMode.DOWN).toString());
		operateReportCSVVO.setRechargeTotal(recharge.setScale(2, RoundingMode.DOWN).toString());
		return operateReportCSVVO;
	}
}
