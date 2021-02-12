package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.UserMapper;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String DEFAULT_PASSWORD = "123456";
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = lambdaQuery().eq(User::getUsername, username).one();
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

    @Override
    public IPage<User> listUser(String keyword, Status status, Integer dept,
                                Integer page, Integer limit) {
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        return baseMapper.selectByParams(new Page<>((page - 1) * limit, limit), keyword
                , status, dept);
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.getUserById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public void deleteById(Integer id) {
        User user = checkeUserExisted(id);
        this.removeById(id);
    }

    @Override
    public void batchDelete(Integer[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public User createUser(User user) {
        baseMapper.insert(user);
        return user;
    }

    @Override
    public void updateUser(Integer id, User user) {
        checkeUserExisted(id);
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public void resetPassword(Integer id) {
        User user = checkeUserExisted(id);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        this.updateById(user);
    }

    private User checkeUserExisted(Integer id) {
        User user = this.getUserById(id);
        if (user == null) {
            throw new CommonServiceException("ID为" + id + "的用户不存在。");
        }
        return user;
    }


}
