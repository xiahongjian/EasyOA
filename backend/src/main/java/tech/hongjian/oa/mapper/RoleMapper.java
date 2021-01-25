package tech.hongjian.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findUserRoles(Integer userId);

    IPage<Role> findRoles(Page<Role> p, String name, String key, Status status);
}
