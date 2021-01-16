package tech.hongjian.oa.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import tech.hongjian.oa.entity.User;
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
        User user = (User) authentication.getPrincipal();
        tokenService.removeToken((String) authentication.getCredentials(), user);

//        Object credentials = authentication.getCredentials();
//        if (credentials instanceof String) {
//            String token = (String) credentials;
//            User user = (User) authentication.getPrincipal();
//            String salt = tokenService.getSalt(user);
//            // cache中没有salt则不处理
//            if (salt == null) {
//                return;
//            }
//            // 校验token签名
//            try {
//                JWT.require(Algorithm.HMAC256(salt)).build().verify(token);
//            } catch(Exception e) {
//                // 校验签名未通过
//                return;
//            }
//            // 验证通过则清除token
//            tokenService.removeToken(token, user);
//        }
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        clearToken(authentication);
    }



}
