package tech.hongjian.oa.flowable.listener.global;

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
 * Created by xiahongjian on 2021/5/26.
 */
@Slf4j
@Component
public class UpdateBizFormVariableListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
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
        }
    }
}
