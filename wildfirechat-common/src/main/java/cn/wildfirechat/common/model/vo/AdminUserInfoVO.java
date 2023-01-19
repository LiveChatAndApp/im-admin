package cn.wildfirechat.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * View Object 系统管理者显示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserInfoVO {

    private Long id;
    private Long roleId;
    private String roleName;
    private String username;
    private String nickname;
    private Integer status;
    private Date loginTime;
    private String loginIp;
    private Date registerTime;
    private Date createTime;
    private Date updateTime;
    private Integer isVerifyOtpKey;
}
