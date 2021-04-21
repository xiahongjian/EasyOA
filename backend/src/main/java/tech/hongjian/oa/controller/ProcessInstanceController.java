package tech.hongjian.oa.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.BizProcessInstanceService;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@RestController
@RequestMapping("/processes/instances")
public class ProcessInstanceController {
    @Setter(onMethod_ = {@Autowired})
    private BizProcessInstanceService bizProcessInstanceService;

    public R list(String name, Integer creator, int page, int limit) {

        return R.ok(bizProcessInstanceService.listProcessInstance(name, creator, page, limit, true));
    }
}
