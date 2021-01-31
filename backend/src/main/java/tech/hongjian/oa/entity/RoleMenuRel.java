package tech.hongjian.oa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-31
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenuRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer menuId;


}
