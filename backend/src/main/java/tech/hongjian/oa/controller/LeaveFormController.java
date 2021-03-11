package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.LeaveFormService;

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
                       @RequestParam(required = false) Integer requester,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {

        return R.ok(leaveFormService.listForms(type, requester, page, limit));
    }
}
