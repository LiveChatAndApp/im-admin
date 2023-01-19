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
public class RechargeChannelQueryForm {

    @ApiModelProperty(value = "充值方式 1:银行卡,2:微信,3:支付宝")
    private Integer paymentMethod;
}
