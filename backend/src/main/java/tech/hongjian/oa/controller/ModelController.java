package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.enums.ModelType;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.ModelService;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
@Setter(onMethod_ = {@Autowired})
@RestController
@RequestMapping("/process")
public class ModelController {
    private ModelService modelService;

    @GetMapping("/models")
    public R listModel(@RequestParam(required = false) String modelId,
                       @RequestParam(required = false) String name,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        return R.ok(modelService.findByParams(page, limit, ModelType.BPMN, modelId, name));
    }

    @PostMapping("/model")
    public R updateProcessModel(@RequestParam("file") MultipartFile file, @RequestParam("comment") String comment) throws IOException {
        Model model = modelService.importModel(file.getInputStream());
        return R.ok(model);
    }
}
