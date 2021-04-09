package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.model.RoleWithMenu;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface RoleService extends IService<Role> {

    List<Role> getUserRoles(Integer userId);

    IPage<Role> findRoles(String name, String key, Status status, Integer page, Integer limit);

    boolean updateRole(Integer id, Role role);

    int deleteRole(Integer id);

    int deleteRoles(Integer[] ids);

    Role createRole(Role role);

    Role createRoleWithPermissions(RoleWithMenu role);

    void changeStatus(Integer id, Status status);

    RoleWithMenu getRoleWithPermission(Integer id);

    boolean updateRoleAndPermission(Integer id, RoleWithMenu role);

    List<Integer> getRoleTreeSelect(Integer id);

    List<Role> findAllRoles();

    long countByParamMap(Map<String, Object> params);

    List<Role> findByParamMap(Map<String, Object> params);

    IPage<Role> findByParamMapPage(Page<Role> rolePage, Map<String, Object> params);
}
