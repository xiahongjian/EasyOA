package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.model.TaskBo;

/**
 * Created by xiahongjian on 2021/4/10.
 */
public interface BizTaskService {

    TaskBo findById(String id);

    TaskBo findById(String id, boolean withUserInfo);

    IPage<TaskBo> listTask(String procDefName, String name, Integer assigneeId, boolean suspend, int page, int limit);

    IPage<TaskBo> listTask(String procDefName, String name, Integer assigneeId, boolean suspend, int page, int limit, boolean withUserInfo);

    TaskBo getUserInfo(TaskBo entity);
}
