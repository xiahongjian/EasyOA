package tech.hongjian.oa.util;

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

    public static String wrapperWithPercent(String value) {
        return wrapWith(value, "%");
    }

    public static  <T extends BaseEntity> T setEntityDefault(T entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
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
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdatedBy(updatedBy);
        return entity;
    }
}
