package tech.hongjian.oa.config.flowable;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import tech.hongjian.oa.flowable.listener.global.ProcessCreateListener;
import tech.hongjian.oa.flowable.listener.global.UpdateBizFormVariableListener;

/**
 * @author xiahongjian
 * @time 2021/3/15 21:13
 */
@Configuration
public class FlowableGlobalListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final SpringProcessEngineConfiguration configuration;
    private final ProcessCreateListener processCreateListener;
    private final UpdateBizFormVariableListener updateBizFormVariableListener;

    public FlowableGlobalListenerConfig(SpringProcessEngineConfiguration configuration,
                                        ProcessCreateListener processCreateListener,
                                        UpdateBizFormVariableListener updateBizFormVariableListener) {
        this.configuration = configuration;
        this.processCreateListener = processCreateListener;
        this.updateBizFormVariableListener = updateBizFormVariableListener;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        // 注册流程实例创建监听器
        dispatcher.addEventListener(processCreateListener, FlowableEngineEventType.PROCESS_CREATED);
        // task完成时更新表单变量
        dispatcher.addEventListener(updateBizFormVariableListener, FlowableEngineEventType.TASK_COMPLETED);
        // 注册任务分配监听器
//        dispatcher.addEventListener(null, FlowableEngineEventType.TASK_ASSIGNED);
    }
}
