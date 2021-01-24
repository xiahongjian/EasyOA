package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.entity.Menu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface MenuService extends IService<Menu> {

    List<Menu> getRoleInterfaceMenus(Integer roleId);

    List<Menu> getRoleInterfaceMenus(List<Integer> roleIds);

    List<Menu> getMenuTree();

    void updateMenu(Integer id, Menu menu);

    List<Menu> getMenuTree(String query, Boolean visible);

    void deleteMenu(Integer id);

    Menu createMenu(Menu menu);
}
