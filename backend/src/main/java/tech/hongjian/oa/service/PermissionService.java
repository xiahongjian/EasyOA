package tech.hongjian.oa.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.Permission;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
public interface PermissionService extends IService<Permission> {
    List<Permission> getUserPermission(Long id);
}
