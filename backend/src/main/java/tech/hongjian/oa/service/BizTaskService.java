package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.model.TaskVo;

/**
 * Created by xiahongjian on 2021/4/10.
 */
public interface BizTaskService {

    TaskVo findById(String id);

    IPage<TaskVo> listTask(Integer assigneeId, int page, int limit);
}
