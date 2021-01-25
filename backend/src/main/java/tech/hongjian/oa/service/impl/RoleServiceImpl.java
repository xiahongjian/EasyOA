package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.RoleMapper;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.util.CommonUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> getUserRoles(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return getBaseMapper().findUserRoles(userId);
    }

    @Override
    public IPage<Role> findRoles(String name, String key, Status status,
                                 Integer page, Integer limit) {
        name = CommonUtil.wrapWith(name, "%");
        key = CommonUtil.wrapWith(key, "%");
        Page<Role> p = new Page<>((page - 1) * limit, limit);
        return getBaseMapper().findRoles(p, name, key, status);
    }

    @Override
    public boolean updateRole(Integer id, Role role) {
        checkExisted(id);
        role.setUpdateTime(LocalDateTime.now());
        return updateById(role);
    }

    @Override
    public int deleteRole(Integer id) {
        checkExisted(id);
        return getBaseMapper().deleteById(id);
    }

    @Override
    public Role createRole(Role role) {
        if (lambdaQuery().eq(Role::getKey, role.getKey()).count() > 0) {
            throw new CommonServiceException("角色标识为[" + role.getKey() + "]的角色已存在。");
        }
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        save(role);
        return role;
    }

    private Role checkExisted(Integer id) {
        Role existed = getById(id);
        if (existed == null) {
            throw new CommonServiceException("ID为[" + id +"]的角色不存在。");
        }
        return existed;
    }
}
