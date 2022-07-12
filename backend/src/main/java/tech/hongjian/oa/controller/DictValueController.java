package tech.hongjian.oa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.entity.DictValue;
import tech.hongjian.oa.entity.enums.Status;
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
@RestController
@RequestMapping("/dict")
public class DictValueController {
    @Autowired
    private DictValueService dictValueService;

    @GetMapping("/datas")
    public R listDictValues(@RequestParam(required = false) String key, @RequestParam(required = false) String label,
                            @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer page,
                            @RequestParam(required = false) Integer limit) {
        return R.ok(dictValueService.listDictValue(key, label, Status.of(status), page, limit));
    }

    @GetMapping("/dataEnable")
    public R getEnableValues(@RequestParam String dictKey) {
        return R.ok(dictValueService.getEnableValueByDictKey(dictKey));
    }

    @GetMapping("/data/{id}")
    public R getDictData(@PathVariable Integer id) {
        return R.ok(dictValueService.getById(id));
    }

    @DeleteMapping("/data/{id}")
    public R deleteDictData(@PathVariable Integer id) {
        dictValueService.delete(id);
        return R.ok();
    }

    @DeleteMapping("/data/batch")
    public R batchDeleteDatas(@RequestParam Integer[] ids) {
        dictValueService.batchDelete(ids);
        return R.ok();
    }

    @PostMapping("/data")
    public R creteDictData(@RequestBody DictValue dictValue) {
        dictValueService.create(dictValue);
        return R.ok();
    }

    @PutMapping("/data/{id}")
    public R updateDictValue(@RequestBody DictValue dictValue) {
        dictValueService.updateById(dictValue);
        return R.ok();
    }
}
