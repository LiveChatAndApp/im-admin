package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Form 群成员編輯表
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberEditForm {

    @ApiModelProperty(value = "群组ID")
    private Long groupId;

    @ApiModelProperty(value = "用户id流水号列表(若为群主只能有一个用户)", required = true)
    private List<Long> memberIdList;

    @ApiModelProperty(value = "群成員身分 1: 成员, 2: 管理员, 3: 群主")
    private Integer memberType;
}
