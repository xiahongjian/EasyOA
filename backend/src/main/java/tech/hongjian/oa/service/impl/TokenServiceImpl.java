package tech.hongjian.oa.service.impl;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.service.TokenService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    /**
     * 计算超时时，额外增加的时间，确保key在redis中过期后，token一定是过期的
     */
    private static final long ADD = 10;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void cancelToken(String token) {
        LocalDateTime expireAt = JWT.decode(token).getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        // 如果token已经失效，则不需要吊销
        if (expireAt.isBefore(now)) {
            return;
        }

        // 计算吊销token在redis中的有效期
        long timeout = Duration.between(now, expireAt).toMillis() / 1000 + ADD;
        redisTemplate.opsForValue().set(toKey(token), "", timeout, TimeUnit.SECONDS);
    }

    private String toKey(String token) {
        return CANCEL_LIST_PREFIX + token;
    }

    @Override
    public boolean inCancelList(String token) {
        return redisTemplate.opsForValue().get(toKey(token)) != null;
    }
}
