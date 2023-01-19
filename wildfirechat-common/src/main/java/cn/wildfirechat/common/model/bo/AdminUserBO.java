package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Business Object 系统管理者业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserBO {

    private Long id;
    private Long roleId;
    private String username;
    private String nickname;
    private String password;
    private Integer status;
    private Long parentId;
    private String fullPath;
    private String brandName;
    private String memo;
    private String channel;
    private String phone;
    private String email;
    private Integer loginError;
    private Date loginFrozen;
    private Integer loginSucceed;
    private Integer loginFail;
    private Date loginTime;
    private Date loginIp;
    private Date registerTime;
    private Date createTime;
    private Date updateTime;
    private String otpKey;
    private Integer isVerifyOtpKey;
}
