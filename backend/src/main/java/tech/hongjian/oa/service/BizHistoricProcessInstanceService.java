package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.hongjian.oa.model.HistoricProcessInstanceBo;

/**
 * Created by xiahongjian on 2021/4/21.
 */
public interface BizHistoricProcessInstanceService {

    IPage<HistoricProcessInstanceBo> listHistoricProcessInstance(String processName, Integer creator, Integer state, int page, int limit, boolean withUserInfo);
}
