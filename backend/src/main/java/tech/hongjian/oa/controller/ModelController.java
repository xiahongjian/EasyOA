package tech.hongjian.oa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.enums.ModelType;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.service.ProcessResourceService;
import tech.hongjian.oa.util.WebUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiahongjian
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/processes/models")
public class ModelController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private ProcessResourceService processResourceService;

    @GetMapping("")
    public R listModel(@RequestParam(required = false) String key,
                       @RequestParam(required = false) String name,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        return R.ok(modelService.findByParams(page, limit, ModelType.BPMN, key, name));
    }

    @PostMapping("")
    public R updateProcessModel(@RequestParam MultipartFile file,
                                @RequestParam(required = false) String comment) throws IOException {
        Model model = modelService.importModel(file.getInputStream(), comment);
        return R.ok(model);
    }

    @GetMapping("/{id}")
    public R getProcessModel(@PathVariable Integer id) {
        return R.ok(modelService.getModel(id));
    }

    @PutMapping("/{id}")
    public R updateProcessModel(@PathVariable Integer id,
                                @RequestParam(required = false) MultipartFile file,
                                @RequestParam(required = false) String comment) throws IOException {
        modelService.updateModel(id, file == null ? null : file.getInputStream(), comment);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R deleteProcessModel(@PathVariable Integer id) {
        modelService.deleteModel(id);
        return R.ok();
    }

    // xml 下载
    @GetMapping("/{id}/xml")
    public void getProcessModelXml(@PathVariable Integer id, HttpServletResponse response) {
        Model model = modelService.getModel(id);
        WebUtil.writeXml(response, processResourceService.generateXmlData(model), model.getKey() + ".bpmn20.xml");
    }

    @GetMapping("/{id}/image")
    public void getProcessModelImage(@PathVariable Integer id, HttpServletResponse response) {
        Model model = modelService.getModel(id);
        byte[] bytes = processResourceService.generateProcessImage(model);
        WebUtil.writeImage(response, bytes);
    }

    @GetMapping("/{id}/deploy")
    public R deployProcess(@PathVariable Integer id) {
        modelService.deploy(id);
        return R.ok();
    }
}
