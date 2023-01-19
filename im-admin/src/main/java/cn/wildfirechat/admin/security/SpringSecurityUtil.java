package cn.wildfirechat.admin.security;

import cn.wildfirechat.admin.security.provider.UsernamePasswordToken;
import cn.wildfirechat.common.model.po.AdminUserPO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Collection;

/**
 * 用户安全工具类
 *
 * <p>
 * 该类主要结合SpringSecurity框架，提供：
 * <ul>
 * <li>检查是否含有权限</li>
 * <li>获取登录名</li>
 * <li>获取登录令牌</li>
 * <li>获取登录源</li>
 * <li>获取登录类型</li>
 * </ul>
 * </p>
 */
public class SpringSecurityUtil {

    /**
     * 用户是否已经认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordToken) {
            return authentication.isAuthenticated();
        }
        return false;
    }

    /**
     * 是否有权限
     *
     * @param auth 权限名称
     */
    public static boolean hasAuth(String auth) {
        if (!StringUtils.hasText(auth)) {
            return false;
        }

        UsernamePasswordToken token = getToken();
        if (token != null && !CollectionUtils.isEmpty(token.getAuthorities())) {
            for (GrantedAuthority granted : token.getAuthorities()) {
                if (auth.equals(granted.getAuthority())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取登录用户名
     */
    public static String getPrincipal() {
        UsernamePasswordToken token = getToken();
        return token != null ? (String) token.getPrincipal() : null;
    }

    /**
     * 获取登录用户名 + 后坠
     */
    public static String getSuffixUsername() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getPrincipal() + AdminUserPO.Suffix_SYSTEM_ADMIN : null;
    }

    /**
     * 获取Long型的令牌
     */
    public static Long getUserId() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getUserId() : null;
    }

    /**
     * 获取聊天帐号ID
     */
    public static Long getChatId() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getChatId() : null;
    }

    /**
     * 获取聊天帐号UID
     */
    public static String getChatUID() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getChatUID() : null;
    }

    /**
     * 获取来源
     */
    public static String getSource() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getSource() : null;
    }

    /**
     * 获取菜单
     */
    public static Collection<? extends SecurityUserMenu> getMenus() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getMenus() : null;
    }

    /**
     * 获取Brand品牌
     */
    public static String getBrand() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getBrand() : null;
    }

    /**
     * 获取登录角色id
     */
    public static Long getRoleId() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getRoleId() : null;
    }

    /**
     * 获取登录角色等级
     */
    public static Integer getRoleLevel() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getRoleLevel() : null;
    }

    /**
     * 获取代理階層路徑
     */
    public static String getFullPath() {
        UsernamePasswordToken token = getToken();
        return token != null ? token.getFullPath() : null;
    }

    /**
     * 获取登录Token对象
     */
    public static UsernamePasswordToken getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordToken) {
            return (UsernamePasswordToken) authentication;
        }
        return null;
    }

}
