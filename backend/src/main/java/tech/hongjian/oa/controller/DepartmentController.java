package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/deparment/{id}")
    public R getDepartment(@PathVariable Integer id) {
        return R.ok(departmentService.getDepartmentWithLeader(id));
    }
}
