package cn.wildfirechat.admin.security.provider;

import cn.wildfirechat.admin.common.utils.IpTools;
import cn.wildfirechat.admin.common.utils.UserAgentUtils;
import cn.wildfirechat.admin.common.utils.WebUtils;
import cn.wildfirechat.admin.constant.ConstWeb;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * 重载UsernamePasswordAuthenticationFilter的attemptAuthentication,
 * obtainUsername,obtainPassword方法(完善逻辑) 增加验证码校验模块 添加验证码属性 添加验证码功能开关属性
 * </pre>
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // 验证码字段
    private String validateCodeParameter = "validateCode";

    // 是否开启验证码功能
    private boolean openValidateCode = false;

    // post请求
    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 判断是不是post请求
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = null;
        String password = null;
        String validateCode = null;
        String totpCode = null;
        // json post 格式
        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
             username = requestMap.get("username");
             password = requestMap.get("password");
             validateCode = requestMap.get("checkcode");
             totpCode = requestMap.get("totpCode");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 开启验证码功能的情况
        if (openValidateCode) {
            checkValidateCode(request, validateCode);
        }
        String domain = obtainTopDomain(request);
        // 获取Username和Password
      //   username = obtainUsername(request);
       //  password = obtainPassword(request);
        String googleToken = request.getParameter("googleToken");
        String vToken = request.getParameter("vToken");
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();

        // UsernamePasswordToken实现Authentication校验
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, domain, IpTools.getIpAddr(request), getDevice(request));
        // 允许子类设置详细属性
        setDetails(request, token);
        token.setGoogleToken(googleToken);
        token.setVToken(vToken);
        token.setTotpCode(totpCode);
        // 用户名密码验证通过后,注册session
        // sessionRegistry.registerNewSession(request.getSession().getId(), token.getPrincipal());
        // 运行UserDetailsService的loadUserByUsername 再次封装Authentication
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 匹对验证码的正确性
     */
    private void checkValidateCode(HttpServletRequest request) {
        String validateCode = obtainValidateCodeParameter(request);
        if (validateCode == null || "".equals(validateCode)) {
            throw new AuthenticationServiceException("请输入验证码！");
        }
        if (request.getSession().getAttribute(ConstWeb.FIELD_IMAGE_CODE) == null) {
            throw new AuthenticationServiceException("验证码失效！");
        }
        // 对比普通验证码
        if (!request.getSession().getAttribute(ConstWeb.FIELD_IMAGE_CODE).equals(validateCode)) {
            throw new AuthenticationServiceException("验证码错误！");
        }
    }

    private void checkValidateCode(HttpServletRequest request, String checkCode) {
        String validateCode = checkCode;
        if (validateCode == null || "".equals(validateCode)) {
            throw new AuthenticationServiceException("请输入验证码！");
        }
        if (request.getSession().getAttribute(ConstWeb.FIELD_IMAGE_CODE) == null) {
            throw new AuthenticationServiceException("验证码失效！");
        }
        // 对比普通验证码
        if (!request.getSession().getAttribute(ConstWeb.FIELD_IMAGE_CODE).equals(validateCode)) {
            throw new AuthenticationServiceException("验证码错误！");
        }
    }
    /**
     * 获取验证码
     */
    private String obtainValidateCodeParameter(HttpServletRequest request) {
        return request.getParameter(validateCodeParameter);
    }

    /**
     * 设置验证码字段名
     */
    public void setValidateCodeParameter(String validateCode) {
        Assert.hasText(validateCode, "validateCode parameter must not be empty or null");
        this.validateCodeParameter = validateCode;
    }

    /**
     * 设置验证码校验开关
     */
    public void setOpenValidateCode(boolean openValidateCode) {
        this.openValidateCode = openValidateCode;
    }

    /**
     * 获取登录顶级域名
     */
    private String obtainTopDomain(HttpServletRequest request) {
        return WebUtils.getTopDomain(request);
    }

    /**
     * 获取登录装置
     */
    private String getDevice(HttpServletRequest request) {
        return UserAgentUtils.getDeviceType(UserAgentUtils.getUserAgent(request));
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

}

