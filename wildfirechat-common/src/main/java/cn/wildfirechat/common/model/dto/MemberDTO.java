package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object 会员数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;// ID
    private String uid;// UID
    private String nickName;// 昵称
    private String memberName;// 账号
    private String password;// 密码
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
    private Boolean loginEnable;// 登陆启用
    private Boolean addFriendEnable;// 添加好友开关
    private Boolean createGroupEnable;// 建群开关
    private Boolean adminEnable;// 管理号开关
    private String memo;// 备注
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
}
