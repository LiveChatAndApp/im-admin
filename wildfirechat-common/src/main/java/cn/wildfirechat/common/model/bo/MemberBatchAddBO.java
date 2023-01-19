package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.MemberAccountTypeEnum;
import cn.wildfirechat.common.model.enums.MemberAvatarTypeEnum;
import cn.wildfirechat.common.model.enums.MemberNickNameTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 会员业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBatchAddBO {
    private MemberAccountTypeEnum accountType;// 帐号类型
    private String prefix;// 前缀
    private MemberNickNameTypeEnum nickNameType;// 昵称类型 1:随机昵称, 2:自定义昵称
    private String nickName;// 昵称
    private MemberAvatarTypeEnum avatarType;// 头像类型 1:系统默认, 2:随机生成
    private String loginPwd;// 登陆密码
    private Integer batchCount;// 批量数量
    private String channel;// 渠道
}
