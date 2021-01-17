package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.mapper.RoleMapper;
import tech.hongjian.oa.service.RoleService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private RoleMapper roleMapper;

    @Override
    public List<Role> getUserRoles(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return roleMapper.findUserRoles(userId);
    }

}
