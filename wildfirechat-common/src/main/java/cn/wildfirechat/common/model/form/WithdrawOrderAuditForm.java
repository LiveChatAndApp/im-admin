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
public class WithdrawOrderAuditForm {
    @ApiModelProperty(value = "提现订单ID", required = true)
    private Long id;
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;
}
