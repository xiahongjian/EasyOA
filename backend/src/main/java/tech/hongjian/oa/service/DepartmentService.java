package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.hongjian.oa.entity.Department;
import tech.hongjian.oa.entity.enums.Status;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface DepartmentService extends IService<Department> {

    Department findDepartmentById(Integer id);

    List<Department> getDepartmentTree(String query, Status status);

    Department getDepartmentWithLeader(Integer id);

    Department create(Department department);

    Department findDepartmentByName(String name);

    Department update(Integer id, Department department);

    void delete(Integer id);

    void delete(Integer[] ids);
}
