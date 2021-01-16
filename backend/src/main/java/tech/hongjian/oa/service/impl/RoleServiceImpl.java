package tech.hongjian.oa.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.mapper.RoleMapper;
import tech.hongjian.oa.service.RoleService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getUserRoles(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return roleMapper.findUserRoles(userId);
    }

}
