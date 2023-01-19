package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 群成员编辑群主
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberOwnerEditForm {
    @ApiModelProperty(value = "群组ID")
    private Long groupId;
    @ApiModelProperty(value = "新群主用户id流水号")
    private Long memberId;
}
