package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.GroupMuteTypeEnum;
import cn.wildfirechat.common.model.enums.GroupPrivateChatEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 群組更新业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateBO {

    private String groupId; // 群組ID

    private String name; // 群組名稱

    private String groupImage; // 群組圖片

    private String memberName; // 群主帐号

    private String updater; // 更新者ID

}
