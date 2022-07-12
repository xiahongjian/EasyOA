package tech.hongjian.oa.controller;

import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.BizProcessDefinitionService;
import tech.hongjian.oa.service.ProcessResourceService;
import tech.hongjian.oa.util.WebUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiahongjian on 2021/3/31.
 */
@RestController
@RequestMapping("/processes/definitions")
public class ProcessDefinitionController {
    @Autowired
    private BizProcessDefinitionService bizProcessDefinitionService;
    @Autowired
    private ProcessResourceService processResourceService;

    @RequestMapping("")
    public R listProcDef(@RequestParam(required = false) String key,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) Integer suspend,
                         @RequestParam Integer page,
                         @RequestParam Integer limit) {
        return R.ok(bizProcessDefinitionService.listProcessDefinition(key, name, suspend, page, limit));
    }


    // xml 下载
    @GetMapping("/{id}/xml")
    public void getProcessModelXml(@PathVariable String id, HttpServletResponse response) {
        ProcessDefinition definition = bizProcessDefinitionService.getProcDefById(id);
        byte[] bytes = processResourceService.generateXmlData(id);
        WebUtil.writeXml(response, bytes, definition.getKey() + ".bpmn20.xml");
    }

    @GetMapping("/{id}/image")
    public void getProcessModelImage(@PathVariable String id, HttpServletResponse response) {
        byte[] bytes = processResourceService.generateProcessImage(id);
        WebUtil.writeImage(response, bytes);
    }

    @GetMapping("/{id}/suspend")
    public R suspendProcessDefinition(@PathVariable String id) {
        bizProcessDefinitionService.suspend(id);
        return R.ok();
    }

    @GetMapping("/{id}/active")
    public R activeProcessDefinition(@PathVariable String id) {
        bizProcessDefinitionService.active(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R deleteProcessDefinition(@PathVariable String id) {
        bizProcessDefinitionService.delete(id);
        return R.ok();
    }
}
