package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.UserRoleRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-31
 */
public interface UserRoleRelService extends IService<UserRoleRel> {

    UserRoleRel create(Integer userId, Integer roleId);
}
