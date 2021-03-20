package tech.hongjian.oa.config;

import lombok.Setter;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import tech.hongjian.oa.flow.listener.globab.ProcessCreatedListener;

/**
 * @author xiahongjian
 * @time 2021/3/15 21:13
 */
@Setter(onMethod_ = {@Autowired})
@Configuration
public class FlowableGlobalListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    private SpringProcessEngineConfiguration configuration;
    private ProcessCreatedListener processCreatedListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        // 注册流程实例创建监听器
        dispatcher.addEventListener(processCreatedListener, FlowableEngineEventType.PROCESS_CREATED);
        // 注册任务分配监听器
//        dispatcher.addEventListener(null, FlowableEngineEventType.TASK_ASSIGNED);
    }
}