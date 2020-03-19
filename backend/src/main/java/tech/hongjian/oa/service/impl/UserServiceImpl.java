package tech.hongjian.oa.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Permission;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.mapper.UserMapper;
import tech.hongjian.oa.service.PermissionService;
import tech.hongjian.oa.service.UserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PermissionService permissionService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("User[" + username + "] not existed.");
        }
        List<Permission> permmissions = permissionService.getUserPermission(user.getId());
        Set<Permission> set = new HashSet<>();
        set.addAll(permmissions);
        user.setAuthorities(set);
        return user;
    }

}
