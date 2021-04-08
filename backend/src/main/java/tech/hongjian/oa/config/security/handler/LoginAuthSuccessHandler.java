package tech.hongjian.oa.config.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.UserTokenService;
import tech.hongjian.oa.util.JSONUtil;

/**
 * @author xiahongjian
 * @since  2021-01-16 12:17:14
 */
public class LoginAuthSuccessHandler implements AuthenticationSuccessHandler {

    private UserTokenService tokenService;

    public LoginAuthSuccessHandler(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String token = tokenService.createToken((User) authentication.getPrincipal());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.toJSON(R.ok(token)));
        out.flush();
    }

}
