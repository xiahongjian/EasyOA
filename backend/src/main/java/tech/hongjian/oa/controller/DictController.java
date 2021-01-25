package tech.hongjian.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.Dict;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.DictService;

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

    @GetMapping("/types")
    public R listDictTypes(@RequestParam(required = false) String name, @RequestParam(required = false) String key,
                           @RequestParam(required = false) Integer status,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
        return R.ok(dictService.getDict(name, key, Status.of(status), page, limit));
    }

    @GetMapping("/type/{id}")
    public R getDictType(@PathVariable Integer id) {
        return R.ok(dictService.getDictById(id));
    }

    @PostMapping("/type")
    public R createDictType(@RequestBody Dict dict) {
        return R.ok(dictService.create(dict));
    }

    @PutMapping("/type/{id}")
    public R updateDict(@PathVariable Integer id, @RequestBody Dict dict) {
        return R.ok(dictService.update(dict));
    }

    @DeleteMapping("/type/{id}")
    public R deleteDict(@PathVariable Integer id) {
        return R.ok(dictService.delete(id));
    }

    @DeleteMapping("/type/batch")
    public R deleteDicts(@RequestParam Integer[] ids) {
        dictService.delete(ids);
        return R.ok();
    }
}
