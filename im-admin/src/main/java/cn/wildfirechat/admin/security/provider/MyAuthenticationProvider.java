package cn.wildfirechat.admin.security.provider;

import cn.wildfirechat.admin.common.enums.AdminUserStatusEnum;
import cn.wildfirechat.admin.common.strategy.PasswordUtil;
import cn.wildfirechat.admin.common.support.GoogleAuthenticator;
import cn.wildfirechat.admin.common.utils.OtpUtil;
import cn.wildfirechat.admin.common.utils.UserAgentUtils;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.AdminUserService;
import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.po.AdminRoleInfoPO;
import cn.wildfirechat.admin.security.SecurityUser;
import cn.wildfirechat.admin.security.SecurityUserMenu;
import cn.wildfirechat.admin.security.exception.UserLoginException;
import cn.wildfirechat.admin.service.AdminAuthService;
import cn.wildfirechat.admin.service.AdminRoleService;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.support.SpringMessage;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 登录校验逻辑
 */
@Slf4j
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private AdminAuthService adminAuthService;

    @Resource
    protected SpringMessage message;

    @Autowired
    private LogService logService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordToken)) {
            throw new UserLoginException("登录令牌类型错误");
        }
        UsernamePasswordToken token = (UsernamePasswordToken) authentication;

        String username = token.getPrincipal().toString();
        String password = token.getCredentials().toString();
        String domain = token.getDomain();
        String source = token.getSource();
        String device = token.getDevice();
        String vToken = token.getVToken();
        String googleToken = token.getGoogleToken();

        // 如果userDetails不为空重新构建UsernamePasswordToken（已认证）
        SecurityUser userDetails = loadUserByUsername(username, domain);

        if (userDetails == null) {
            throw new UsernameNotFoundException("登录失败:" + username);
        }

        if (!userDetails.isAccountNonLocked()) {
            throw new UserLoginException("账号已禁用..."); // 账号已冻结
        } else if (!userDetails.isEnabled()) {
            throw new UserLoginException("账号已禁用..."); // 账号已禁用
        } else if (!userDetails.isCredentialsNonExpired()) {
            throw new UserLoginException("用户密码已过期");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new UserLoginException("用户帐户已过期");
        } else if (!userDetails.isSourceSecure()) {
            throw new UserLoginException("用户来源限制");
        } else {
            if (!PasswordUtil.matchesA(password, userDetails.getPassword())) {
                throw new UserLoginException("用户名或密码错误");
            }
        }

        //otp验证
        if (userDetails.getIsVerifyOtpKey() == 1){
            if(StringUtil.isEmpty(token.getTotpCode())){
                throw new UserLoginException(message.get(I18nAdmin.ADMIN_USER_OTP_IS_REQUIRED)); //此账号已绑定OTP，请输入OTP验证码登录
            }
            boolean isSuccess = OtpUtil.verifyTotpCode(userDetails.getOtpKey(), token.getTotpCode());
            if (!isSuccess){
                throw new UserLoginException(message.get(I18nAdmin.ADMIN_USER_VERIFY_OTP_FAILURE)); //OTP验证码错误
            }
        }

        if (userDetails.isGoogleEnable()) {
            if (StringUtils.isBlank(googleToken)) {
                throw new UserLoginException("谷歌验证码不能为空");
            }
            // 验证
            GoogleAuthenticator ga = new GoogleAuthenticator();
            ga.setWindowSize(10);
            boolean check = ga.check_code(userDetails.getGoogleSecret(), googleToken);
            if (!check) {
                throw new UserLoginException("谷歌验证码错误或已过期");
            }
        }

        // 如果userDetails不为空重新构建UsernamePasswordToken（已认证）
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userDetails.getUsername(),
                "[protected]",
                userDetails.getAuthorities());
        usernamePasswordToken.setDetails(userDetails);
        usernamePasswordToken.setUserId(userDetails.getAdminId());
        usernamePasswordToken.setChatId(userDetails.getChatId());
        usernamePasswordToken.setChatUID(userDetails.getChatUID());
        usernamePasswordToken.setDomain(domain);
        usernamePasswordToken.setSource(source);
        usernamePasswordToken.setBrand(userDetails.getBrandName());
        usernamePasswordToken.setRoleId(userDetails.getRoleId());
        usernamePasswordToken.setRoleLevel(userDetails.getRoleLevel());
        usernamePasswordToken.setVToken(vToken);
        usernamePasswordToken.setMenus(userDetails.getMenus());


        // 新增日志(登录成功)
        OperateLogList list = new OperateLogList();
        list.addLog("用户账号", userDetails.getUsername(), false);
