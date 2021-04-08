package tech.hongjian.oa.config.flowable;

import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xiahongjian on 2021/4/8.
 */
@Configuration
public class FlowableConfiguration {

    @Bean
    public EngineConfigurationConfigurer<SpringIdmEngineConfiguration> customEngineConfigurer() {
        return idmEngineConfiguration -> idmEngineConfiguration
                .setIdmIdentityService(new CustomIdentityServiceImpl(idmEngineConfiguration));
    }
}
