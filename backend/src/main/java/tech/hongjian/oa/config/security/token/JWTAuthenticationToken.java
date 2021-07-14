package tech.hongjian.oa.config.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author xiahongjian
 * @since 2021-01-16 10:36:36
 */
public class JWTAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 8080914751985777332L;

    private UserDetails principal;
    private String credentials;

    public JWTAuthenticationToken(String credentials) {
        super(Collections.emptyList());
        this.credentials = credentials;
    }

    public JWTAuthenticationToken(UserDetails principal, String credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }
}
