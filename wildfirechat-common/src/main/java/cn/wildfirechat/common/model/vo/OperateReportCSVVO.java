package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateReportCSVVO {

	@ApiModelProperty(value = "日期")
	@JsonProperty("日期")
	private String date;

	@ApiModelProperty(value = "用戶充值金额")
	@JsonProperty("用戶充值金额")
	private String rechargeTotal;

	@ApiModelProperty(value = "用戶提现金额")
	@JsonProperty("用戶提现金额")
	private String withdrawTotal;
}
