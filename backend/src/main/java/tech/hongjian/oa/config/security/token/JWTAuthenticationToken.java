package tech.hongjian.oa.config.security.token;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author xiahongjian
 * @since 2021-01-16 10:36:36
 */
public class JWTAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 8080914751985777332L;

    private UserDetails principal;
    private String credientials;

    public JWTAuthenticationToken(String credientials) {
        super(Collections.emptyList());
        this.credientials = credientials;
    }

    public JWTAuthenticationToken(UserDetails principal, String credientials,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credientials = credientials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credientials;
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
