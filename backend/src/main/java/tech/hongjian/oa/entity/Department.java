package tech.hongjian.oa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.hongjian.oa.entity.enums.Status;

/**
 * <p>
 *
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer leaderId;

    private Integer parentId;

    private Status status;

    private Integer sort;


}
