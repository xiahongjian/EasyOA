package tech.hongjian.oa.config.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.hongjian.oa.config.security.token.JWTAuthenticationToken;
import tech.hongjian.oa.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiahongjian
 * @since 2021-01-16 10:07:21
 */
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public static final String DEFAULT_AUTHENTICATION_HEADER = "Authorization";

    private TokenService tokenService;
    private String authenticationHeader;
    private AuthenticationManager authenticationManager;
    private RequestMatcher requiresAuthenticationRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationSuccessHandler successHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();
    private AuthenticationFailureHandler failureHandler =
            new SimpleUrlAuthenticationFailureHandler();


    public JWTAuthenticationFilter() {
        this.authenticationHeader = DEFAULT_AUTHENTICATION_HEADER;
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(this.authenticationHeader);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(tokenService, "tokenService must be specified");
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "authenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "authenticationFailureHandler must be specified");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 当request header中不包含token时，直接放行
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        // 检查token是否在吊销列表中

        Authentication authResult = null;
        AuthenticationException failed = assetNotInBlockList(token);

        if (failed == null) {
            if (token != null) {
                try {
                    JWTAuthenticationToken authToken = new JWTAuthenticationToken(token);
                    authResult = this.authenticationManager.authenticate(authToken);
                } catch (JWTDecodeException e) {
                    log.error("Failed to decoded JWT, token: [{}].", token, e);
                    failed = new InsufficientAuthenticationException("JWT format error", e);
                } catch (AuthenticationException e) {
                    failed = e;
                }
            } else {
                failed = new InsufficientAuthenticationException("JWT is empty");
            }
        }
        if (authResult != null) {
            this.successfulAuthentication(request, response, filterChain, authResult);
        } else if (!permissiveRequest(request)) {
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private AuthenticationException assetNotInBlockList(String token) {
        if (tokenService.inCancelList(token)) {
            return new InsufficientAuthenticationException("令牌已失效，请重新登录。");
        }
        return null;
    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null)
            return false;
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
            HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    protected String getToken(HttpServletRequest request) {
        String header = request.getHeader(authenticationHeader);
        return StringUtils.isEmpty(header) ? null : StringUtils.removeStart(header, "Bearer");
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String getAuthenticationHeader() {
        return authenticationHeader;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setPermissiveUrl(String... urls) {
        if(permissiveRequestMatchers == null) {
            permissiveRequestMatchers = new ArrayList<>();
        }
        for(String url : urls) {
            permissiveRequestMatchers .add(new AntPathRequestMatcher(url));
        }
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
