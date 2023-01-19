package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.GroupMemberTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Business Object 群組成员新增业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberEditBO {
    private Long groupId; // 群组id

    private List<Long> memberIdList; // 用户id流水号列表

    private GroupMemberTypeEnum groupMemberTypeEnum; // 成员类型
}
