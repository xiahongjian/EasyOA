package tech.hongjian.oa.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import tech.hongjian.oa.entity.Role;

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

}
