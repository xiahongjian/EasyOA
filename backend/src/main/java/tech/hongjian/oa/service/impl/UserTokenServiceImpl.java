package tech.hongjian.oa.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
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
        cacheService.set(getCacheSaltKey(user), salt);
        cacheService.set(token, true);
        return token;
    }

    @Override
    public String removeToken(String token, User user) {
        String saltKey = getCacheSaltKey(user);
        if (cacheService.contains(saltKey)) {
            cacheService.remove(saltKey);
        }
        if (cacheService.contains(token)) {
            return (String) cacheService.remove(token);
        }
        return null;
    }

    private String getCacheSaltKey(User user) {
        return "user#salt#" + user.getId();
    }

    @Override
    public String getSalt(User user) {
        return (String) cacheService.get(getCacheSaltKey(user));
    }

    @Override
    public String removeSalt(User user) {
        return (String) cacheService.remove(getSalt(user));
    }

    @Override
    public UserDetails validate(String token) {
        String msg = "Token expires";
        if (!cacheService.contains(token)) {
            throw new CredentialsExpiredException(msg);
        }
        Date now = new Date();
        DecodedJWT decode = JWT.decode(token);
        if (decode.getExpiresAt().before(now)) {
            throw new CredentialsExpiredException(msg);
        }

        String username = decode.getSubject();
        UserDetails user = userService.loadUserByUsername(username);
        if (user == null || user.getPassword() == null) {
            throw new CredentialsExpiredException(msg);
        }

        String salt = getSalt((User) user);
        if (salt == null) {
            throw new CredentialsExpiredException(msg);
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm).withSubject(username).build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new BadCredentialsException("JWT token verify fail", e);
        }
        return user;
    }

}
