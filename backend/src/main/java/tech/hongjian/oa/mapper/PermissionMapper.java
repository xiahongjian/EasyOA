package tech.hongjian.oa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    
    @Select("select p.* from permission p left join user_permission_rel r on p.id=r.permission_id left join user u on r.user_id=u.id where u.id=#{id}")
    List<Permission> findUserPermission(@Param("id") Long id);
}
