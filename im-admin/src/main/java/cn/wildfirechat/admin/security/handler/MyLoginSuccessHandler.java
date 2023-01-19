package cn.wildfirechat.admin.security.handler;

import cn.wildfirechat.admin.common.support.ResultMsg;
import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.common.utils.WebUtils;
import cn.wildfirechat.admin.constant.ConstWeb;
import cn.wildfirechat.admin.security.JwtTokenUtil;
import cn.wildfirechat.admin.security.exception.UserLoginException;
import cn.wildfirechat.admin.security.provider.UsernamePasswordToken;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    JwtTokenUtil jwtUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 清除验证码
        request.getSession().removeAttribute(ConstWeb.FIELD_IMAGE_CODE);

        // 访问域名
        String host = WebUtils.getDomainName(request);

        if (!(authentication instanceof UsernamePasswordToken)) {
            throw new UserLoginException("登录令牌类型错误");
        }

        // Token
        UsernamePasswordToken token = (UsernamePasswordToken) authentication;
       String name =  (String)token.getPrincipal();
        JSONObject data = new JSONObject();
        data.put("username", token.getPrincipal());
    //    data.put("google", ((SecurityUser) token.getDetails()).isGoogleEnable());
        data.put("menus", token.getMenus());
//        data.put("authList", token.getAuthorities());
        data.put("roleId", token.getRoleId());
        data.put("chatUID", token.getChatUID());
        data.put("roleLevel", token.getRoleLevel());
        data.put("brand", token.getBrand());

        String jwt = jwtUtils.generateJwtToken(authentication);
        data.put("token", jwt);
        redisUtil.set(name,jwt,200000l);

        response.setStatus(HttpStatus.OK.value());
//        response.setContentType("application/json;charset=UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.getWriter().write(objectMapper.writeValueAsString(ResultMsg.success("登录成功", data)));

        log.info("[{}]登录成功 - [{}, {}]", token.getPrincipal(), token.getSource(), host);
    }
}
