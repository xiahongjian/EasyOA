package tech.hongjian.oa.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import tech.hongjian.oa.entity.Permission;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    
    List<Permission> findUserPermission(Long userId);
}
