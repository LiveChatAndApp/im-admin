package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Form 群成员新增表
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberAddForm {

    @ApiModelProperty(value = "群组ID")
    private Long groupId;

    @ApiModelProperty(value = "用户id流水号列表")
    private List<Long> memberIdList;
}
