package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeChannelUpdateForm {

    @NotNull
    @ApiModelProperty(value = "充值渠道ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "充值渠道名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String memo;
}
