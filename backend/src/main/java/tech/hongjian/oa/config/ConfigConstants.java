package tech.hongjian.oa.config;
/**
 * @author xiahongjian
 * @time   2020-01-09 20:34:03
 */
public interface ConfigConstants {
    interface URLs {
        String HOME = "/";
        String LOGIN = "/login";
        String LOGOUT = "/logout";
    }

    String PARAM_USERNAME = "username";
    String PARAM_PASSWORD = "password";

    String DEFAULT_PASSWORD = "123456789";

    interface DictKeys {
        /**
         * 当启动流程时，没有指定submit
         */
        String FLOW_DEFAULT_SUBMITTER = "flowDefaultSubmitter";
    }
}
