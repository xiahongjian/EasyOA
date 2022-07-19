package tech.hongjian.oa.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import tech.hongjian.oa.config.security.filter.JWTAuthenticationFilter;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.UserTokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiahongjian
 * @since 2021-01-16 15:49:28
 */
public class JWTRefreshSuccessHandler implements AuthenticationSuccessHandler {
    private static final int TOKEN_REFRESH_INTERVAL = 300; // 5分钟刷新一次

    private UserTokenService tokenService;

    public JWTRefreshSuccessHandler(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String token = (String) authentication.getCredentials();
        String newToken = tokenService.refreshToken(token, (User) authentication.getPrincipal());
        if (newToken != null) {
            response.setHeader(JWTAuthenticationFilter.DEFAULT_AUTHENTICATION_HEADER, newToken);
        }
    }
}
