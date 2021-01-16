package tech.hongjian.oa.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.Role;

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

}
