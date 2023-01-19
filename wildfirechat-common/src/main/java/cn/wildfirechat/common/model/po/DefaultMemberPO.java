package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 预设好友/群
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMemberPO implements Serializable {
    private Long id;// 流水号ID

    private Integer defaultType;// 预设类型 好友:1, 群:2 [DefaultMemberDefaultTypeEnum]

    private Long memberId; // 预设好友ID

    private Long groupId; // 预设群ID

    private String welcomeText; // 欢迎词

    private Integer type; // 类型 1:所有新注册用户, 2:使用邀请码注册用户 [DefaultMemberTypeEnum]

    private Long inviteCodeId; // 邀请码ID, 0为全部新加入者

    private Date createTime; // 创建时间

    private Date updateTime; // 修改时间

    private String creator; // 创建者

    private String updater; // 修改者
}
