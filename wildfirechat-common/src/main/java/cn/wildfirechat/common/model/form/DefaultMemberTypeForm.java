package cn.wildfirechat.common.model.form;


import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DefaultMemberTypeForm {

    @ApiModelProperty(value = "预设好友模式 1: 所有, 2: 轮询")
    private Integer defaultMemberType;

}
