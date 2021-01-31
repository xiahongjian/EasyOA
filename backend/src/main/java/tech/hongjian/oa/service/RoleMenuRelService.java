package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.RoleMenuRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-31
 */
public interface RoleMenuRelService extends IService<RoleMenuRel> {
    RoleMenuRel createRel(Integer roleId, Integer menuId);

    void deleteRel(Integer roleId, Integer menuId);
}
