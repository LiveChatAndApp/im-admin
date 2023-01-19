package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddBatchForm {

    @ApiModelProperty("前缀")
    private String prefix;

    @Min(value = 1, message = "[nickNameType]不能小于1")
    @Max(value = 2, message = "[nickNameType]不能大于2")
    @ApiModelProperty("昵称类型 1:随机昵称, 2:自定义昵称")
    private Integer nickNameType;

    @ApiModelProperty("昵称")
    private String nickName;

    @Min(value = 1, message = "[avatarType]不能小于1")
    @Max(value = 2, message = "[avatarType]不能大于2")
    @ApiModelProperty("头像类型 1:系统默认, 2:随机生成")
    private Integer avatarType;

    @NotBlank(message = "Parameter [loginPwd] cannot be null")
    @ApiModelProperty(value = "登陆密码", required = true)
    private String loginPwd;

    @Min(value = 1, message = "[batchCount]不能小于1")
    @ApiModelProperty(value = "批量数量", required = true)
    private Integer batchCount;
}
