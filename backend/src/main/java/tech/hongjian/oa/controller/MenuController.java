package tech.hongjian.oa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.MenuService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menurole")
    public R getAllMenu() {
        return R.ok(menuService.getMenuTree());
    }
}
