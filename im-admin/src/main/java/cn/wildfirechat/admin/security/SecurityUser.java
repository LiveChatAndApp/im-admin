package cn.wildfirechat.admin.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;

/**
 * Security 安全校验实体-用于封转登陆用户的身份信息和权限信息
 * 实质上是对 org.springframework.security.core.userdetails.UserDetails 接口的实现
 * org.springframework.security.core.userdetails.User 提供了构造方法，便于我们业务用户实体和Security 校验身份的实体分离
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;

    private Long adminId;           // 管理员ID
    private String brandName;       // 品牌
    private Long chatId;           // 聊天帐号ID
    private String chatUID;       // 聊天帐号UID
    private Long roleId;            // 角色ID
    private Integer roleLevel;      // 角色等级
    private String fullPath;        // 代理階層路徑
    private boolean googleEnable;   // 谷歌验证开关
    private String googleSecret;    // 谷歌验证密钥
    private String otpKey;          // OTP验证密钥
    private Integer isVerifyOtpKey; // 是否通过OTP绑定验证
    private boolean sourceSecure;

    private Collection<? extends SecurityUserMenu> menus; //自定义权限菜单

    public SecurityUser(Long chatId, String chatUID, Long adminId, String username, String password, boolean enabled, boolean accountNonExpired,
                        boolean credentialsNonExpired, boolean accountNonLocked, boolean sourceSecure,
                        boolean googleEnable, String googleSecret, Collection<? extends GrantedAuthority> authorities,
                        Collection<? extends SecurityUserMenu> menus, String brandName, Long roleId, Integer roleLevel,
                        String fullPath, String otpKey, Integer isVerifyOtpKey) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.chatId = chatId;
        this.chatUID = chatUID;
        this.adminId = adminId;
        this.googleEnable = googleEnable;
        this.googleSecret = googleSecret;
        this.sourceSecure = sourceSecure;
        this.menus = menus;
        this.brandName = brandName;
        this.roleId = roleId;
        this.roleLevel = roleLevel;
        this.fullPath = fullPath;
        this.otpKey = otpKey;
        this.isVerifyOtpKey = isVerifyOtpKey;
    }
}
