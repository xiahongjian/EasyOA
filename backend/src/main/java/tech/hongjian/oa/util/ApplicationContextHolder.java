package tech.hongjian.oa.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by xiahongjian on 2021/4/12.
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }

    public static ApplicationContext getAppCtx() {
        return appCtx;
    }
}
