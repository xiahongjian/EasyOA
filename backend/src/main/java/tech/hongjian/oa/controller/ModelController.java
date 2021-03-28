package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.entity.enums.ModelType;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.ModelImageService;
import tech.hongjian.oa.service.ModelService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

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
@RequestMapping("/processes/models")
public class ModelController {
    private ModelService modelService;
    private ModelImageService modelImageService;

    @GetMapping("")
    public R listModel(@RequestParam(required = false) String modelId,
                       @RequestParam(required = false) String name,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        return R.ok(modelService.findByParams(page, limit, ModelType.BPMN, modelId, name));
    }

    @PostMapping("")
    public R updateProcessModel(@RequestParam("file") MultipartFile file, @RequestParam("comment") String comment) throws IOException {
        Model model = modelService.importModel(file.getInputStream(), comment);
        return R.ok(model);
    }

    @DeleteMapping("/{id}")
    public R deleteProcessModel(@PathVariable Integer id) {
        modelService.deleteModel(id);
        return R.ok();
    }

    @GetMapping("/{id}/xml")
    public R getProcessModelXml(@PathVariable Integer id) {

        return R.ok();
    }

    @GetMapping("/{id}/image")
    public void getProcessModelImage(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Model model = modelService.getModel(id);
        byte[] bytes = modelImageService.generateProcessImage(model);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(bytes);
    }
}
