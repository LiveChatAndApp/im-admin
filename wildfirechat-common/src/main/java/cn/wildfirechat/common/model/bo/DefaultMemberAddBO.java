package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.DefaultMemberDefaultTypeEnum;
import cn.wildfirechat.common.model.enums.DefaultMemberTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 
 * Business Object 预设好友/群编辑业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMemberAddBO implements Serializable {

    private DefaultMemberTypeEnum defaultMemberTypeEnum; // 类型 1:所有新注册用户, 2:使用邀请码注册用户

    private DefaultMemberDefaultTypeEnum defaultMemberDefaultTypeEnum; // 预设类型 好友:1, 群:2

    private String username; // 预设好友用戶帳號/GID

    private String welcomeText; // 欢迎词

    private String inviteCode; // 邀请码

    private Long inviteCodeId; // 邀请码ID

    private String creator;// 建立者ID

    private String updater;// 更新者ID
}
