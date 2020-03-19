package tech.hongjian.oa.config;
/** 
 * @author xiahongjian
 * @time   2020-01-09 20:34:03
 */
public interface ConfigConsts {
    interface SessionKey {
        String USER = "user";
    }
    
    interface URL {
        String LOGIN = "/login";
        String LOGOUT = "/logout";
    }
    
    String PARAM_USERNAME = "username";
    String PARAM_PASSWORD = "password";
    
    String CONTENT_TYPE = "application/json;charset=UTF-8";
}
