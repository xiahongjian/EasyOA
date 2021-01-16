package tech.hongjian.oa.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.entity.enums.MenuType;

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

    List<Menu> findAllMenuWithRole();
}
