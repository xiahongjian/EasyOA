package tech.hongjian.oa.controller;


import lombok.Setter;
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
@Setter(onMethod_ = {@Autowired})
@RestController
public class LeaveFormController {
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
}
