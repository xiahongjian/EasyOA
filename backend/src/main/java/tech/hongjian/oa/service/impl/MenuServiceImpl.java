package tech.hongjian.oa.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.entity.enums.MenuType;
import tech.hongjian.oa.mapper.MenuMapper;
import tech.hongjian.oa.service.MenuService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getRoleInterfaceMenus(Integer roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return getRoleInterfaceMenus(Arrays.asList(roleId));
    }

    @Override
    public List<Menu> getRoleInterfaceMenus(List<Integer> roleIds) {
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return menuMapper.findRoleMenus(MenuType.INTERFACE, roleIds);
    }

    @Override
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = menuMapper.findAllMenuWithRole();
        if (allMenus.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Menu> idMenuMap = allMenus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
        List<Menu> tree = new ArrayList<>();

        for (Menu menu : allMenus) {
            if (menu.getParentId() == null || !idMenuMap.containsKey(menu.getParentId())) {
                tree.add(menu);
            } else {
                idMenuMap.get(menu.getParentId()).getChildren().add(menu);
            }
        }
        return tree;
    }

}
