package tech.hongjian.oa.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.CacheService;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.service.UserTokenService;

/**
 * @author xiahongjian
 * @since 2021-01-16 14:29:39
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserService userService;

    @Override
    public String createToken(User user) {
        if (user == null) {
            return null;
        }
        String salt = BCrypt.gensalt();
        // 1小时候过期
        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        String token = JWT.create().withSubject(user.getUsername()).withExpiresAt(expire)
                .withIssuedAt(new Date()).sign(Algorithm.HMAC256(salt));
        cacheService.set(token, salt);
        return token;
    }

    @Override
    public String removeToken(String token) {
        if (cacheService.contains(token)) {
            return (String) cacheService.remove(token);
        }
        return null;
    }

    @Override
    public UserDetails validate(String token) {
        String msg = "Token expires";
        if (!cacheService.contains(token)) {
            throw new CredentialsExpiredException(msg);
        }
        DecodedJWT decode = JWT.decode(token);
        Date now = new Date();
        if (decode.getExpiresAt().before(now)) {
            throw new CredentialsExpiredException(msg);
        }
        User user = (User) userService.loadUserByUsername(decode.getSubject());
        return user;
    }

    @Override
    public String refreshToken(String oldToken, User user) {
        if (cacheService.contains(oldToken)) {
            removeToken(oldToken);
        }
        return createToken(user);
    }

}
