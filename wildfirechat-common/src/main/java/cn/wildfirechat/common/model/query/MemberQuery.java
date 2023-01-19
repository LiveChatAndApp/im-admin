package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Query Object 会员查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberQuery {
    private Long id;// ID

    private String uid;// UID

    private String nickName;// 昵称

    private String memberName;// 帐号

    private Integer accountType;// 帐号类型 1:普通帐号, 2:管理号

    private String inviteCode;// 邀请码

    private Long inviteMemberId;// 邀请用户ID

    private String avatarUrl;// 头像

    private String phone;// 手机号

    private String email;// 邮箱

    private String signature;// 个性签名

    private Integer gender;// 性别

    private Integer loginStatus;// 登陆状态

    private Date lastActiveTime;// 最后活跃时间

    private Integer loginErrorCount;// 登陆错误次数

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

    private Date createTimeGt;// 创建时间

    private Date createTimeLe;// 创建时间

    private Date updateTimeGt;// 最后修改时间

    private Date updateTimeLe;// 最后修改时间
}
