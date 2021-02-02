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
import tech.hongjian.oa.entity.Department;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.DepartmentService;

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
public class DepartmentController {
    private DepartmentService departmentService;

    @GetMapping("/departments")
    public R listDepartment(@RequestParam(required = false) String query,
                            @RequestParam(required = false) Integer status) {
        return R.ok(departmentService.getDepartmentTree(query, Status.of(status)));
    }

    @GetMapping("/department/{id}")
    public R getDepartment(@PathVariable Integer id) {
        return R.ok(departmentService.getDepartmentWithLeader(id));
    }

    @PostMapping("/department")
    public R createDepartment(@RequestBody Department department) {
        return R.ok(departmentService.create(department));
    }

    @PutMapping("/department/{id}")
    public R updateDepartment(@PathVariable Integer id,
                             @RequestBody Department department) {
        return R.ok(departmentService.update(id, department));
    }

    @DeleteMapping("/department/{id}")
    public R deleteDepartment(@PathVariable Integer id) {
        departmentService.delete(id);
        return R.ok();
    }

    @DeleteMapping("/departments")
    public R batchDelete(@RequestParam Integer[] ids) {
        departmentService.delete(ids);
        return R.ok();
    }
}
