package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 邀请码
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodePO implements Serializable {
    private Long id;// 流水号ID

    private String inviteCode; // 邀请码

    private Integer friendsDefaultType; // 预设好友模式 1: 所有, 2: 轮询[InviteCodeDefaultFriendTypeEnum]

    private Integer status; // 状态 0: 停用, 1: 使用中 [InviteCodeStatusEnum]

    private String memo; // 后台备注

    private Date createTime;// 创建时间

    private Date updateTime;// 最后修改时间

    private String creator; // 创建者

    private String updater; // 修改者

    private Integer creatorRole;// 创建者角色 1: 系统管理者, 2: 会员 [EditorRoleEnum]

    private Integer updaterRole;// 修改者角色 1: 系统管理者, 2: 会员 [EditorRoleEnum]
}
