package tech.hongjian.oa.controller.vo;

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
public class RoleVO extends Role {
    private List<Integer> menuIds = new ArrayList<>();

    public RoleVO(Role role, List<Integer> menuIds) {
        BeanUtils.copyProperties(role, this);
        this.menuIds = menuIds;
    }
}
