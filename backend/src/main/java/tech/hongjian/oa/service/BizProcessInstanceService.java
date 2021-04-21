package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.model.ProcessInstanceBo;

/**
 * Created by xiahongjian on 2021/4/21.
 */
public interface BizProcessInstanceService {

    IPage<ProcessInstanceBo> listProcessInstance(String processName, Integer creator, int page, int limit, boolean withUserInfo);
}
