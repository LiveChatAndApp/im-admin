package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleAddForm {

    @ApiModelProperty(value="角色名称")
    private String name;

    @ApiModelProperty(value="角色层级")
    private Integer level;//[[AdminRoleLevelEnum]]

    @ApiModelProperty(value="权限设置")
    private List<Long> adminAuthList;

    @ApiModelProperty(value="备注")
    private String memo;

}
