package tech.hongjian.oa.config.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import tech.hongjian.oa.config.ConfigConsts.URLs;
import tech.hongjian.oa.util.JSONUtil;

/**
 * UsernamePasswordAuthenticationFilter的增强版，支持Content-Type为application/json类型
 * 的登录请求
 * @author xiahongjian
 * @since 2021-01-09 22:51:51
 */
public class AdvancedLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public AdvancedLoginAuthenticationFilter() {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(URLs.LOGIN, "POST"));
    }

    @SuppressWarnings("deprecation")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        String contentType = request.getContentType();
        if (MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)
                || MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {

            Map<String, String> loginData = new HashMap<>();
            try {
                String json = StreamUtils.copyToString(request.getInputStream(),
                        Charset.forName("UTF-8"));
                loginData = JSONUtil.toMap(json, String.class);
            } catch (IOException e) {
            }
            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        return super.attemptAuthentication(request, response);
    }

}
