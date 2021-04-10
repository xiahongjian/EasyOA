package tech.hongjian.oa.flowable.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import tech.hongjian.oa.entity.FlowEntity;

import java.util.Map;

/**
 * Created by xiahongjian on 2021/4/10.
 */
public  abstract class BaseFlowBizFormService<M extends BaseMapper<T>, T extends FlowEntity> extends ServiceImpl<M, T> implements FlowBizFormService<T> {

    @Setter(onMethod_ = {@Autowired})
    protected FlowService flowService;

    @Override
    public String startProcess(T entity, String procDefKey, Map<String, Object> variables) {
        return flowService.startProcess(entity, procDefKey, variables);
    }

    @Override
    public String startProcess(T entity, String procDefKey) {
        return flowService.startProcess(entity, procDefKey);
    }

    @Override
    public String startProcess(T entity) {
        return flowService.startProcess(entity, supplyProcessDefinitionKey());
    }

    @Override
    public String startProcess(T entity, Map<String, Object> variables) {
        return flowService.startProcess(entity, supplyProcessDefinitionKey(), variables);
    }

    @Override
    public void approve(String taskId) {
        flowService.approve(taskId);
    }

    @Override
    public void approve(String taskId, String comment) {
        flowService.approve(taskId, comment);
    }

    @Override
    public void reject(String taskId, String comment) {
        flowService.reject(taskId, comment);
    }

    @Override
    public void completeTask(String taskId, String comment, Map<String, Object> variables) {
        flowService.completeTask(taskId, comment, variables);
    }
}
