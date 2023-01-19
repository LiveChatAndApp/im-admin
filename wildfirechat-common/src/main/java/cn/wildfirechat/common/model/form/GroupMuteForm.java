package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 群禁言
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMuteForm {
    @ApiModelProperty(value = "群組ID")
    private String groupId;

    @ApiModelProperty(value = "禁言 0:正常 1:禁言普通成员 2:禁言整个群")
    private Integer muteType;
}
