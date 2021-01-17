package tech.hongjian.oa.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import tech.hongjian.oa.service.UserTokenService;

/**
 * @author xiahongjian
 * @since  2021-01-16 14:22:47
 */
public class TokenClearLogoutHandler implements LogoutSuccessHandler {
    private UserTokenService tokenService;

    public TokenClearLogoutHandler(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }

    private void clearToken(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        tokenService.removeToken((String) authentication.getCredentials());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        clearToken(authentication);
    }



}
