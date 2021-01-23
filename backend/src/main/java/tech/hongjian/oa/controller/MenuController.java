package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.entity.enums.Status;
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
                       @RequestParam(required = false) Integer status) {
        return R.ok(menuService.getMenuTree(query, Status.of(status)));
    }

    @GetMapping("/menu/{id}")
    public R getMenu(@PathVariable Integer id) {
        return R.ok(menuService.getById(id));
    }

    @PostMapping("/menu")
    public R createMenu(@RequestBody Menu menu) {
        menuService.save(menu);
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
