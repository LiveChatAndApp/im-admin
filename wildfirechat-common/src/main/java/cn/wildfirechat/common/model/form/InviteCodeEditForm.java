package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Form 邀请码编辑表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeEditForm {

    @ApiModelProperty(value = "邀请码ID")
    private Long inviteCodeID;

    @ApiModelProperty(value = "备注")
    private String memo;

    @Min(value = 0, message = "Parameter [status] cannot be less than 0")
    @Max(value = 1, message = "Parameter [status] cannot be greater than 1")
    @ApiModelProperty(value = "状态 0: 关闭, 1: 开启")
    private Integer status;

    @Min(value = 1, message = "Parameter [friendsDefault] cannot be less than 1")
    @Max(value = 2, message = "Parameter [friendsDefault] cannot be greater than 2")
    @ApiModelProperty(value = "预设好友模式 1: 所有, 2: 轮询")
    private Integer friendsDefaultType;
}
