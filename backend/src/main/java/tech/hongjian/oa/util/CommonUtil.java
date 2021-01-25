package tech.hongjian.oa.util;

/**
 * @author xiahongjian
 * @time 2021/1/25 23:05
 */
public class CommonUtil {

    public static String wrapWith(String value, String s) {
        if (value == null) {
            return null;
        }
        return s + value + s;
    }
}
