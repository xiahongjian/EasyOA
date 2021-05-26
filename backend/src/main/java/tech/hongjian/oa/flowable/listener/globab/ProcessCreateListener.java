package tech.hongjian.oa.flowable.listener.globab;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.entity.FlowEntity;
import tech.hongjian.oa.flowable.FlowConstants;
import tech.hongjian.oa.flowable.service.FlowService;

/**
 * @author xiahongjian
 * @time 2021/3/15 21:25
 */
@Slf4j
@Setter(onMethod_ = {@Autowired})
@Component
public class ProcessCreateListener extends AbstractFlowableEngineEventListener {
    private RuntimeService runtimeService;
    private FlowService flowService;

    @Override
    protected void processCreated(FlowableEngineEntityEvent event) {
        if (event instanceof FlowableEntityEventImpl) {
            FlowableEntityEventImpl evt = (FlowableEntityEventImpl) event;
            String instanceId = evt.getProcessInstanceId();
            ProcessInstance processInstance =
                    runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            String bizKey = processInstance.getBusinessKey();
            if (bizKey != null) {
                FlowEntity entity = flowService.getBizFormByBizKey(bizKey);
                runtimeService.setVariable(instanceId, FlowConstants.V_FORM, entity);
            }
            // 设置启用跳过表达式
            runtimeService.setVariable(instanceId, FlowConstants.V_ENABLE_SKIP_EXPRESSION, true);
        }
    }
}
