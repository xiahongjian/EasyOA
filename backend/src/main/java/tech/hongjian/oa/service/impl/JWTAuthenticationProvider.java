package tech.hongjian.oa.service.impl;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import tech.hongjian.oa.config.security.token.JWTAuthenticationToken;
import tech.hongjian.oa.service.UserTokenService;

/**
 * @author xiahongjian
 * @since 2021-01-16 16:01:43
 */
public class JWTAuthenticationProvider implements AuthenticationProvider {
    private UserTokenService tokenService;

    public JWTAuthenticationProvider(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        UserDetails userDetails = tokenService.validate(token);
        JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        jwtAuthenticationToken.setDetails(userDetails);
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JWTAuthenticationToken.class);
    }

}
