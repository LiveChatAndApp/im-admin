package cn.wildfirechat.admin.security.handler;

import cn.wildfirechat.admin.common.support.ResultMsg;
import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.common.utils.IpTools;
import cn.wildfirechat.admin.common.utils.UserAgentUtils;
import cn.wildfirechat.admin.common.utils.WebUtils;
import cn.wildfirechat.admin.security.provider.UsernamePasswordToken;
import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LogService logService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 登录IP
        String ip = IpTools.getIpAddr(request);
        // 访问域名
        String host = WebUtils.getDomainName(request);

        // 用户名
        String principal = null;
        // 登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authentication;
        if (token != null) {
            principal = (String) token.getPrincipal();
        }

        // 新增日志(登出成功)
        OperateLogList list = new OperateLogList();
        list.addLog("用户账号", principal, false);
//        list.addLog("操作设备", getDevice(request), false);
        logService.addLogInOutOperateLog("/logout", list, ip, token.getUserId().toString(), principal, token.getRoleLevel());

        if (principal != null) {
            redisUtil.delete(principal);
        }
        // 清除身份认证信息
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();

        request.getSession().invalidate();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultMsg.success(200, "退出成功", "ok")));

        log.info("[{}]退出登录 - [{}, {}]", principal, ip, host);
    }

    /**
     * 获取登录装置
     */
    private String getDevice(HttpServletRequest request) {
        return UserAgentUtils.getDeviceType(UserAgentUtils.getUserAgent(request));
    }

}
