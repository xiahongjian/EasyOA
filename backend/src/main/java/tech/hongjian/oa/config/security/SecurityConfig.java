package tech.hongjian.oa.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import tech.hongjian.oa.config.Code;
import tech.hongjian.oa.config.ConfigConstants.URLs;
import tech.hongjian.oa.config.security.handler.JWTRefreshSuccessHandler;
import tech.hongjian.oa.config.security.handler.LoginAuthSuccessHandler;
import tech.hongjian.oa.config.security.handler.TokenClearLogoutHandler;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.TokenService;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.service.UserTokenService;
import tech.hongjian.oa.service.impl.JWTAuthenticationProvider;
import tech.hongjian.oa.util.JSONUtil;
import tech.hongjian.oa.util.WebUtil;

import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * @author xiahongjian
 * @time 2020-01-15 22:37:28
 */
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ResourceBasedDecisionManager customUrlDecisionManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",
                corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**",
                "/fonts/**", "/favicon.ico", "/error");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.authenticationProvider(new JWTAuthenticationProvider(userTokenService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll() // TODO for test
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        return object;
                    }
                })
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .cors()
                .and()
                // 配置登录请求数据以JSON格式传到后端时的认证处理
                .apply(new AdvancedLoginConfigurer<>())
                .loginSuccessHandler(new LoginAuthSuccessHandler(userTokenService))
                .and()
                // 配置JWT的认证处理
                .apply(new JWTAuthConfigurer<>())
                .permissiveRequestUrls(URLs.LOGIN)
                .tokenService(tokenService)
                .tokenValidSuccessHandler(new JWTRefreshSuccessHandler(userTokenService))
                .and()
                .logout()
                .logoutUrl(URLs.LOGOUT)
                .logoutSuccessHandler(new TokenClearLogoutHandler(userTokenService))
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            out.print(JSONUtil.toJSON(R.error(Code.UNAUTHORIZED, accessDeniedException.getMessage())));
            out.close();
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // AJAX请求
            if (WebUtil.isAjaxRequest(request)) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                PrintWriter out = response.getWriter();
                out.print(JSONUtil.toJSON(R.error(Code.UNAUTHORIZED,
                        "Unauthorized")));
                out.close();
                return;
            }
            // 非AJAX请求
            response.sendRedirect("/login?redirect=" + URLEncoder.encode(request.getRequestURI(), "UTF-8"));
        };
    }
}
