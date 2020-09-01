package tech.hongjian.oa.config;

import lombok.Getter;

/**
 * @author xiahongjian
 * @since  2020-05-30 04:01:57
 */
@Getter
public enum Code {
    OK(1000), // OK
    NOT_LOGIN(2001), // 未登录
    LONGIN_ERROR(2002), // 登录错误
    NO_PERMISSION(2003), // 无权限
    SERVER_ERROR(5000) // 系统错误
    ;

    private int code;

    Code(int code) {
        this.code = code;
    }
}
