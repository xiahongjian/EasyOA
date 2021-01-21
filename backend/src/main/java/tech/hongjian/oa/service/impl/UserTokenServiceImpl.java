package tech.hongjian.oa.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.CacheService;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.service.UserTokenService;
import tech.hongjian.oa.util.JSONUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiahongjian
 * @since 2021-01-16 14:29:39
 */
@Slf4j
@Setter(onMethod_ = {@Autowired})
@Service
public class UserTokenServiceImpl implements UserTokenService {
    private CacheService cacheService;
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
            log.info("Expired token: {}", token);
            log.info("All token: {}", JSONUtil.toJSON(((MemoryCacheServiceImpl) cacheService).getAllData()));
            throw new CredentialsExpiredException(msg);
        }
        DecodedJWT decode = JWT.decode(token);
        Date now = new Date();
        if (decode.getExpiresAt().before(now)) {
            Date expiresAt = decode.getExpiresAt();
            log.info("ExpiresAt: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiresAt));
            throw new CredentialsExpiredException(msg);
        }
        return userService.loadUserByUsername(decode.getSubject());
    }

    @Override
    public synchronized String refreshToken(String oldToken, User user) {
        // 保持两个token在缓存中，防止刷新token时，瞬间导致前端的连续请求中的后续请求token失效
        if (cacheService.contains(oldToken)) {
            String removeToken = (String) cacheService.get(oldToken);
            if (removeToken != null) {
                removeToken(removeToken);
            }
        }
        String newToken = createToken(user);
        cacheService.set(newToken, oldToken);
        return newToken;
    }

}
