package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AdminResetPwdForm {

    @NotNull(message = "{id}不能为空")
    @ApiModelProperty(value="后台用户ID" )
    private Long id;

    @ApiModelProperty(value="密码" )
    private String password;

}
