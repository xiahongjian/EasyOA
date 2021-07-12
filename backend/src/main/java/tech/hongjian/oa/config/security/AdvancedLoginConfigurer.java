package tech.hongjian.oa.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import tech.hongjian.oa.config.security.filter.AdvancedLoginAuthenticationFilter;
import tech.hongjian.oa.config.security.handler.LoginAuthFailureHandler;

/**
 * @author xiahongjian
 * @since 2021-01-16 11:57:25
 */
public class AdvancedLoginConfigurer<T extends AdvancedLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
        extends AbstractHttpConfigurer<T, B> {
    private AdvancedLoginAuthenticationFilter authFilter;

    public AdvancedLoginConfigurer() {
        this.authFilter = new AdvancedLoginAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new LoginAuthFailureHandler());
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        http.addFilterAfter(postProcess(authFilter), UsernamePasswordAuthenticationFilter.class);
    }

    public AdvancedLoginConfigurer<T, B> loginSuccessHandler(AuthenticationSuccessHandler successHandler) {
        authFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

    public AdvancedLoginConfigurer<T, B> loginFailureHandler(AuthenticationFailureHandler failureHandler) {
        authFilter.setAuthenticationFailureHandler(failureHandler);
        return this;
    }


}
