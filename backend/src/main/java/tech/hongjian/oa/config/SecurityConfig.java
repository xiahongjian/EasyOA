package tech.hongjian.oa.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.model.RestResponse;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.util.JSONUtil;
import tech.hongjian.oa.util.WebUtil;

/**
 * @author xiahongjian
 * @time 2020-01-15 22:37:28
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private ResourceBasedDecisionManager customUrlDecisionManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/error");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                    object.setAccessDecisionManager(customUrlDecisionManager);
                    return object;
                }})
            .and()
            .formLogin()
            .usernameParameter(ConfigConsts.PARAM_USERNAME)
            .passwordParameter(ConfigConsts.PARAM_PASSWORD)
            .loginProcessingUrl(ConfigConsts.URL.LOGIN)
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                    response.setContentType(ConfigConsts.CONTENT_TYPE);
                    User user = (User) authentication.getPrincipal();
                    user.setPassword(null);
                    PrintWriter out = response.getWriter();
                    out.print(JSONUtil.toJSON(RestResponse.ok(user)));
                    out.close();
                }
            })
            .failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                    response.setContentType(ConfigConsts.CONTENT_TYPE);
                    PrintWriter out = response.getWriter();
                    String msg = null;
                    if (exception instanceof LockedException) {
                        msg = "账户被锁定，请联系管理员!";
                    } else if (exception instanceof CredentialsExpiredException) {
                        msg = "密码过期，请联系管理员!";
                    } else if (exception instanceof AccountExpiredException) {
                        msg = "账户过期，请联系管理员!";
                    } else if (exception instanceof DisabledException) {
                        msg = "账户被禁用，请联系管理员!";
                    } else if (exception instanceof BadCredentialsException) {
                        msg = "用户名或者密码输入错误，请重新输入!";
                    }
                    out.print(JSONUtil.toJSON(RestResponse.fail(Code.LONGIN_ERROR, msg)));
                    out.close();
                }
            })
            .permitAll()
            .and()
            .logout()
            .logoutUrl(ConfigConsts.URL.LOGOUT)
            .logoutSuccessHandler(new LogoutSuccessHandler() {
                @Override
                public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    PrintWriter out = response.getWriter();
                    out.print(JSONUtil.toJSON(RestResponse.ok()));
                    out.close();
                }
            })
            .permitAll()
            .and()
            .csrf().disable().exceptionHandling()
            .authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
                    // AJAX请求
                    if (WebUtil.isAjaxRequest(request)) {
                        response.setContentType(ConfigConsts.CONTENT_TYPE);
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        PrintWriter out = response.getWriter();
                        out.print(JSONUtil.toJSON(RestResponse.fail(Code.NOT_LOGIN, "Unauthorized")));
                        out.close();
                        return;
                    }
                    // 非AJAX请求
                    response.sendRedirect(ConfigConsts.URL.LOGIN);
                }
            });
    }

}
