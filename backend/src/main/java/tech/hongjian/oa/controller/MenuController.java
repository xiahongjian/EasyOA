package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.MenuService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@RestController
public class MenuController {
    private MenuService menuService;

    @GetMapping("/menu/menurole")
    public R getAllMenu() {
        return R.ok(menuService.getMenuTree());
    }

    @GetMapping("/menus")
    public R listMenus(@RequestParam(required = false) String query,
                       @RequestParam(required = false) Boolean visible) {
        return R.ok(menuService.getMenuTree(query, visible));
    }

    @GetMapping("/menu/{id}")
    public R getMenu(@PathVariable Integer id) {
        return R.ok(menuService.getById(id));
    }

    @PostMapping("/menu")
    public R createMenu(@RequestBody Menu menu) {
        menuService.createMenu(menu);
        return R.ok();
    }

    @PutMapping("/menu/{id}")
    public R updateMenu(@PathVariable Integer id, @RequestBody Menu menu) {
        menuService.updateMenu(id, menu);
        return R.ok();
    }

    @DeleteMapping("/menu/{id}")
    public R deleteMenu(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return R.ok();
    }
}
