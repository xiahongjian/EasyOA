package tech.hongjian.oa.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import tech.hongjian.oa.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/1/31 20:04
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleWithMenu extends Role {
    private List<Integer> menuIds = new ArrayList<>();

    public RoleWithMenu(Role role, List<Integer> menuIds) {
        BeanUtils.copyProperties(role, this);
        this.menuIds = menuIds;
    }
}
