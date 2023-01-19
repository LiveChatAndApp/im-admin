package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AdminUpdateForm {

    @NotNull(message = "{id}不能为空")
    @ApiModelProperty(value="后台用户ID", required = true)
    private Long id;

    @ApiModelProperty(value="昵称" )
    private String nickname;

    @ApiModelProperty(value="电子信箱" )
    private String email;

    @ApiModelProperty(value="手机" )
    private String phone;

    @NotNull(message = "{status}不能为空")
    @ApiModelProperty(value="状态" , example = "1")
    private Integer status;

    @ApiModelProperty(value="备注" )
    private String memo;

}
