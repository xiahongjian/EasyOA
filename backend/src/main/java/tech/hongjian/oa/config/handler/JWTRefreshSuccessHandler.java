package tech.hongjian.oa.config.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import tech.hongjian.oa.config.filter.JWTAuthenticationFilter;
import tech.hongjian.oa.config.token.JWTAuthenticationToken;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.UserTokenService;

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
        String token = (String) ((JWTAuthenticationToken) authentication).getCredentials();
        DecodedJWT decoded = JWT.decode(token);
        boolean shouldRefresh = shouldTokenRefresh(decoded.getIssuedAt());
        if (shouldRefresh) {
            String newToken = tokenService.refreshToken(token, (User) authentication.getPrincipal());
            response.setHeader(JWTAuthenticationFilter.DEFAULT_AUTHENTICATION_HEADER, newToken);
        }
    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime =
                LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(issueTime);
    }
}
