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
public class RechargeOrderQueryForm {
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    @ApiModelProperty(value = "用户账号")
    private String memberName;
    @ApiModelProperty(value = "充值方式 1:线下支付,2:微信,3:支付宝")
    private Integer method;
    @ApiModelProperty(value = "状态 1:待审查,2:已完成,3:已拒绝,4:用户取消,5:订单超时")
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
