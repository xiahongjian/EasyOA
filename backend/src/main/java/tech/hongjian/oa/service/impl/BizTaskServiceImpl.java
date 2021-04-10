package tech.hongjian.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.model.TaskVo;
import tech.hongjian.oa.service.BizTaskService;

import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/10.
 */
@Service
public class BizTaskServiceImpl implements BizTaskService {
    @Setter(onMethod_ = {@Autowired})
    private TaskService taskService;

    @Override
    public TaskVo findById(String id) {
        return new TaskVo(taskService.createTaskQuery().taskId(id).singleResult());
    }

    @Override
    public IPage<TaskVo> listTask(Integer assigneeId, int page, int limit) {
        Page<TaskVo> result = new Page<>();
        TaskQuery query;
        if (assigneeId == null) {
            query = taskService.createTaskQuery();
        } else {
            query = taskService.createTaskQuery().taskAssignee(String.valueOf(assigneeId));
        }
        result.setTotal(query.count());
        result.setRecords(query.listPage((page - 1) * limit, limit)
                .stream().map(TaskVo::new).collect(Collectors.toList()));
        return result;
    }
}
