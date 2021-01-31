package tech.hongjian.oa.controller.vo;

import lombok.Data;
import tech.hongjian.oa.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/1/31 20:04
 */
@Data
public class RoleVO extends Role {
    private List<Integer> menuIds = new ArrayList<>();
}
