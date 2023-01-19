package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualRechargeWithdrawForm {
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
    @ApiModelProperty(value = "类型 1:手动充值,2:手动提取", required = true)
    private Integer type;// 类型 1:手动充值,2:手动提取
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;// 金额
//    @ApiModelProperty(value = "冻结金额")
//    private BigDecimal freeze;// 冻结金额
}
