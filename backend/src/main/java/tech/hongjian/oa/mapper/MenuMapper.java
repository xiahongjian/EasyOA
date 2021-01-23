package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.entity.enums.MenuType;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findRoleMenus(MenuType type, List<Integer> roleIds);

    List<Menu> findAllMenuWithRole(String query, Status status);
}
