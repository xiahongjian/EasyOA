package tech.hongjian.oa.flowable.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.FlowEntity;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.flowable.FlowVariables;
import tech.hongjian.oa.flowable.service.FlowBizFormService;
import tech.hongjian.oa.flowable.service.FlowService;

import java.util.Collections;
import java.util.Map;

/**
 * Created by xiahongjian on 2021/3/16.
 */
@Slf4j
@Setter(onMethod_ = {@Autowired})
@Service
public class FlowServiceImpl implements FlowService {
    public static final String SERVICE_BEAN_NAME_SUFFIX = "ServiceImpl";
    public static final String BIZ_KEY_SEPARATOR = ":";

    private RuntimeService runtimeService;
    private ApplicationContext appCtx;
    private TaskService taskService;

    @Override
    public <T extends FlowEntity> String startProcess(T form, String processDefKey, Map<String, Object> variables) {
        return form == null
                ? startProcess(null, null, processDefKey, variables)
                : startProcess(form.getClass(), form.getId(), processDefKey, variables);
    }

    @Override
    public <T extends FlowEntity> String startProcess(T form, String processDefKey) {
        return startProcess(form, processDefKey, null);
    }

    @Override
    public <T extends FlowEntity> String startProcess(Class<T> clazz, Integer id, String processDefKey, Map<String, Object> variables) {
        if (StringUtils.isBlank(processDefKey)) {
            throw new IllegalArgumentException("Process key不能为空");
        }
        if (clazz == null || id == null) {
            ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefKey, variables);
            return instance.getId();
        }

        String bizKey = id2BizKey(clazz.getSimpleName(), id);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefKey, bizKey, variables);
        return instance.getId();
    }

    @Override
    public <T extends FlowEntity> String startProcess(Class<T> clazz, Integer id, String processDefKey) {
        return startProcess(clazz, id, processDefKey, null);
    }


    @Override
    public <T extends FlowEntity> T getBizFormByBizKey(String bizKey) {
        String formClass = getBizFormClassName(bizKey);
        FlowBizFormService<? extends FlowEntity> bizFormService = getBizFormService(formClass);
        if (bizFormService == null) {
            log.warn("类：{}对应的service bean未找到。", formClass);
            return null;
        }
        return (T) bizFormService.getByBizKey(bizKey);
    }

    @Override
    public <T extends FlowEntity> void updateBizFormVariable(String processInstanceId, T bizForm) {
        runtimeService.setVariable(processInstanceId, FlowVariables.V_FORM, bizForm);
    }

    private Task checkTaskExisted(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new CommonServiceException("任务ID为[{" + taskId + "}]的任务不存在");
        }
        return task;
    }

    @Override
    public void approve(String taskId) {
        approve(taskId, null);
    }

    @Override
    public void approve(String taskId, String comment) {
        checkTaskExisted(taskId);
        if (StringUtils.isNoneBlank(comment)) {
            taskService.addComment(taskId, null, comment);
        }
        taskService.complete(taskId, Collections.singletonMap(FlowVariables.V_ACTION, FlowVariables.APPROVE));
    }

    @Override
    public void reject(String taskId, String comment) {
        checkTaskExisted(taskId);
        if (StringUtils.isNoneBlank(comment)) {
            taskService.addComment(taskId, null, comment);
        }
        taskService.complete(taskId, Collections.singletonMap(FlowVariables.V_ACTION, FlowVariables.REJECT));
    }

    @Override
    public void completeTask(String taskId, String comment, Map<String, Object> variables) {
        checkTaskExisted(taskId);
        if (StringUtils.isNotBlank(comment)) {
            taskService.addComment(taskId, null, comment);
        }
        taskService.complete(taskId, variables);
    }

    private FlowBizFormService<? extends FlowEntity> getBizFormService(String formClass) {
        if (StringUtils.isBlank(formClass)) {
            return null;
        }
        String serviceBeanName = getServiceBeanName(formClass);
        if (appCtx.containsBean(serviceBeanName)) {
            Object serviceBean = appCtx.getBean(serviceBeanName);
            if (serviceBean instanceof FlowBizFormService) {
                return (FlowBizFormService<? extends FlowEntity>) serviceBean;
            }
        }
        return null;
    }

    private String getServiceBeanName(String formClass) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.firstCharToLower(formClass) + SERVICE_BEAN_NAME_SUFFIX;
    }

    @Override
    public String id2BizKey(String clazzName, Integer id) {
        return clazzName + BIZ_KEY_SEPARATOR + id;
    }

    @Override
    public Integer bizKey2Id(String bizKey) {
        return Integer.valueOf(bizKey.substring(bizKey.indexOf(BIZ_KEY_SEPARATOR) + 1));
    }

    @Override
    public String getBizFormClassName(String bizKey) {
        return bizKey.substring(0, bizKey.indexOf(BIZ_KEY_SEPARATOR));
    }
}
