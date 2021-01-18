package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.DictValueService;

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
@RequestMapping("/dict/value")
public class DictValueController {
    private DictValueService dictValueService;

    @GetMapping("/value/list")
    public R getDictValues(@RequestParam(required = false) String dictKey) {
        return R.ok(dictValueService.getValueByDictKey(dictKey));
    }

    @GetMapping("/value/enable")
    public R getEnableValues(@RequestParam(required = false) String dictKey) {
        return R.ok(dictValueService.getEnableValueByDictKey(dictKey));
    }
}
