package tech.hongjian.oa.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.oa.config.security.SysTokenProperties;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.service.UserTokenService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author xiahongjian
 * @since 2021-01-16 14:29:39
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class UserTokenServiceImpl implements UserTokenService {
    @Autowired
    private SysTokenProperties tokenConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Override
    public String createToken(User user) {
        if (user == null) {
            return null;
        }
        String token = generateToken(user);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(token, LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss")), tokenConfig.getExpire(), TimeUnit.MINUTES);
        operations.set(generateLatestTokenKey(user.getId()), token,
                tokenConfig.getExpire(), TimeUnit.MINUTES);
        return token;
    }

    private String generateToken(User user) {
        String salt = BCrypt.gensalt();
        // 设置过期时间
        Date expire =
                new Date(System.currentTimeMillis() + tokenConfig.getExpire() * 60000);
        String token = JWT.create().withSubject(user.getUsername()).withExpiresAt(expire)
                .withIssuedAt(new Date()).sign(Algorithm.HMAC256(salt));
        return token;
    }

    @Override
    public UserDetails validate(String token) {
        String msg = "Token expires";
        ValueOperations<String, String> operations =
                redisTemplate.opsForValue();
        if (operations.get(token) == null) {
            log.info("Expired token: {}", token);
            throw new CredentialsExpiredException(msg);
        }
        DecodedJWT decode = JWT.decode(token);
        Date now = new Date();
        if (decode.getExpiresAt().before(now)) {
            Date expiresAt = decode.getExpiresAt();
            log.info("ExpiresAt: {}",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiresAt));
            throw new CredentialsExpiredException(msg);
        }
        return userService.loadUserByUsername(decode.getSubject());
    }

    @Override
    public synchronized String refreshToken(String oldToken, User user) {
        DecodedJWT decode = JWT.decode(oldToken);
        if (!shouldTokenRefresh(user.getId(), decode.getIssuedAt())) {
            return null;
        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String newToken = generateToken(user);
        // 设置最新token
        operations.set(generateLatestTokenKey(user.getId()), newToken,
                tokenConfig.getExpire(), TimeUnit.MINUTES);
        return newToken;
    }

    @Override
    public boolean shouldTokenRefresh(Integer userId, Date issueAt) {
        LocalDateTime issueTime =
                LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        if (!LocalDateTime.now().minusMinutes(tokenConfig.getRefreshInterval()).isAfter(issueTime)) {
            return false;
        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String latestToken = operations.get(generateLatestTokenKey(userId));
        if (latestToken == null) {
            return true;
        }
        DecodedJWT decode = JWT.decode(latestToken);
        Date createTime = decode.getIssuedAt();
        return LocalDateTime.now().minusMinutes(tokenConfig.getRefreshInterval())
                .isAfter(LocalDateTime.ofInstant(createTime.toInstant(),
                        ZoneId.systemDefault()));
    }

    private String generateLatestTokenKey(Integer id) {
        return "UserToken#" + id;
    }
}
