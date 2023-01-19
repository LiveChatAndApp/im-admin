package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 系统角色明细持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleInfoPO implements Serializable {
    private Long id;// 管理员ID
    private Long chatId; // 聊天帐号ID
    private String chatUID; // 聊天帐号UID
    private Long roleId;// 角色ID
    private String nickname;// 用户昵称
    private String username;// 用户名
    private Integer status;// 账户状态
    private String password;// 密码
    private String fullPath;// 代理階層路徑
    private String brandName;// 品牌

    private String googleSecret;// GA密钥
    private Boolean googleEnable;// GA开关
    private Integer loginError;// 连续登录失败次数
    private Date loginFrozen;// 登录冻结时间
    private Integer loginSucceed;// 登录成功次数
    private Integer loginFail;// 登录失败次数
    private Date loginTime;// 最后登录时间
    private String loginIp;// 最后登录IP
    private Date registerTime;// 注册时间
    private String otpKey;              // OTP密钥
    private Integer isVerifyOtpKey;     // 是否通过OTP绑定验证

    private Integer roleLevel;// 角色等级
}
