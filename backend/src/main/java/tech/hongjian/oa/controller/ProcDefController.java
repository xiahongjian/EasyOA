package tech.hongjian.oa.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.ModelImageService;
import tech.hongjian.oa.service.ProcDefService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@Setter(onMethod_ = {@Autowired})
@RestController
@RequestMapping("/processes/definitions")
public class ProcDefController {
    private ProcDefService procDefService;
    private ModelImageService modelImageService;

    @RequestMapping("")
    public R listProcDef(@RequestParam(required = false) String key,
                         @RequestParam(required = false) String name,
                         @RequestParam Integer page,
                         @RequestParam Integer limit) {
        return R.ok(procDefService.listProcessDefinition(key, name, page, limit));
    }


    // xml 下载
    @GetMapping("/{id}/xml")
    public void getProcessModelXml(@PathVariable Integer id, HttpServletResponse response) throws IOException {

    }

    @GetMapping("/{id}/image")
    public void getProcessModelImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        byte[] bytes = modelImageService.generateProcessImage(id);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }
}
