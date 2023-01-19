package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 群成员搜寻表
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberQueryForm {

    @ApiModelProperty(value = "群组ID")
    private Long groupId;

    @ApiModelProperty(value = "用户昵称/帐号")
    private String member;
}
