package tech.hongjian.oa.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.Menu;

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
}
