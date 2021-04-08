package tech.hongjian.oa.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiahongjian
 * @time 2021/2/3 19:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "sys.token")
public class SysTokenConfig {
    private Integer expire;
    private Integer refreshInterval;
}
