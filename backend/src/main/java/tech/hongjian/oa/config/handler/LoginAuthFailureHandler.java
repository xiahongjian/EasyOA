package tech.hongjian.oa.config.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.slf4j.Slf4j;
import tech.hongjian.oa.config.Code;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.util.JSONUtil;

/**
 * @author xiahongjian
 * @since  2021-01-16 12:05:38
 */
@Slf4j
public class LoginAuthFailureHandler implements AuthenticationFailureHandler {

    @SuppressWarnings("deprecation")
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        log.error(exception.getMessage(), exception);
        PrintWriter out = response.getWriter();
        R r = null;
        if (exception instanceof LockedException) {
            r = R.error(Code.ACCOUNT_LOCKED, "账户被锁定，请联系管理员！");
        } else if (exception instanceof CredentialsExpiredException) {
            r = R.error(Code.CREDENTIALS_EXPIRED, "Token已过期，请重新登录！");
        } else if (exception instanceof AccountExpiredException) {
            r = R.error(Code.ACCOUNT_EXPIRED, "账户过期，请联系管理员！");
        } else if (exception instanceof DisabledException) {
            r = R.error(Code.ACCOUNT_DISABLED, "账户被禁用，请联系管理员！");
        } else if (exception instanceof BadCredentialsException) {
            r = R.error(Code.BAD_CREDENTIALS, "用户名或者密码输入错误，请重新输入！");
        } else if (exception instanceof InsufficientAuthenticationException) {
            r = R.error(Code.ERROR_TOKEN, exception.getMessage());
        } else {
            r = R.error();
        }
        out.print(JSONUtil.toJSON(r));
        out.close();
    }

}
