package cn.wildfirechat.common.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理员
 */
@Data
@NoArgsConstructor
public class AdminUserAuthInfoDTO {
    private Long id;                // 管理员ID
    private Long roleId;            // 角色ID
    private String nickname;        // 用户昵称
    private String username;        // 用户名
    private Integer status;         // 账户状态
//    private String password;        // 密码
    private String fullPath;        // 代理階層路徑
    private String brandName;       // 品牌

//    private String googleSecret;    // GA密钥
//    private Boolean googleEnable;   // GA开关
//    private Integer loginError;     // 连续登录失败次数
//    private Date loginFrozen;       // 登录冻结时间
//    private Integer loginSucceed;   // 登录成功次数
//    private Integer loginFail;      // 登录失败次数
//    private Date loginTime;         // 最后登录时间
//    private String loginIp;         // 最后登录IP
//    private Date registerTime;      // 注册时间
//    private String registerIp;      // 注册IP
//    private String remark;          // 备注信息
//    private String phone;           // 手机号码
//    private String email;           // 邮箱地址
//    private String qq;              // QQ号码
//    private String wechat;          // 微信号码

    private Integer roleLevel;      // 角色等级

    public AdminUserAuthInfoDTO(Long id) {
        this.id = id;
    }

}
