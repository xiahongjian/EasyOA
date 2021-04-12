package tech.hongjian.oa.model.anno;

import java.lang.annotation.*;

/**
 * 用于标记字段的作用是：保存某个字段对应的user信息
 * 用userField指定对应的user id字段
 *
 * Created by xiahongjian on 2021/4/12.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserInfo {
    /**
     * 用于指定存储user id的字段，从而根据其值查询user信息，
     * 并存储与此字段
     * @return
     */
    String userField();
}
