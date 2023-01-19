package cn.wildfirechat.admin.security.provider;

import cn.wildfirechat.admin.security.SecurityUserMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class UsernamePasswordToken extends UsernamePasswordAuthenticationToken {
    // 用户ID
    private Long userId;
    // 角色ID
    private Long roleId;
    // 聊天帳號ID
    private Long chatId;
    // 聊天帳號UID
    private String chatUID;
    // 角色等级
    private Integer roleLevel;
    // 登录标识vToken
    private String vToken;
    // google验证token
    private String googleToken;
    // totpCode验证码
    private String totpCode;
    // 登录域名，顶级域名
    private String domain;
    // 登录来源，一般值IP
    private String source;
    // 登录装置
    private String device;
    // 菜单
    private Collection<? extends SecurityUserMenu> menus = Collections.emptyList();
    // 品牌
    private String brand;
    // 代理階層路徑
    private String fullPath;

    public UsernamePasswordToken(Object principal, Object credentials, String domain, String source, String device) {
        super(principal, credentials);
        this.domain = domain;
        this.source = source;
        this.device = device;
    }

    public UsernamePasswordToken(Object principal, Object credentials,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
