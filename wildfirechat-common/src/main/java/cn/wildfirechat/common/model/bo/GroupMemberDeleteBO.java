package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.EditorRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Business Object 群組成员刪除业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDeleteBO {
    private Long groupId; // 群组id
    private List<Long> memberIdList; // 用户id流水号列表
    private Boolean isManager;
}
