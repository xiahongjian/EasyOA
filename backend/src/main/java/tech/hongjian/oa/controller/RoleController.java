package tech.hongjian.oa.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
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
import tech.hongjian.oa.controller.vo.RoleVO;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.RoleService;

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
public class RoleController {

    private RoleService roleService;

    @GetMapping("/roles")
    public R listRoles(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String key,
                       @RequestParam(required = false) Integer status,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {

        IPage<Role> result = roleService.findRoles(name, key, Status.of(status),
                page, limit);
        return R.ok(result);
    }

    @GetMapping("/role/{id}")
    public R getRole(@PathVariable Integer id) {
        return R.ok(roleService.getRoleWithPermission(id));
    }

    @GetMapping("/role/{id}/treeSelect")
    public R getRoleTreeSelect(@PathVariable Integer id) {
        return R.ok(roleService.getRoleTreeSelect(id));
    }

    @PutMapping("/role/{id}")
    public R updateRole(@PathVariable Integer id, @RequestBody RoleVO role) {
        roleService.updateRoleAndPermission(id, role);
        return R.ok();
    }

    @DeleteMapping("/role/{id}")
    public R deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return R.ok();
    }

    @DeleteMapping("/roles")
    public R deleteRoles(@RequestParam Integer[] ids) {
        roleService.deleteRoles(ids);
        return R.ok();
    }

    @PostMapping("/role")
    public R createRole(@RequestBody RoleVO role) {
        return R.ok(roleService.createRoleWithPermissions(role));
    }

    @PutMapping("/role/{id}/status")
    public R changeRoleStatus(@PathVariable Integer id, @RequestParam Integer status) {
        roleService.changeStatus(id, Status.of(status));
        return R.ok();
    }
}
