package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.RoleWithMenu;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.RoleMenuRel;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.RoleMapper;
import tech.hongjian.oa.service.RoleMenuRelService;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.util.CommonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private RoleMenuRelService roleMenuRelService;

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
        Page<Role> p = new Page<>((page - 1L) * limit, limit);
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
        // 删除角色菜单关联
        roleMenuRelService.lambdaUpdate().eq(RoleMenuRel::getRoleId, id).remove();
        return getBaseMapper().deleteById(id);
    }

    @Override
    public int deleteRoles(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        // 删除角色菜单关联
        roleMenuRelService.lambdaUpdate().in(RoleMenuRel::getRoleId, ids).remove();
        return getBaseMapper().deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public Role createRole(Role role) {
        if (lambdaQuery().eq(Role::getKey, role.getKey()).count() > 0) {
            throw new CommonServiceException("角色标识为[" + role.getKey() + "]的角色已存在。");
        }
        role = CommonUtil.setEntityDefault(role);
        save(role);
        return role;
    }

    @Override
    public Role createRoleWithPermissions(RoleWithMenu role) {
        Role entity = createRole(role);
        for (Integer menuId : role.getMenuIds()) {
            roleMenuRelService.createRel(entity.getId(), menuId);
        }
        return role;
    }

    @Override
    public void changeStatus(Integer id, Status status) {
        if (status == null) {
            throw new CommonServiceException("角色状态不能为空");
        }
        checkExisted(id);
        lambdaUpdate().eq(Role::getId, id).set(Role::getStatus, status).set(Role::getUpdateTime, LocalDateTime.now()).update();
    }

    @Override
    public RoleWithMenu getRoleWithPermission(Integer id) {
        if (id == null) {
            return null;
        }
        Role role = getById(id);
        if (role == null) {
            return null;
        }
        List<Integer> menuIds =
                roleMenuRelService.lambdaQuery().eq(RoleMenuRel::getRoleId, id).list()
                        .stream().map(RoleMenuRel::getMenuId).collect(Collectors.toList());
        return new RoleWithMenu(role, menuIds);
    }

    @Override
    public boolean updateRoleAndPermission(Integer id, RoleWithMenu role) {
        boolean res = updateRole(id, role);
        if (!res) {
            return false;
        }
        Set<Integer> origin =
                roleMenuRelService.lambdaQuery().eq(RoleMenuRel::getRoleId, id).list()
                        .stream().map(RoleMenuRel::getMenuId).collect(Collectors.toSet());

        List<Integer> needCreated =
                role.getMenuIds().stream().filter(e -> !origin.contains(e)).collect(Collectors.toList());
        // 删除已经不存在的关联
        LambdaUpdateChainWrapper<RoleMenuRel> updateChainWrapper =
                roleMenuRelService.lambdaUpdate().eq(RoleMenuRel::getRoleId, id);
        if (!role.getMenuIds().isEmpty()) {
            updateChainWrapper.notIn(RoleMenuRel::getMenuId, role.getMenuIds());
        }
        updateChainWrapper.remove();

        // 创建新增的关联
        for (Integer menuId : needCreated) {
            roleMenuRelService.createRel(id, menuId);
        }
        return true;
    }

    @Override
    public List<Integer> getRoleTreeSelect(Integer id) {
        return baseMapper.findSelectTreeChecked(id);
    }

    @Override
    public List<Role> findAllRoles() {
        return lambdaQuery().eq(Role::getStatus, Status.NORMAL).orderByAsc(Role::getSort).list();
    }

    private Role checkExisted(Integer id) {
        Role existed = getById(id);
        if (existed == null) {
            throw new CommonServiceException("ID为[" + id + "]的角色不存在。");
        }
        return existed;
    }
}
