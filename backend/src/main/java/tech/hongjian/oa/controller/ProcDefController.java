package tech.hongjian.oa.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.ProcDefService;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@Setter(onMethod_ = {@Autowired})
@RestController
@RequestMapping("/processes/definitions")
public class ProcDefController {
    private ProcDefService procDefService;

    @RequestMapping("")
    public R listProcDef(@RequestParam String key, @RequestParam Integer start, @RequestParam Integer limit) {
        return R.ok(procDefService.listProcessDefinition(key, start, limit));
    }


}
