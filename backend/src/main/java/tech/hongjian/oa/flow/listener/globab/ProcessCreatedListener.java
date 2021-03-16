package tech.hongjian.oa.flow.listener.globab;

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
import tech.hongjian.oa.flow.FlowVariables;
import tech.hongjian.oa.service.FlowService;

/**
 * @author xiahongjian
 * @time 2021/3/15 21:25
 */
@Slf4j
@Setter(onMethod_ = {@Autowired})
@Component
public class ProcessCreatedListener extends AbstractFlowableEngineEventListener {
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
                runtimeService.setVariable(instanceId, FlowVariables.V_FORM, entity);
            }
        }
    }
}
