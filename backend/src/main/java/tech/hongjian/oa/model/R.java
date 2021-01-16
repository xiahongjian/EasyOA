package tech.hongjian.oa.model;

import java.util.HashMap;
import java.util.Map;

import tech.hongjian.oa.config.Code;

/**
 * @author xiahongjian
 * @since  2021-01-16 12:33:54
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = -6378855522995649222L;

    public static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_TOTAL = "total";

    public R() {
        code(Code.OK.getValue()).success(true);
    }

    public static R error() {
        return error(Code.SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(Code.SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        return new R().code(code).message(msg);
    }

    public static R error(Code code, String msg) {
        return error(code.getValue(), msg);
    }

    public static R ok(Object data) {
        return ok().data(data);
    }

    public static R ok(Map<String, Object> map) {
        return ok().withAll(map);
    }

    public static R ok() {
        return new R().success(true).code(Code.OK);
    }

    public R code(int code) {
        return with(KEY_CODE, code);
    }

    public R code(Code code) {
        return code(code.getValue());
    }

    public R message(String message) {
        return with(KEY_MESSAGE, message);
    }

    public R success(boolean success) {
        return with(KEY_SUCCESS, success);
    }

    public R data(Object data) {
        return with(KEY_DATA, data);
    }

    public R total(int total) {
        return with(KEY_TOTAL, total);
    }

    public R with(String key, Object value) {
        put(key, value);
        return this;
    }

    public R withAll(Map<? extends String, ? extends Object> m) {
        putAll(m);
        return this;
    }
}
