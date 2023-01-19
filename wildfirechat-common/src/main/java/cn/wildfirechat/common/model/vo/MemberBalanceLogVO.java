package cn.wildfirechat.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * View Object 系统管理者显示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("会员资金明细")
public class MemberBalanceLogVO {
    @ApiModelProperty(value = "资金明细流水ID")
    private Long memberBalanceId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户UID")
    private String uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "帳號")
    private String memberName;

    @ApiModelProperty(value = "交易货币")
    private String currency;

    @ApiModelProperty(value = "交易金额")
    private String amount;

    @ApiModelProperty(value = "交易前馀额")
    private String beforeBalance;

    @ApiModelProperty(value = "交易后馀额")
    private String afterBalance;

    @ApiModelProperty(value = "冻结交易金额")
    private String freezeAmount;

    @ApiModelProperty(value = "交易前冻结金额")
    private String beforeFreeze;

    @ApiModelProperty(value = "交易后冻结金额")
    private String afterFreeze;

    @ApiModelProperty(value = "交易时间")
    private String createTime;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String memo;
}
