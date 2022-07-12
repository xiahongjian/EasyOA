package tech.hongjian.oa.config.security.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.ContentType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import tech.hongjian.oa.service.TokenService;
import tech.hongjian.oa.service.UserTokenService;
import tech.hongjian.oa.util.JSONUtil;

/**
 * @author xiahongjian
 * @since  2021-01-16 14:22:47
 */
public class TokenClearLogoutHandler implements LogoutSuccessHandler {
    private TokenService tokenService;

    public TokenClearLogoutHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private void clearToken(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        tokenService.cancelToken((String) authentication.getCredentials());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        clearToken(authentication);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().write(JSONUtil.toJSON(R.ok(null)).getBytes(StandardCharsets.UTF_8));
    }
}
