package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 群組新增业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupAddBO {
    private String groupName;
    private String groupImage;
    private String memberName;
    private Integer groupType;
    private String creator;
}
