package tech.hongjian.oa.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import tech.hongjian.oa.entity.Permission;
import tech.hongjian.oa.mapper.PermissionMapper;
import tech.hongjian.oa.service.PermissionService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> getUserPermission(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        return permissionMapper.findUserPermission(id);
    }

}
