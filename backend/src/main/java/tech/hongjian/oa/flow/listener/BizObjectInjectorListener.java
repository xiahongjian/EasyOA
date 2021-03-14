package tech.hongjian.oa.flow.listener;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.entity.LeaveForm;
import tech.hongjian.oa.flow.FlowVariables;
import tech.hongjian.oa.service.LeaveFormService;

/**
 * @author xiahongjian
 * @time 2021/3/14 22:21
 */
@Component("bizObjectInjector")
public class BizObjectInjectorListener implements ExecutionListener {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private LeaveFormService leaveFormService;

    @Override
    public void notify(DelegateExecution execution) {
        String instanceId = execution.getProcessInstanceId();
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String bizKey = processInstance.getBusinessKey();
        if (bizKey != null) {
            LeaveForm form = leaveFormService.getById(Integer.valueOf(bizKey));
            runtimeService.setVariable(execution.getId(), FlowVariables.FORM, form);
        }
    }
}
