package tech.hongjian.oa.mapper;

import tech.hongjian.oa.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface DepartmentMapper extends BaseMapper<Department> {
    Department selectByIdWithLeader(Integer id);
}
