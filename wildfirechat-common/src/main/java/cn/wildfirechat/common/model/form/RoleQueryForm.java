package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RoleQueryForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="角色ID")
    private Long id;

    @ApiModelProperty(value="角色名称")
    private String name;

    @ApiModelProperty(value="开始创建时间")
    private Date createTimeGe;

    @ApiModelProperty(value="结束创建时间")
    private Date createTimeLe;

    @ApiModelProperty(value="级别 1:管理员, 2:员工")
    private Integer level;


}
