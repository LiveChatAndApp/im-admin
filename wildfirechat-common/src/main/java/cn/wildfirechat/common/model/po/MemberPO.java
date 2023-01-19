package cn.wildfirechat.common.model.po;

import cn.wildfirechat.common.utils.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 会员列表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPO implements Serializable {

    public static final String MEMBER_TRADE_PWD_SALT = "SX&SLC`";

    private Long id;// ID

    private String uid;// UID

    private String nickName;// 昵称

    private String memberName;// 帐号

    private String tradePwd;// 交易密码

    private Integer accountType;// 帐号类型 1:普通帐号, 2:管理号 [MemberAccountTypeEnum]

    private String inviteCode;// 邀请码

    private Long inviteMemberId;// 邀请用户ID

    private String avatarUrl;// 头像

    private String phone;// 手机号

    private String email;// 邮箱

    private String signature;// 个性签名

    private Integer gender;// 性别 1: 保密, 2: 男, 3: 女 [MemberAccountTypeEnum]

    private Integer loginStatus;// 登陆状态

    private String registerIp;// 注册IP

    private String registerArea;// 注册地区

    private BigDecimal balance;// 馀额

    private BigDecimal freeze;// 冻结金额

    private Boolean loginEnable;// 登陆启用

    private Boolean addFriendEnable;// 添加好友开关

    private Boolean createGroupEnable;// 建群开关

    private Boolean adminEnable;// 管理号开关

    private String channel;// 渠道

    private String memo;// 备注

    private Date createTime;// 创建时间

    private Date updateTime;// 最后修改时间


    //    private Date lastActiveTime;// 最后活跃时间
    private Integer loginErrorCount;// 登陆错误次数

    public static String createMemberName() {
        // return String.format("B%s", DateUtils.format(new Date(), DateUtils.YMDHMSSS));
        return String.format("B%s", UUIDUtils.shortUUID(16).replaceAll("-", ""));
    }
}
