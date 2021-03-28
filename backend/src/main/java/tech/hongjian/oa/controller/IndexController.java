package tech.hongjian.oa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.FlowService;
import tech.hongjian.oa.service.LeaveFormService;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.util.ImageGenerator;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author xiahongjian
 * @since 2020-03-18 21:59:22
 */
@RestController
public class IndexController {

    //    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/admin")
//    public String admin() {
//        return "admin";
//    }
//
//    @ResponseBody
//    @GetMapping("/admin/content")
//    public R adminContent() {
//        return R.ok("content");
//    }
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private LeaveFormService leaveFormService;

    @Autowired
    private FlowService flowService;


    @GetMapping("/test/{id}/start")
    public R start(@PathVariable Integer id) {

        String processInstanceId = flowService.startProcess(LeaveForm.class, id, "LeaveFormFlow");

        leaveFormService.lambdaUpdate().eq(LeaveForm::getId, id)
                .set(LeaveForm::getProcessInstanceId, processInstanceId)
                .update();
        return R.ok(processInstanceId);
    }

    @GetMapping("/test/{id}/history")
    public R history(@PathVariable Integer id) {
        LeaveForm form = leaveFormService.getById(id);
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processInstanceId(form.getProcessInstanceId()).singleResult();
        List<HistoricTaskInstance> list =
                historyService.createHistoricTaskInstanceQuery().processInstanceId(form.getProcessInstanceId()).list();

        for (HistoricTaskInstance his : list) {
            System.out.println("Task Id: " + his.getId());
            System.out.println("Name: " + his.getName());
            System.out.println("Process instance id: " + his.getProcessInstanceId());
            System.out.println("Process definition id: " + his.getProcessDefinitionId());
            System.out.println("Assignee: " + his.getAssignee());
        }
        return R.ok(list);
    }

    @Autowired
    private ModelService modelService;

    @GetMapping("/test/image")
    public void getImage(HttpServletResponse response) throws IOException {
        Model model = modelService.getModel(7);
        BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
        JsonNode jsonNode = (ObjectNode) new JsonMapper().readTree(model.getModelEditorJson());
        BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(jsonNode);

        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BufferedImage bufferedImage = diagramGenerator.generateImage(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), "宋体", "宋体", "宋体", ClassLoader.getSystemClassLoader(), 1.0, false);
//        BufferedImage image = ImageGenerator.createImage(bpmnModel);

        response.setHeader("Content-type", "image/png");
        response.getOutputStream().write(ImageGenerator.createByteArrayForImage(bufferedImage, "png"));
    }
}
