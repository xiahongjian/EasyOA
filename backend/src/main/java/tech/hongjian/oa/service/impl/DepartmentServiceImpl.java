package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Department;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.mapper.DepartmentMapper;
import tech.hongjian.oa.service.DepartmentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
@Setter(onMethod_ = {@Autowired})
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public Department getDepartmentById(Integer id) {
        if (id == null) {
            return null;
        }
        return getById(id);
    }

    @Override
    public List<Department> getDepartmentTree(String query, Status status) {
        LambdaQueryChainWrapper<Department> queryChainWrapper =
                lambdaQuery();
        if (StringUtils.isNotBlank(query)) {
            queryChainWrapper.like(Department::getName, "%" + query + "%");
        }
        if (status != null) {
            queryChainWrapper.eq(Department::getStatus, status);
        }
        List<Department> list = queryChainWrapper.orderByAsc(Department::getSort).list();
        return toTree(list);
    }

    @Override
    public Department getDepartmentWithLeader(Integer id) {
        return null;
    }

    private List<Department> toTree(List<Department> departments) {
        if (departments.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Department> map =
                departments.stream().collect(Collectors.toMap(Department::getId, d -> d));
        List<Department> tree = new ArrayList<>();
        for (Department department : departments) {
            if (department.getParentId() == null) {
                tree.add(department);
                continue;
            }
            if (map.containsKey(department.getParentId())) {
                map.get(department.getParentId()).getChildren().add(department);
            }
        }
        return tree;
    }
}
