package tech.hongjian.oa.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.DictValueService;
import tech.hongjian.oa.util.JSONUtil;

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
@RequestMapping("/dict")
public class DictValueController {
    private DictValueService dictValueService;

    @GetMapping("/dataList")
    public R getDictValues(@RequestParam(required = false) String key, @RequestParam(required = false) String label,
                           @RequestParam(required = false) String status, @RequestParam(required = false) Integer page,
                           @RequestParam(required = false) Integer pageSize) {
        Status s = StringUtils.isNotBlank(status) ? null : JSONUtil.toBean(status, Status.class);
        return R.ok(dictValueService.listDictValue(key, label, s, page, pageSize));
    }

    @GetMapping("/dataEnable")
    public R getEnableValues(@RequestParam String dictKey) {
        return R.ok(dictValueService.getEnableValueByDictKey(dictKey));
    }
}
