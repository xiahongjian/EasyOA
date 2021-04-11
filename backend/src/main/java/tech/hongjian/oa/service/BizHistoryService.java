package tech.hongjian.oa.service;

import tech.hongjian.oa.model.HistoricActivityBo;
import tech.hongjian.oa.model.HistoricTaskBo;

import java.util.List;

/**
 * @author xiahongjian
 * @time 2021/4/11 22:17
 */
public interface BizHistoryService {

    List<HistoricActivityBo> listHistoricActivity(String processId, boolean withUserInfo);

    List<HistoricTaskBo> listHistoricTask(String processInstanceId, boolean withUserInfo);
}
