package tech.hongjian.oa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.model.R;

/**
 * @author xiahongjian
 * @time 2021/3/19 22:10
 */
@RestController
public class FlowManageController {

    @GetMapping("/models")
    public R listModel(@RequestParam(required = false) String name, @RequestParam(required = false) String key) {

        return R.ok();
    }
}
