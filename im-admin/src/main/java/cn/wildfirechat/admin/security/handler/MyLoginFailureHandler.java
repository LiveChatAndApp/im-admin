package cn.wildfirechat.admin.security.handler;

import cn.wildfirechat.admin.common.support.ResultMsg;
import cn.wildfirechat.admin.common.utils.IpTools;
import cn.wildfirechat.admin.common.utils.WebUtils;
import cn.wildfirechat.admin.constant.ConstWeb;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 清除验证码
        request.getSession().removeAttribute(ConstWeb.FIELD_IMAGE_CODE);

        // 失败原因
        String reason = exception.getMessage();

        // 登录账号
        String username = request.getParameter("username");
        // 登录密码
        String password = request.getParameter("password");
        // 登录IP
        String ip = IpTools.getIpAddr(request);
        // 访问域名
        String host = WebUtils.getDomainName(request);

        if (StringUtils.isBlank(username) || exception instanceof UsernameNotFoundException) {
            log.info("非用户[{}]尝试登录: password={}, ip={}, {}", username, password, ip, host);
        } else {
//            Admin admin = adminService.getByUsername(username);
//            if (admin != null && AdminStatusEnum.NORMAL.getValue() == admin.getStatus()) {
//                Admin mod = new Admin();
//                mod.setId(admin.getId());
//                mod.setLoginError(admin.getLoginError() + 1);
//                mod.setLoginFail(admin.getLoginFail() + 1);
//                mod.setLoginTime(new Date());
//                mod.setLoginIp(IpTools.getIpAddr(request));
//                if (mod.getLoginError() >= 3) {
//                    mod.setStatus(AdminStatusEnum.FROZEN.getValue());
//                }
//
//                // 更新用户信息
//                try {
//                    adminService.update(mod);
//                } catch (Exception e) {
//                    log.error("[{}]登录失败，更新admin信息出错", username, e);
//                }
//
//                if (mod.getStatus() != null && AdminStatusEnum.FROZEN.getValue() == mod.getStatus()) {
//                    reason = "帐户冻结[" + mod.getLoginError() + "]";
//                }
//
//                log.info("[{}]登录失败 - [{}, {}], {}", username, mod.getLoginIp(), host, reason);
//            }
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultMsg.failure(reason)));
    }

}
