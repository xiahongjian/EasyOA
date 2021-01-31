package tech.hongjian.oa.entity;

import tech.hongjian.oa.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;


}
