package tech.hongjian.oa.controller;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tech.hongjian.oa.entity.Model;
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
@Controller
@RequestMapping("/model")
public class ModelController {
    private ModelService modelService;

    @PostMapping("/process")
    public R updateProcessModel(@RequestParam MultipartFile file) throws IOException {
        Model model = modelService.createOrUpdateProcessModel(file.getInputStream());
        return R.ok(model);
    }
}
