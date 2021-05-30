package tech.hongjian.oa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.entity.Model;
import tech.hongjian.oa.flowable.service.FlowService;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.model.TaskBo;
import tech.hongjian.oa.service.LeaveFormService;
import tech.hongjian.oa.service.ModelService;
import tech.hongjian.oa.util.CommonUtil;
import tech.hongjian.oa.util.ImageGenerator;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private TaskService taskService;

    @GetMapping("/test/task/{id}")
    public List<TaskBo> listTask(@PathVariable String id) {
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(id).active().list();
        return list.stream().map(this::createTaskBo).collect(Collectors.toList());
    }

    private TaskBo createTaskBo(Task task) {
        TaskBo bo = new TaskBo(task);
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(bo.getProcessInstanceId()).singleResult();

        if (NumberUtils.isDigits(instance.getStartUserId())) {
            bo.setSubmitter(CommonUtil.toInteger(instance.getStartUserId()));
        }
        bo.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        bo.setProcessDefinitionName(instance.getProcessDefinitionName());
        bo.setProcessDefinitionId(instance.getProcessDefinitionId());
        // 关联业务表单信息
        bo.setBusinessKey(instance.getBusinessKey());
        if (bo.getAssignee() == null) {

            List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
            List<Integer> candidateUsers = new ArrayList<>();
            List<String> candidateGroups = new ArrayList<>();
            for (IdentityLink link : identityLinksForTask) {
                String userId = link.getUserId();
                if (StringUtils.isNotBlank(userId)) {
                    String[] ids = link.getUserId().split(",");
                    if (ids != null && ids.length > 0) {
                        candidateUsers.addAll(Stream.of(ids).map(CommonUtil::toInteger).filter(Objects::nonNull).collect(Collectors.toList()));
                    }
                }
                String groupIds = link.getGroupId();
                if (StringUtils.isNotBlank(groupIds)) {
                    String[] groups = groupIds.split(",");
                    if (groups != null && groups.length > 0) {
                        candidateGroups.addAll(Arrays.asList(groups));
                    }
                }
            }
            bo.setCandidateUserIds(candidateUsers);
            bo.setCandidateGroups(candidateGroups);
        }

        return bo;
    }
}
