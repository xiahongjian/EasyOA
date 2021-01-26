package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

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
}
