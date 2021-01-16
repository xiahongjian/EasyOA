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
public class DictValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer dictId;

    private String label;

    private String value;

    private String remark;

    private Status status;
}
