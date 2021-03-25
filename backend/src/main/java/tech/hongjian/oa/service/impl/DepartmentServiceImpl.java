package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Department;
import tech.hongjian.oa.entity.enums.Status;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.mapper.DepartmentMapper;
import tech.hongjian.oa.service.DepartmentService;
import tech.hongjian.oa.util.CommonUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (id == null) {
            return null;
        }
        return baseMapper.selectByIdWithLeader(id);
    }

    @Override
    public Department create(Department department) {
        Department entity = validate(department);
        entity = CommonUtil.setEntityDefault(entity);
        save(entity);
        return entity;
    }

    private Department validate(Department department) {
        Department found = getDepartmentByName(department.getName());
        if ( found != null && !found.getId().equals(department.getId())) {
            throw new CommonServiceException("名称为[" + department.getName() + "]的部门已经存在");
        }
        // 当前端传来的数据中parentId为-1时，表示为一级部门，将parentId设置为null
        if (department.getParentId() == -1) {
            department.setParentId(null);
        }
        if (department.getParentId() != null && getById(department.getParentId()) == null) {
            throw new CommonServiceException("指定的父级部门不存在，ID为[" + department.getParentId() + "]");
        }
        return department;
    }

    @Override
    public Department getDepartmentByName(String name) {
        return lambdaQuery().eq(Department::getName, name).one();
    }

    @Override
    public Department update(Integer id, Department department) {
        if (getDepartmentById(id) == null) {
            throw new CommonServiceException("ID为[" + id + "]的部门不存在");
        }
        department.setId(id);
        Department entity = validate(department);
        entity.setUpdatedAt(LocalDateTime.now());
        updateById(entity);
        return entity;
    }

    @Override
    public void delete(Integer id) {
        if (getDepartmentById(id) == null) {
            throw new CommonServiceException("ID为[" + id + "]的部门不存在");
        }
        removeById(id);
    }

    @Override
    public void delete(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        removeByIds(Arrays.asList(ids));
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
