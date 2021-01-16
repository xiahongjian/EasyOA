package tech.hongjian.oa.config;

import lombok.Getter;

/**
 * @author xiahongjian
 * @since  2020-05-30 04:01:57
 */
@Getter
public enum Code {
    OK(20000), // OK
    UNAUTHORIZED(30000), // 未登录
    ACCOUNT_LOCKED(30001),
    CREDENTIALS_EXPIRED(30002), // 凭证过期
    ACCOUNT_EXPIRED(30003), // 账号过期
    ACCOUNT_DISABLED(30004), // 账号已禁用
    BAD_CREDENTIALS(30005), // 凭证信息错误
    ERROR_TOKEN(30006), // token格式不正确
    NO_PERMISSION(31000), // 无权限
    SERVER_ERROR(50000) // 系统错误
    ;

    private int value;

    Code(int value) {
        this.value = value;
    }
}