//        list.addLog("操作设备", device, false);
        logService.addLogInOutOperateLog("/login", list, source, userDetails.getAdminId().toString(), userDetails.getUsername(), userDetails.getRoleLevel());


        //更新用户登入资讯
        adminUserService.updateLoginInfo(AdminUserPO.builder().id(userDetails.getAdminId()).loginTime(new Date()).loginIp(source).build());

        return usernamePasswordToken;
    }

    public SecurityUser loadUserByUsername(String username, String domain) {
        AdminRoleInfoPO info = adminRoleService.getRoleInfoByUsername(username);
        if (info == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 角色权限
        List<SimpleGrantedAuthority> adminAuths = getAdminAuths(info);

        // 角色菜单
        List<SecurityUserMenu> adminMenus = getAdminMenus(info);

        // 可用性 :true:可用 false:不可用
        boolean enabled = AdminUserStatusEnum.DISABLE.getValue() != info.getStatus();
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = AdminUserStatusEnum.DISABLE.getValue() != info.getStatus();
        // 来源性 :true:限制 false:不限制
        boolean sourceSecure = true;
        // GA验证：是否开启
        boolean googleEnable = Optional.ofNullable(info.getGoogleEnable()).orElse(false);

        return new SecurityUser(info.getChatId(),
                info.getChatUID(),
                info.getId(),
                info.getUsername(),
                info.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                sourceSecure,
                googleEnable,
                info.getGoogleSecret(),
                adminAuths, adminMenus,
                info.getBrandName(),
                info.getRoleId(),
                info.getRoleLevel(),
                info.getFullPath(),
                info.getOtpKey(),
                info.getIsVerifyOtpKey());
    }

    /**
     * 管理员权限
     */
    private List<SimpleGrantedAuthority> getAdminAuths(AdminRoleInfoPO admin) {
        if (admin == null || AdminUserStatusEnum.NORMAL.getValue() != admin.getStatus()) {
            return Collections.emptyList();
        }
        log.info("getAdminAuths roleId: {}", admin.getRoleId());
        List<AdminAuthPO> list = adminAuthService.getByRoleId(admin.getRoleId());

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<SimpleGrantedAuthority> auths = new ArrayList<>();
//        log.info("----------list: {}", list);
        for (AdminAuthPO auth : list) {
            auths.add(new SimpleGrantedAuthority(auth.getCode()));
        }
        return auths;
    }

    /**
     * 管理员菜单
     */
    private List<SecurityUserMenu> getAdminMenus(AdminRoleInfoPO admin) {
        if (admin == null || AdminUserStatusEnum.NORMAL.getValue() != admin.getStatus()) {
            return Collections.emptyList();
        }

        List<AdminAuthPO> adminAuthlist = adminAuthService.getByRoleId(admin.getRoleId());
        if (CollectionUtils.isEmpty(adminAuthlist)) {
            return Collections.emptyList();
        }

        List<SecurityUserMenu> menus = new ArrayList<>();
        for (AdminAuthPO auth : adminAuthlist) {
            SecurityUserMenu userMenu = new SecurityUserMenu(auth.getId(), auth.getParentId(), auth.getName(), auth.getCode(), auth.getApi(), auth.getOrder());
            menus.add(userMenu);
        }
        return menus;
    }

    /**
     * 只有Authentication为UsernamePasswordToken使用此Provider认证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordToken.class.isAssignableFrom(authentication);
    }


}
