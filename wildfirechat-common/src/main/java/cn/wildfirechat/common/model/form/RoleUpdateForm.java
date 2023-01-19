package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleUpdateForm {


    @NotNull(message = "{id}不能为空")
    @ApiModelProperty(value="角色ID")
    private Long id;

    @ApiModelProperty(value="角色名称")
    private String name;

    @ApiModelProperty(value="权限设置")
    private List<Long> adminAuthList;

    @ApiModelProperty(value="备注")
    private String memo;


}
