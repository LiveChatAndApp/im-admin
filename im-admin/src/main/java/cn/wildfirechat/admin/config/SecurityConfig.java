package cn.wildfirechat.admin.config;

import cn.wildfirechat.admin.security.JwtFilter;
import cn.wildfirechat.admin.security.endpoint.UnauthorizedEntryPoint;
import cn.wildfirechat.admin.security.handler.MyAccessDeniedHandler;
import cn.wildfirechat.admin.security.handler.MyLoginFailureHandler;
import cn.wildfirechat.admin.security.handler.MyLoginSuccessHandler;
import cn.wildfirechat.admin.security.handler.MyLogoutSuccessHandler;
import cn.wildfirechat.admin.security.provider.MyAuthenticationFilter;
import cn.wildfirechat.admin.security.provider.MyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private JwtFilter jwtRequestFilter;

    @Resource
    private MyAuthenticationProvider authProvider;

    @Resource
    private MyLoginSuccessHandler myLoginSuccessHandler;

    @Resource
    private MyLoginFailureHandler myLoginFailureHandler;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Resource
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public void configure(WebSecurity web) {
        //解决静态资源被拦截的问题
        //spring security 建议使用permitAll()方式放行这些请求
//        web.ignoring()
//                .antMatchers(HttpMethod.GET,
//                        "/favicon.ico",
//                        "/*.html",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/**/*.map",
//                        "/**/*.png",
//                        "/**/*.jpg",
//                        // swagger---start--
//                        "/swagger-ui.html",
//                        "/swagger-ui/**",
//                        "/swagger-resources/**",
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "/v3/api-docs/swagger-config",
//                        "/webjars/**",
//                        "/doc.html"
//                        // swagger---end--
//                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.csrf().disable();

        // 解决同源问题 X-Frame-Options to allow any request from same domain
        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();

        //session管理策略
        //   http.sessionManagement().sessionAuthenticationStrategy(new SessionFixationProtectionStrategy());

        // 登录认证过滤器
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
        http.addFilterAt(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // session并发控制过滤器
        http.addFilterBefore(jwtRequestFilter, LogoutFilter.class);


        // 批配上的接口放行
        http.authorizeRequests()
                .antMatchers("/login",
                        "/code/image",
                        "/session/get",
                        "/google/getToken",
                        "/example/test",
                        "/google/checkCode",

                        "/favicon.ico",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.map",
                        "/**/*.png",
                        "/**/*.jpg",
                        // swagger---start--
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config",
                        "/webjars/**",
                        "/doc.html",
                        "/admin/system/test/**"
                        // swagger---end--
                )
                .permitAll()

                // 任何没被批配上的 antMatchers的，需要验证 token
                .anyRequest()
                .authenticated()

                // 定义登入模式
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")///登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password

                // 定义登出模式
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)

                .invalidateHttpSession(true)
                .permitAll();

        // 其他所有请求都需要授权
//        http.authorizeRequests().anyRequest().permitAll();

        // 自定义未授权异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler);
    }


    /**
     * 用户登陆校验
     * 调用了customUserService()，内部覆盖重写了 UserDetailsService.loadUserByUsername,需返回 配置了权限的UserDetails的子类对象
     * 作为登陆用户权限配置的依据
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    /**
     * 自定义登录过滤器
     */
    private UsernamePasswordAuthenticationFilter myAuthenticationFilter() throws Exception {
        MyAuthenticationFilter filter = new MyAuthenticationFilter();
        filter.setPostOnly(true);                       // POST提交
        filter.setOpenValidateCode(false);               // 开启验证码校验
        filter.setValidateCodeParameter("checkCode");   // 设置验证码字段名
        filter.setAuthenticationManager(this.authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationSuccessHandler(myLoginSuccessHandler); // 必须现在这里注入，否则Handler中无法注入实例
        filter.setAuthenticationFailureHandler(myLoginFailureHandler); // 必须现在这里注入，否则Handler中无法注入实例
        // session并发控制,因为默认的并发控制方法是空方法.这里必须自己配置一个
        filter.setSessionAuthenticationStrategy(new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry));
        return filter;
    }

    /**
     * session策略
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        return new DefaultHttpFirewall();
    }

}
