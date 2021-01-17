package tech.hongjian.oa.exception;

import lombok.Getter;
import tech.hongjian.oa.config.Code;

/**
 * @author xiahongjian
 * @since 2021/1/17 21:52
 */
@Getter
public class CommonServiceException extends RuntimeException {
    private Code code;

    public CommonServiceException() {
        code = Code.SERVER_ERROR;
    }

    public CommonServiceException(String msg) {
        this(Code.SERVER_ERROR, msg, null);
    }

    public CommonServiceException(String msg, Throwable cause) {
        this(Code.SERVER_ERROR, msg, cause);
    }

    public CommonServiceException(Code code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

}
