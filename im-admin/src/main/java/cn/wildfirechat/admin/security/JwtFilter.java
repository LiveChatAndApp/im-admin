package cn.wildfirechat.admin.security;

import cn.wildfirechat.admin.common.enums.AdminUserStatusEnum;
import cn.wildfirechat.admin.common.support.ResultMsg;
import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.common.utils.IpTools;
import cn.wildfirechat.admin.security.provider.UsernamePasswordToken;
import cn.wildfirechat.admin.service.AdminAuthService;
import cn.wildfirechat.admin.service.AdminRoleService;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.po.AdminRoleInfoPO;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
	@Resource
	private AdminRoleService adminRoleService;

	@Resource
	private AdminAuthService adminAuthService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RedisUtil redisUtil;

	private List<SimpleGrantedAuthority> getAdminAuths(AdminRoleInfoPO po) {
		if (po == null || AdminUserStatusEnum.NORMAL.getValue() != po.getStatus()) {
			return Collections.emptyList();
		}

		List<AdminAuthPO> list = adminAuthService.getByRoleId(po.getRoleId());

		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}

		List<SimpleGrantedAuthority> auths = new ArrayList<>();
		for (AdminAuthPO auth : list) {
			auths.add(new SimpleGrantedAuthority(auth.getCode()));
		}
		return auths;
	}

	public SecurityUser loadUserByUsername(String username, String domain) {
		AdminRoleInfoPO info = adminRoleService.getRoleInfoByUsername(username);
		if (info == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		// 角色权限
		List<SimpleGrantedAuthority> adminAuths = getAdminAuths(info);

		List<SecurityUserMenu> adminMenus = Collections.emptyList();

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

		return new SecurityUser(info.getChatId(), info.getChatUID(), info.getId(), info.getUsername(), info.getPassword(), enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, sourceSecure, googleEnable, info.getGoogleSecret(), adminAuths,
				adminMenus, info.getBrandName(), info.getRoleId(), info.getRoleLevel(), info.getFullPath(), info.getOtpKey(), info.getIsVerifyOtpKey());
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		String[] swagger_whitelist = {"/swagger-ui.html", "/swagger-ui", "/swagger-resources", "/v2/api-docs",
				"/v3/api-docs", "/v3/api-docs/swagger-config", "/webjars", "/doc.html"};
		return Arrays.stream(swagger_whitelist).anyMatch(url -> request.getServletPath().startsWith(url));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
		final String ip = IpTools.getIpAddr(request);
		log.info("request ip:{}, url:{}, requestTokenHeader:{}", ip, request.getServletPath(),
				requestTokenHeader);

		if (request.getServletPath().startsWith("/login") || request.getServletPath().startsWith("/code/image")
				|| request.getServletPath().startsWith("/session/get")) {
			chain.doFilter(request, response);
			return;
		}
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the
		// Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUserNameFromJwtToken(jwtToken);
				log.info("JwtFilter:: username: {} , parse from requestTokenHeader: {}", username, requestTokenHeader);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
			if (!"knight4j".equals(username) && StringUtil.isNotEmpty(username)) { // knight4j为测试用,不检验Redis过期登入,但会受jwt过期限制影响(24h)
				if (!redisUtil.exists(username) || !redisUtil.get(username).equals(jwtToken)) {
					logger.warn("JWT Token does not begin with Bearer String");
					response.setStatus(HttpStatus.OK.value());
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(JSONObject.toJSONString(ResultMsg.failure(401, "登录过期")));
					return;
				}
			}
		} else {
			if (request.getRequestURI().startsWith("/admin")) {
				logger.info("JWT Token does not begin with Bearer String");
			}
			response.setStatus(HttpStatus.OK.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONObject.toJSONString(ResultMsg.failure(401, "登录过期")));
			return;
		}

		if (username != null) {
			SecurityUser userDetails = this.loadUserByUsername(username, null);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordToken usernamePasswordAuthenticationToken = new UsernamePasswordToken(
						userDetails.getUsername(), "[protected]", userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				usernamePasswordAuthenticationToken.setUserId(userDetails.getAdminId());
				usernamePasswordAuthenticationToken.setRoleId(userDetails.getRoleId());
				usernamePasswordAuthenticationToken.setChatId(userDetails.getChatId());
				usernamePasswordAuthenticationToken.setChatUID(userDetails.getChatUID());
				usernamePasswordAuthenticationToken.setRoleLevel(userDetails.getRoleLevel());
				usernamePasswordAuthenticationToken.setBrand(userDetails.getBrandName());
				usernamePasswordAuthenticationToken.setFullPath(userDetails.getFullPath());
				usernamePasswordAuthenticationToken.setSource(ip);

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
