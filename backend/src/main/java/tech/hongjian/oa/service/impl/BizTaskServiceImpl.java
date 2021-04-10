package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.TaskBo;
import tech.hongjian.oa.service.BizTaskService;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/10.
 */
@Service
public class BizTaskServiceImpl implements BizTaskService {
    @Setter(onMethod_ = {@Autowired})
    private TaskService taskService;

    @Setter(onMethod_ = {@Autowired})
    private UserService userService;

    @Setter(onMethod_ = {@Autowired})
    private RuntimeService runtimeService;

    @Override
    public TaskBo findById(String id) {
        return findById(id, false);
    }

    @Override
    public TaskBo findById(String id, boolean withUserInfo) {
        return createTaskBo(taskService.createTaskQuery().taskId(id).singleResult(), withUserInfo);
    }

    @Override
    public IPage<TaskBo> listTask(String procDefName, String name, Integer assigneeId, boolean suspend, int page, int limit) {
        return listTask(procDefName, name, assigneeId, suspend, page, limit, false);
    }

    @Override
    public IPage<TaskBo> listTask(String procDefName, String name, Integer assigneeId, boolean suspend, int page, int limit, boolean withUserInfo) {
        Page<TaskBo> result = new Page<>();
        TaskQuery query = taskService.createTaskQuery();
        if (StringUtils.isNotBlank(procDefName)) {
            query.processDefinitionKeyLikeIgnoreCase(CommonUtil.wrapWithPercent(procDefName));
        }
        if (StringUtils.isNotBlank(name)) {
            query.taskNameLikeIgnoreCase(CommonUtil.wrapWithPercent(name));
        }
        if (suspend) {
            query.suspended();
        }
        if (assigneeId != null) {
            query.taskAssignee(String.valueOf(assigneeId));
        }
        result.setTotal(query.count());
        result.setRecords(query.listPage((page - 1) * limit, limit)
                .stream().map(e -> createTaskBo(e, withUserInfo)).collect(Collectors.toList()));
        return result;
    }

    private TaskBo createTaskBo(Task task, boolean withUserInfo) {
        TaskBo bo = new TaskBo(task);
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(bo.getProcessInstanceId()).singleResult();
        bo.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        bo.setProcessDefinitionName(instance.getProcessDefinitionName());
        bo.setProcessDefinitionId(instance.getProcessDefinitionId());
        // TODO 关联业务表单信息

        return withUserInfo ? getUserInfo(bo) : bo;
    }

    @Override
    public TaskBo getUserInfo(TaskBo entity) {
        if (entity != null) {
            entity.setAssigneeUserInfo(userService.getUserBriefInfo(entity.getAssignee()));
            entity.setOwnerUserInfo(userService.getUserBriefInfo(entity.getOwner()));
        }
        return entity;
    }
}
