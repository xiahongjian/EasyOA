package tech.hongjian.oa.util;

import org.flowable.common.engine.impl.db.SuspensionState;
import tech.hongjian.oa.entity.BaseEntity;
import tech.hongjian.oa.entity.BaseEntityWithOperator;

import java.time.LocalDateTime;

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

    public static String wrapWithPercent(String value) {
        return wrapWith(value, "%");
    }

    public static  <T extends BaseEntity> T setEntityDefault(T entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    public static  <T extends BaseEntityWithOperator> T setEntityDefault(T entity, Integer createdBy) {
        LocalDateTime now = LocalDateTime.now();
        entity = setEntityDefault(entity);
        entity.setCreatedBy(createdBy);
        entity.setUpdatedBy(createdBy);
        return entity;
    }

    public static  <T extends BaseEntityWithOperator> T setUpdateDefault(T entity, Integer updatedBy) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(updatedBy);
        return entity;
    }

    public static boolean isSuspendState(Integer code) {
        return code != null && code == SuspensionState.SUSPENDED.getStateCode();
    }

    public static boolean isActiveState(Integer code) {
        return code != null && code == SuspensionState.ACTIVE.getStateCode();
    }

    public static Integer toInteger(String value) {
        return value == null ? null : Integer.valueOf(value);
    }
}
