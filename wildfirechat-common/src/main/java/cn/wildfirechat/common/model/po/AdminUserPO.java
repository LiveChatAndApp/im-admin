package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Persistant Object 系统管理者持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserPO {
    public static final String PATH_SEPARATOR = "/";
    public static final String PREFIX_SYSTEM_ADMIN = "systemadmin@";
    public static final String Suffix_SYSTEM_ADMIN = "(admin)";

    private Long id;
    private Long roleId;
    private String username;
    private String nickname;
    private Long memberId;
    private String password;
    private Integer status;
    private Long parentId;
    private String fullPath;
    private String brandName;
    private String memo;
    private String phone;
    private String email;
    private Integer loginError;
    private Date loginFrozen;
    private Integer loginSucceed;
    private Integer loginFail;
    private Date loginTime;
    private String loginIp;
    private Date registerTime;
    private Date createTime;
    private Date updateTime;
    private String otpKey;
    private Integer isVerifyOtpKey;
}
