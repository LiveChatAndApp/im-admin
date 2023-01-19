package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuthQueryForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="角色ID")
    private Long roleId;

}
