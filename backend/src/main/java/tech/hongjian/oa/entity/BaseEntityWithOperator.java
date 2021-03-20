package tech.hongjian.oa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiahongjian
 * @time 2021/3/19 21:50
 */
@Getter
@Setter
public abstract class BaseEntityWithOperator extends BaseEntity {
    private Integer createdBy;
    private Integer updatedBy;
}
