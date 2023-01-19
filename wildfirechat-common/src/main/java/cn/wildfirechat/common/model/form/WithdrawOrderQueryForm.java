package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderQueryForm {
    @ApiModelProperty(value = "订单偏号")
    private String orderCode;
    @ApiModelProperty(value = "用户帐号")
    private String memberName;
    @ApiModelProperty(value = "提现渠道 1:银行卡,2:微信,3:支付宝")
    private Integer channel;
    @ApiModelProperty(value = "订单状态 1:待审核,2:已完成,3:已拒绝")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间起(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间迄(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间起(yyyy-MM-dd HH:mm:ss)")
    private Date completeTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间迄(yyyy-MM-dd HH:mm:ss)")
    private Date completeTimeLe;
}
