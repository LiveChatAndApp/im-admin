package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 邀请码新增业务对象
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeAddBO {
    private String inviteCode; // 邀请码

    private String memo; // 后台备注

    private String username; // 预设好友用戶帐号

    private String welcomeText; // 欢迎词

    private Integer friendsDefaultType; // 预设好友模式 1: 所有, 2: 轮询[InviteCodeDefaultFriendTypeEnum]

    private Integer status; // 状态 0: 停用, 1: 使用中 [InviteCodeStatusEnum]

    private String creator; // 创建者

    private String updater; // 修改者
}
