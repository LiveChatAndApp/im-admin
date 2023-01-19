package cn.wildfirechat.common.model.bo;

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
public class GroupMemberAddBO {
    private Long groupId; // 群组id

    private List<Long> memberIdList; // 用户id流水号列表

    private Integer verify; // 验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除 [RelateVerifyEnum]

    private String verifyText; // 验证消息
}
