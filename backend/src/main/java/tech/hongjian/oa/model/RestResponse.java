package tech.hongjian.oa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.hongjian.oa.config.Code;

/**
 * @author xiahongjian
 * @time   2019-12-12 23:01:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private boolean success;
    private int code;
    private String msg;
    private T data;

    public static RestResponse<?> ok() {
        return new RestResponse<>(true, Code.OK.getCode(), null, null);
    }

    public static RestResponse<?> ok(String msg) {
        return new RestResponse<>(true, Code.OK.getCode(), msg, null);
    }

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<>(true, Code.OK.getCode(), null, data);
    }

    public static RestResponse<?> fail(int code, String msg) {
        return new RestResponse<>(false, code, msg, null);
    }

    public static RestResponse<?> fail(Code code, String msg) {
        return new RestResponse<>(false, code.getCode(), msg, null);
    }

    public static RestResponse<?> fail(String msg) {
        return fail(-1, msg);
    }
}
