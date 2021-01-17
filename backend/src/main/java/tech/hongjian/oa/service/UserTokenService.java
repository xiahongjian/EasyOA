package tech.hongjian.oa.service;

import org.springframework.security.core.userdetails.UserDetails;

import tech.hongjian.oa.entity.User;

/**
 * @author xiahongjian
 * @since  2021-01-16 14:27:36
 */
public interface UserTokenService {
    String createToken(User user);

    UserDetails validate(String token);

    String refreshToken(String oldToken, User user);

    String removeToken(String token);

}
