package tech.hongjian.oa.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import tech.hongjian.oa.config.filter.JWTAuthenticationFilter;
import tech.hongjian.oa.config.handler.LoginAuthFailureHandler;

/**
 * @author xiahongjian
 * @since 2021-01-16 13:56:29
 */
public class JWTAuthConfigurer<T extends JWTAuthConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
        extends AbstractHttpConfigurer<T, B> {

    private JWTAuthenticationFilter authFilter;

    public JWTAuthConfigurer() {
        this.authFilter = new JWTAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new LoginAuthFailureHandler());

        // 放在LogoutFilter之前是为了保证不会存在使用伪造token（不能通过验证）冒充登录者进行登出操作
        http.addFilterBefore(postProcess(authFilter), LogoutFilter.class);
    }

    public JWTAuthConfigurer<T, B> failureHandler(AuthenticationFailureHandler failureHandler) {
        authFilter.setAuthenticationFailureHandler(failureHandler);
        return this;
    }

    public JWTAuthConfigurer<T, B> permissiveRequestUrls(String... urls) {
        authFilter.setPermissiveUrl(urls);
        return this;
    }

    public JWTAuthConfigurer<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler) {
        authFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }
}
