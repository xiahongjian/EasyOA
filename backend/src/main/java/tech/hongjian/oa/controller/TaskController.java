package tech.hongjian.oa.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.oa.flowable.FlowConstants;
import tech.hongjian.oa.flowable.service.FlowService;
import tech.hongjian.oa.model.R;
import tech.hongjian.oa.service.BizTaskService;
import tech.hongjian.oa.util.WebUtil;

import java.util.Collections;

/**
 * Created by xiahongjian on 2021/4/10.
 */
@RestController
@RequestMapping("/processes/tasks")
public class TaskController {
    @Autowired
    private BizTaskService bizTaskService;
    @Autowired
    private FlowService flowService;

    @GetMapping("")
    public R listTask(@RequestParam(required = false) String procDefName,
                      @RequestParam(required = false) String name,
                      @RequestParam(required = false) Integer suspended,
                      @RequestParam int page,
                      @RequestParam int limit) {
        return R.ok(bizTaskService.listTask(procDefName, name, null, suspended, page, limit, true));
    }

    @GetMapping("/mine")
    public R listMyTask(@RequestParam(required = false) String procDefKey,
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) Integer suspended,
                        @RequestParam int page,
                        @RequestParam int limit) {
        return R.ok(bizTaskService.listTask(procDefKey, name, WebUtil.currentUser().getId(), suspended, page, limit, true));
    }

    @PutMapping("/{taskId}/complete")
    public R completeTask(@PathVariable String taskId, @RequestParam(required = false) String action, @RequestParam(required = false) String comment) {
        if (StringUtils.isNotBlank(action)) {
            flowService.completeTask(taskId, comment, Collections.singletonMap(FlowConstants.V_ACTION, action));
        } else {
            flowService.completeTask(taskId, comment, null);
        }
        return R.ok();
    }

    @PutMapping("/{taskId}/reassign")
    public R reassign(@PathVariable String taskId, @RequestParam Integer userId, @RequestParam(required = false) String comment) {
        flowService.reassign(taskId, userId, comment);
        return R.ok();
    }


}
