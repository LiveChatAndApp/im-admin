package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Form 邀请码搜寻表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeQueryPageForm {

    @ApiModelProperty(value = "验证码")
    private String inviteCode;

    @ApiModelProperty(value = "备注")
    private String memo;

    @Min(value = 0, message = "Parameter [status] cannot be less than 0")
    @Max(value = 1, message = "Parameter [status] cannot be greater than 1")
    @ApiModelProperty(value = "状态 0: 停用, 1: 启用")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "统计注册人数开始时间(yyyy-MM-dd HH:mm:ss)")
    private Date registerTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "统计注册人数结束时间(yyyy-MM-dd HH:mm:ss)")
    private Date registerTimeLe;
}
