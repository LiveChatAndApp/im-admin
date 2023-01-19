package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 群成员编辑管理者
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberManageEditForm {
    @ApiModelProperty(value = "群组ID")
    private Long groupId;
    @ApiModelProperty(value = "用户id流水号")
    private Long memberId;
    @ApiModelProperty(value = "是否为管理者")
    private Boolean isManager;
}
