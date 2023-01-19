package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdminKickOutForm {

    @NotNull(message = "{id}不能为空")
    @ApiModelProperty(value="后台用户ID" )
    private Long id;

}
