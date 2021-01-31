package tech.hongjian.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRel {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer roleId;

    public UserRoleRel(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}
