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

    Department getDepartmentById(Integer id);

    List<Department> getDepartmentTree(String query, Status status);

    Department getDepartmentWithLeader(Integer id);
}
