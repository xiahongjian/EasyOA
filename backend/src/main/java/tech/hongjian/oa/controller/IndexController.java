package tech.hongjian.oa.controller;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.LeaveFormService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @GetMapping("/test/{id}/start")
    public R start(@PathVariable Integer id) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("init", "init");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "LeaveFormFlow", String.valueOf(id), vars);

        leaveFormService.lambdaUpdate().eq(LeaveForm::getId, id)
                .set(LeaveForm::getProcessInstanceId, processInstance.getProcessInstanceId())
                .update();
        return R.ok(processInstance.getProcessInstanceId());
    }

    @GetMapping("/test/{id}/history")
    public R history(@PathVariable Integer id) {
        LeaveForm form = leaveFormService.getById(id);
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processInstanceId(form.getProcessInstanceId()).singleResult();
        List<HistoricActivityInstance> list =
                historyService.createHistoricActivityInstanceQuery().processInstanceId(form.getProcessInstanceId()).list();
        return R.ok(list);
    }
}
