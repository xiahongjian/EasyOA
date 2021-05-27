package tech.hongjian.oa.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.BizHistoricProcessInstanceService;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@RestController
@RequestMapping("/processes/instances")
public class HistoricProcessInstanceController {
    @Setter(onMethod_ = {@Autowired})
    private BizHistoricProcessInstanceService bizHistoricProcessInstanceService;

    @GetMapping("")
    public R list(String name, Integer creator, int page, int limit) {

        return R.ok(bizHistoricProcessInstanceService.listHistoricProcessInstance(name, creator, page, limit, true));
    }
}
