package tech.hongjian.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
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
@Data
public class RoleMenuRel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    public RoleMenuRel(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

}
