package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderAuditForm {

    @ApiModelProperty(value = "充值订单ID", required = true)
    private Long id;
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;
    @ApiModelProperty(value = "OTP验证码", required = true)
    private String totpCode;
}
