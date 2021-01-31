package tech.hongjian.oa.service.impl;

import tech.hongjian.oa.entity.RoleMenuRel;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.RoleMenuRelMapper;
import tech.hongjian.oa.service.RoleMenuRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-31
 */
@Service
public class RoleMenuRelServiceImpl extends ServiceImpl<RoleMenuRelMapper, RoleMenuRel> implements RoleMenuRelService {

    @Override
    public RoleMenuRel createRel(Integer roleId, Integer menuId) {
        RoleMenuRel rel = new RoleMenuRel(roleId, menuId);
        save(rel);
        return rel;
    }

    @Override
    public void deleteRel(Integer roleId, Integer menuId) {
        if (roleId == null || menuId == null) {
            return;
        }
        if (lambdaQuery().eq(RoleMenuRel::getRoleId, roleId).eq(RoleMenuRel::getMenuId,
                menuId).count() == 0) {
            throw new CommonServiceException("关联不存在，roleId：" + roleId + "，menuId：" + menuId);
        }
        lambdaUpdate().eq(RoleMenuRel::getRoleId, roleId).eq(RoleMenuRel::getMenuId, menuId).remove();
    }

}
