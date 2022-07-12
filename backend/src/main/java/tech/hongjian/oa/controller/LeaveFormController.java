package tech.hongjian.oa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.LeaveFormService;
import tech.hongjian.oa.util.WebUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-11
 */
@RestController
public class LeaveFormController {
    @Autowired
    private LeaveFormService leaveFormService;

    @GetMapping("/leaveForms")
    public R listForms(@RequestParam(required = false) String type,
                       @RequestParam(required = false) Integer creatorId,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        return R.ok(leaveFormService.listForms(type, creatorId, page, limit));
    }

    @PostMapping("/leaveForm")
    public R createForms(@RequestBody LeaveForm form) {
        form.setCreatorId(WebUtil.currentUser().getId());
        leaveFormService.create(form);
        return R.ok();
    }

    @GetMapping("/leaveForm/{id}")
    public R getForm(@PathVariable Integer id) {
        return R.ok(leaveFormService.find(id));
    }

    @PutMapping("/leaveForm/{id}")
    public R updateForm(@PathVariable Integer id, @RequestBody LeaveForm form) {
        leaveFormService.update(id, form);
        return R.ok();
    }

    @DeleteMapping("/leaveForm/{id}")
    public R deleteForm(@PathVariable Integer id) {
        leaveFormService.delete(id);
        return R.ok();
    }

    @DeleteMapping("/leaveForms")
    public R batchDeleteForms(@RequestParam Integer[] ids) {
        leaveFormService.batchDelete(ids);
        return R.ok();
    }
}
