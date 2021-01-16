package tech.hongjian.oa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.mapper.UserMapper;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.service.UserService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper
                .selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("User[" + username + "] not existed.");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User[" + username + "] is disabled.");
        }
        List<Role> roles = roleService.getUserRoles(user.getId());
        user.setAuthorities(roles);
        user.setRoles(roles.stream().map(Role::getKey).collect(Collectors.toList()));
        return user;
    }

}
