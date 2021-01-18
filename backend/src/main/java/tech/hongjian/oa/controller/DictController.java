package tech.hongjian.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.DictService;
import tech.hongjian.oa.util.JSONUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/typelist")
    public R getDictTypes(@RequestParam(required = false) String name, @RequestParam(required = false) String key,
                          @RequestParam(required = false) String status,
                          @RequestParam int page, @RequestParam int limit) {
        return R.ok(dictService.getDict(name, key, status == null ? null : JSONUtil.toBean(status, Status.class), page, limit));
    }

    @GetMapping("/type/{dictKey}")
    public R getDictType(@PathVariable String dictKey) {
        return R.ok(dictService.getDictByKey(dictKey));
    }

    @PostMapping("/type")
    public R createDictType(@RequestBody Dict dict) {
        return R.ok(dictService.create(dict));
    }

    @PutMapping("/type/{dictType}")
    public R updateDict(@PathVariable String dictType, @RequestBody Dict dict) {
        return R.ok(dictService.update(dict));
    }

    @DeleteMapping("/type/{dictType}")
    public R deleteDict(@PathVariable String dictType) {
        return R.ok(dictService.delete(dictType));
    }
}
