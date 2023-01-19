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
public class MemberBalanceLogForm {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户帐号")
    private String memberName;
    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "类型 1:手动充值,2:手动提取")
    private Integer type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间起(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "交易时间迄(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;
}
