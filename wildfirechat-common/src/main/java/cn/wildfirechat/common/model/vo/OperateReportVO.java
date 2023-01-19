package cn.wildfirechat.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateReportVO {

	@ApiModelProperty(value = "日期")
	private String date;

	@ApiModelProperty(value = "充值金额")
	private String rechargeTotal;

	@ApiModelProperty(value = "提现金额")
	private String withdrawTotal;
}
