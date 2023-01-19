package cn.wildfirechat.common.model.po;

import cn.wildfirechat.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 群
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPO implements Serializable {
    public static final int DEFAULT_MEMBER_COUNT_LIMIT = 2000;
    private Long id;// ID
    private String gid;// 群组ID
    private String name;// 群名称(群信息)
    private Long managerId;// 群主
    private Integer memberCount;// 群人数
    private Integer memberCountLimit;// 群人数上限
    private String groupImage;// 群头像图片
    private Integer muteType;// 禁言 0:正常 1:禁言普通成员 2:禁言整个群 [GroupMuteTypeEnum]
    private Integer enterAuthType;// 入群验证类型 1:无需验证 2:管理员验证 3:不允许入群验证 [GroupEnterAuthTypeEnum]
    private Integer invitePermission;// 邀请权限(谁可以邀请他人入群) 1:管理员 2:所有人 [GroupInvitePermissionEnum]
    private Integer inviteAuth;// 被邀请人身份验证 0:不需要同意 1:需要同意 [GroupInviteAuthEnum]
    private Integer modifyPermission;// 群资料修改权限 1:管理员 2:所有人 [GroupModifyPermissionEnum]
    private Integer privateChat;// 私聊 0: 禁止, 1: 正常 [GroupPrivateChatEnum]
    private String bulletinTitle;// 群公告标题
    private String bulletinContent;// 群公告内容
    private Integer status;// 状态 1: 正常, 2: 解散 [GroupStatusEnum]
    private Integer groupType;// 群组类型 1:一般,2:广播 [GroupTypeEnum]
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
    private String creator;// 创建者
    private String updater;// 修改者
    private Integer creatorRole;// 创建者角色 1: 系统管理者, 2: 会员 [EditorRoleEnum]
    private Integer updaterRole;// 修改者角色 1: 系统管理者, 2: 会员 [EditorRoleEnum]
    public static String createGid() {
        return String.format("G%s", DateUtils.format(new Date(), DateUtils.YMDHMSSS));
    }
}
