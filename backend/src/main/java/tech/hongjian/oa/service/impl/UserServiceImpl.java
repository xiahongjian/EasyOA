package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.config.ConfigConsts;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.UserRoleRel;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.UserMapper;
import tech.hongjian.oa.model.UserVO;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.service.UserRoleRelService;
import tech.hongjian.oa.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    private UserRoleRelService userRoleRelService;

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
                                String prop, String order, Integer page, Integer limit) {
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        if (prop == null || order == null) {
            prop = "id";
            order = "asc";
        }
        return baseMapper.selectByParams(new Page<>((page - 1L) * limit, limit), keyword
                , status, dept, prop, order);
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public void deleteById(Integer id) {
        checkUserExisted(id);
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
    public User createUser(UserVO formData) {
        // 检查username是否重复
        if (usernameIsExisted(formData.getUsername(), null)) {
            throw new CommonServiceException("用户名为[" + formData.getUsername() +
                    "]的用户已存在。");
        }
        // 检查email是否重复
        if (emailIsExisted(formData.getEmail(), null)) {
            throw new CommonServiceException("邮箱地址为[" + formData.getEmail() + "]的用户已存在。");
        }
        // 创建user对象
        User user = new User();
        user.setUsername(formData.getUsername());
        user.setName(formData.getName());
        user.setEmail(formData.getEmail());
        user.setMobile(formData.getMobile());
        user.setPost(formData.getPost());
        user.setGender(formData.getGender());
        user.setDepartmentId(formData.getDepartmentId() == -1 ? null :
                formData.getDepartmentId());
        user.setPassword(passwordEncoder.encode(ConfigConsts.DEFAULT_PASSWORD));
        baseMapper.insert(user);

        // 创建用户角色关联
        List<Integer> roles = formData.getRoleIds();
        if (roles != null && !roles.isEmpty()) {
            for (Integer id : roles) {
                UserRoleRel rel = new UserRoleRel(user.getId(), id);
                userRoleRelService.save(rel);
            }
        }

        return user;
    }

    @Override
    public void updateUser(Integer id, User user) {
        checkUserExisted(id);
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public void updateUser(Integer id, UserVO vo) {
        updateUser(id, (User) vo);
        // 更新用户角色关联
        List<Integer> roleIds = vo.getRoleIds();
        Set<Integer> existedRoles =
                userRoleRelService.lambdaQuery().eq(UserRoleRel::getUserId, id).list()
                        .stream().map(UserRoleRel::getId).collect(Collectors.toSet());
        // 删除已经不存在的关联
        userRoleRelService.lambdaUpdate().eq(UserRoleRel::getUserId, id).notIn(UserRoleRel::getRoleId, roleIds).remove();

        List<Integer> needCreated = roleIds.stream().filter(role -> !existedRoles.contains(role)).collect(Collectors.toList());
        for (Integer roleId : needCreated) {
            userRoleRelService.create(id, roleId);
        }
    }

    @Override
    public void resetPassword(Integer id) {
        User user = checkUserExisted(id);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        this.updateById(user);
    }

    @Override
    public boolean usernameIsExisted(String username, Integer excludeId) {
        LambdaQueryChainWrapper<User> query = lambdaQuery().eq(User::getUsername,
                username);
        if (excludeId != null) {
            query.ne(User::getId, excludeId);
        }
        return query.count() > 0;
    }

    @Override
    public boolean emailIsExisted(String email, Integer excludeId) {
        LambdaQueryChainWrapper<User> query = lambdaQuery().eq(User::getEmail, email);
        if (excludeId != null) {
            query.ne(User::getId, excludeId);
        }
        return query.count() > 0;
    }

    @Override
    public UserVO getUserInfo(Integer id) {
        User user = getUserById(id);
        if (user == null) {
            return null;
        }
        List<UserRoleRel> list =
                userRoleRelService.lambdaQuery().eq(UserRoleRel::getUserId, id).list();
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setRoleIds(list.stream().map(UserRoleRel::getId).collect(Collectors.toList()));
        return vo;
    }

    private User checkUserExisted(Integer id) {
        User user = this.getUserById(id);
        if (user == null) {
            throw new CommonServiceException("ID为" + id + "的用户不存在。");
        }
        return user;
    }


}
